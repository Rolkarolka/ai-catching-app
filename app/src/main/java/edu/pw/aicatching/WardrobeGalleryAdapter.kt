package edu.pw.aicatching

import android.view.LayoutInflater
import android.view.ViewGroup
import edu.pw.aicatching.databinding.WardrobeGalleryItemBinding
import androidx.recyclerview.widget.RecyclerView

// TODO pakietowanie
class WardrobeGalleryAdapter(private val cloths: List<Cloth>)
    : RecyclerView.Adapter<ClothViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = WardrobeGalleryItemBinding.inflate(from, parent, false)
        return ClothViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        holder.bindCloth(cloths[position])
    }

    override fun getItemCount(): Int = cloths.size


}