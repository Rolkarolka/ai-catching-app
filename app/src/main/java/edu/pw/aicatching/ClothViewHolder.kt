package edu.pw.aicatching

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemClothBinding

class ClothViewHolder(
    private val wardrobeGalleryItemBinding: ItemClothBinding,
) : RecyclerView.ViewHolder(wardrobeGalleryItemBinding.root)
{
    fun bindCloth(cloth: Cloth) {
        wardrobeGalleryItemBinding.clothImage.setImageResource(cloth.image)
        wardrobeGalleryItemBinding.clothCategory.text = cloth.category
    }

    fun onClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
    }
}