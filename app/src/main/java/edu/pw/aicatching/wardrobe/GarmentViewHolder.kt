package edu.pw.aicatching.wardrobe

import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.Garment

class GarmentViewHolder(
    private val wardrobeGalleryItemBinding: ItemGarmentBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root) {
    fun bind(
        garment: Garment,
        onClickListener: (Garment, View) -> Unit,
        onLongClickListener: (Garment, View) -> Boolean,
        viewHolderColor: Int
    ) {
        wardrobeGalleryItemBinding.apply {
            loadImage(garment.imgSrcUrl)
            garmentCategory.text = garment.part
        }

        wardrobeGalleryItemBinding.cardView.apply {
            setBackgroundColor(viewHolderColor)
            setOnClickListener { view -> onClickListener(garment, view) }
            setOnLongClickListener { view -> onLongClickListener(garment, view) }
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
