package edu.pw.aicatching.wardrobe

import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.Cloth

class WardrobeGalleryAdapter :
    RecyclerView.Adapter<ClothViewHolder>() {
    var multiSelect: Boolean = false
    val selectedClothes = mutableListOf<Cloth>()
    private var cloths = mutableListOf<Cloth>()

    fun setClothList(cloths: List<Cloth>) {
        this.cloths = cloths.toMutableList()
        notifyDataSetChanged()
    }

    fun filterList(filteredCloths: MutableList<Cloth>) {
        cloths = filteredCloths
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemClothBinding.inflate(from, parent, false)
        binding.root.layoutParams.height = parent.measuredWidth / 2
        return ClothViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        val onClickListener: (Cloth, View) -> Unit = { cloth, view ->
            if (this.multiSelect) {
                this.selectClothes(cloth, view)
            } else {
                val bundle = bundleOf("clothCategory" to cloth.id, "clothImage" to cloth.imgSrcUrl)
                Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment, bundle)
            }
        }
        val onLongClickListener: (Cloth, View) -> Boolean = { cloth, view ->
            (view.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
            selectClothes(cloth, view)
            true
        }
        val cloth = cloths[position]
        val backgroundViewHolderColor = if (selectedClothes.contains(cloth)) Color.LTGRAY else Color.WHITE
        holder.bind(cloth, onClickListener, onLongClickListener, backgroundViewHolderColor)
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            multiSelect = true
            menu?.add("Delete")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            for (cloth in selectedClothes) {
                cloths.remove(cloth) // TODO remove from server
            }
            mode?.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            multiSelect = false
            selectedClothes.clear()
            notifyDataSetChanged()
        }
    }

    private fun selectClothes(cloth: Cloth, view: View) {
        if (selectedClothes.contains(cloth)) {
            selectedClothes.remove(cloth)
            view.setBackgroundColor(Color.WHITE)
        } else {
            selectedClothes.add(cloth)
            view.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int = cloths.size
}
