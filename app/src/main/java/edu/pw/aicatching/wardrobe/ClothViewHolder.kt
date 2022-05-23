package edu.pw.aicatching.wardrobe

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.network.Cloth

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root) {
    fun bind(cloth: Cloth, listener: (Cloth) -> Unit) {
        cloth.imgSrcUrl.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            wardrobeGalleryItemBinding.clothImage.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
        }
        wardrobeGalleryItemBinding.clothCategory.text = cloth.id
        wardrobeGalleryItemBinding.cardView.setOnClickListener { listener(cloth) }
    }
}
