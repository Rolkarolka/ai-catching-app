package edu.pw.aicatching.garmentMatching

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.Garment

class GarmentViewHolder(
    private val outfitGalleryItemBinding: ItemGarmentBinding,
) : RecyclerView.ViewHolder(outfitGalleryItemBinding.root) {
    fun bind(garment: Garment, listener: (Garment) -> Unit) {
        outfitGalleryItemBinding.apply {
            loadImage(garment.imgSrcUrl)
            garmentCategory.text = garment.part.toString()
            cardView.setOnClickListener { listener(garment) }
        }
    }

    private fun ItemGarmentBinding.loadImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        garmentImage.load(imgUri) {
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_damage_image)
        }
    }
}
