package edu.pw.aicatching.wardrobe

import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.model.Cloth

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root) {
    fun bind(cloth: Cloth, listener: (Cloth) -> Unit) {
        wardrobeGalleryItemBinding.clothImage.setImageResource(cloth.image)
        wardrobeGalleryItemBinding.clothCategory.text = cloth.category
        wardrobeGalleryItemBinding.cardView.setOnClickListener { listener(cloth) }
    }
}
