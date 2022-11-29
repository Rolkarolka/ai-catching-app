package edu.pw.aicatching.clothMatching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.Cloth

class OutfitGalleryAdapter(
    private val listener: (Cloth) -> Unit
):
    RecyclerView.Adapter<ClothViewHolder>() {

    private var cloths = mutableListOf<Cloth>()

    fun setClothList(cloths: List<Cloth>) {
        this.cloths = cloths.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemClothBinding.inflate(from, parent, false)
        binding.root.layoutParams.width = parent.measuredWidth / 3
        binding.root.layoutParams.height = parent.measuredWidth / 3
        return ClothViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        holder.bind(cloths[position], listener)
    }

    override fun getItemCount(): Int = cloths.size
}
