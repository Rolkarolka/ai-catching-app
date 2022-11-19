package edu.pw.aicatching.clothMatching

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.Cloth

class ClothViewHolder(
    private val outfitGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(outfitGalleryItemBinding.root) {
    fun bind(cloth: Cloth, listener: (Cloth) -> Unit) {
        cloth.imgSrcUrl.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            outfitGalleryItemBinding.clothImage.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
        }
        outfitGalleryItemBinding.clothCategory.text = cloth.id
        outfitGalleryItemBinding.cardView.setOnClickListener { listener(cloth) }
    }
}
