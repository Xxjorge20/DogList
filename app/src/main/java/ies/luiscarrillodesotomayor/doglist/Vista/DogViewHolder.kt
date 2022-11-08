package ies.luiscarrillodesotomayor.doglist.Vista

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ies.luiscarrillodesotomayor.doglist.databinding.ItemDogBinding

class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemDogBinding.bind(view)

    fun bind(image: String) {
        Glide.with(binding.ivDog.context)
            .load(image)
            .into(binding.ivDog)
    }
}
