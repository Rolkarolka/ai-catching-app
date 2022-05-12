package edu.pw.aicatching

import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.WardrobeGalleryItemBinding

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: WardrobeGalleryItemBinding
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root)
{
    fun bindCloth(cloth: Cloth) {
        wardrobeGalleryItemBinding.clothImage.setImageURI(cloth.image)
        wardrobeGalleryItemBinding.clothCategory.text = cloth.category
    }
}