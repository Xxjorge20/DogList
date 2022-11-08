package ies.luiscarrillodesotomayor.doglist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ies.luiscarrillodesotomayor.doglist.Vista.DogAdapter
import ies.luiscarrillodesotomayor.doglist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogsImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        // Uso de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Navegador.setOnQueryTextListener(this)
        initRecyclerView()
    }


    private fun getRetrofit(): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String)  {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<DogsResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                     val images = puppies?.message ?: emptyList()
                     dogsImages.clear()
                     dogsImages.addAll(images)
                     adapter.notifyDataSetChanged()
                }
                else
                {
                    showError()
                }
                hideKeyboard()
            }

        }

    }

    // metodo para llamaar al adapter
    private fun initRecyclerView()
    {
        adapter = DogAdapter(dogsImages)
        binding.ListaPerros.layoutManager = LinearLayoutManager(this)
        binding.ListaPerros.adapter = adapter

    }

    private fun showError()
    {
        //mostramos error
        Toast.makeText(this, "Ha Ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty())
        {
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun hideKeyboard()
    {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}