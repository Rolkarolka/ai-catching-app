package edu.pw.aicatching.editAttributes

import android.R.layout.simple_spinner_dropdown_item
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemEditAttriibuteBinding

class AttributeViewHolder(
    private val editAttributeItemBinding: ItemEditAttriibuteBinding,
) : RecyclerView.ViewHolder(editAttributeItemBinding.root) {

    fun bind(
        attributeName: String,
        attributeValue: String?,
        availableValues: List<String>,
        listener: (String, String) -> Unit
    ) {
        editAttributeItemBinding.garmentAttributeLabel.text = attributeName
        val garmentAttributeList = availableValues.toMutableList()

        editAttributeItemBinding.garmentAttributeSpinner.apply {
            adapter = ArrayAdapter(
                this@AttributeViewHolder.itemView.context,
                simple_spinner_dropdown_item,
                garmentAttributeList
            )
            setSelection(garmentAttributeList.indexOf(attributeValue))
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    listener(attributeName, garmentAttributeList[position])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }
}
