package edu.pw.aicatching.wardrobe

import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.network.Cloth

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root) {
    fun bind(cloth: Cloth, listener: (Cloth) -> Unit) {
//        wardrobeGalleryItemBinding.clothImage.
        wardrobeGalleryItemBinding.clothCategory.text = cloth.id
        wardrobeGalleryItemBinding.cardView.setOnClickListener { listener(cloth) }
    }
}
