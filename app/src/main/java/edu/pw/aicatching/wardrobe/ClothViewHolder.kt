package edu.pw.aicatching.wardrobe

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.model.Cloth
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemClothBinding

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root)
{
    fun bind(cloth: Cloth, listener: (Cloth) -> Unit) {
        wardrobeGalleryItemBinding.clothImage.setImageResource(cloth.image)
        wardrobeGalleryItemBinding.clothCategory.text = cloth.category
        wardrobeGalleryItemBinding.cardView.setOnClickListener{ listener(cloth) }
    }

    fun onClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
    }
}