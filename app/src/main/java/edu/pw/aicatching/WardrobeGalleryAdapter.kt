package edu.pw.aicatching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemClothBinding

class WardrobeGalleryAdapter(
    private val cloths: List<Cloth>,
    private val listener: (Cloth) -> Unit)
    : RecyclerView.Adapter<ClothViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemClothBinding.inflate(from, parent, false)
        return ClothViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        holder.bind(cloths[position], listener)
    }

    override fun getItemCount(): Int = cloths.size


}