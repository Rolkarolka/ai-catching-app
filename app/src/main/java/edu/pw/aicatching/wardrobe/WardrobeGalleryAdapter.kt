package edu.pw.aicatching.wardrobe

import android.graphics.Color.LTGRAY
import android.graphics.Color.WHITE
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.Garment

class WardrobeGalleryAdapter(
    val listener: (Garment) -> Unit,
    val actionModeListener: (Garment) -> Unit
) :
    RecyclerView.Adapter<GarmentViewHolder>() {
    var multiSelect: Boolean = false
    val selectedGarments = mutableListOf<Garment>()
    private var garments = mutableListOf<Garment>()

    fun setGarmentList(garments: List<Garment>) {
        this.garments = garments.toMutableList()
        notifyDataSetChanged()
    }

    fun filterList(filteredGarments: MutableList<Garment>) {
        garments = filteredGarments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarmentViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemGarmentBinding.inflate(from, parent, false)
        binding.root.layoutParams.height = parent.measuredWidth / 2
        return GarmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GarmentViewHolder, position: Int) {
        val onClickListener: (Garment, View) -> Unit = { garment, view ->
            if (this.multiSelect) {
                this.selectGarments(garment, view)
            } else {
                listener(garment)
            }
        }
        val onLongClickListener: (Garment, View) -> Boolean = { garment, view ->
            (view.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
            selectGarments(garment, view)
            true
        }
        val garment = garments[position]
        val backgroundViewHolderColor = if (selectedGarments.contains(garment)) LTGRAY else WHITE
        holder.bind(garment, onClickListener, onLongClickListener, backgroundViewHolderColor)
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
            for (garment in selectedGarments) {
                garments.remove(garment)
                actionModeListener(garment)
            }
            mode?.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            multiSelect = false
            selectedGarments.clear()
            notifyDataSetChanged()
        }
    }

    private fun selectGarments(garment: Garment, view: View) {
        if (selectedGarments.contains(garment)) {
            selectedGarments.remove(garment)
            view.setBackgroundColor(WHITE)
        } else {
            selectedGarments.add(garment)
            view.setBackgroundColor(LTGRAY)
        }
    }

    override fun getItemCount(): Int = garments.size
}
