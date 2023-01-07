package edu.pw.aicatching.garmentMatching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.Garment

class OutfitGalleryAdapter(
    private val listener: (Garment) -> Unit
) :
    RecyclerView.Adapter<GarmentViewHolder>() {

    private var garments = mutableListOf<Garment>()

    fun setGarmentList(garments: List<Garment>?) {
        this.garments = garments?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarmentViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemGarmentBinding.inflate(from, parent, false)
        binding.root.layoutParams.apply {
            width = parent.measuredWidth / VIEW_HOLDERS_IN_ROW
            height = parent.measuredWidth / VIEW_HOLDERS_IN_ROW
        }
        return GarmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GarmentViewHolder, position: Int) {
        holder.bind(garments[position], listener)
    }

    override fun getItemCount(): Int = garments.size

    companion object {
        const val VIEW_HOLDERS_IN_ROW = 3
    }
}
