package edu.pw.aicatching.wardrobe

import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.Cloth

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root) {
    fun bind(
        cloth: Cloth,
        onClickListener: (Cloth, View) -> Unit,
        onLongClickListener: (Cloth, View) -> Boolean,
        viewHolderColor: Int
    ) {
        wardrobeGalleryItemBinding.cardView.setBackgroundColor(viewHolderColor)
        cloth.imgSrcUrl.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            wardrobeGalleryItemBinding.clothImage.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
        }
        wardrobeGalleryItemBinding.clothCategory.text = cloth.part
        wardrobeGalleryItemBinding.cardView.setOnClickListener { view -> onClickListener(cloth, view) }
        wardrobeGalleryItemBinding.cardView.setOnLongClickListener { view -> onLongClickListener(cloth, view) }
    }
}
