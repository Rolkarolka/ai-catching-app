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
        editAttributeItemBinding.clothAttributeLabel.text = attributeName
        val clothAttributeList = availableValues.toMutableList()
        if (attributeValue == null) {
            clothAttributeList.add(0, "")
        }

        editAttributeItemBinding.clothAttributeSpinner.adapter =
            ArrayAdapter(this.itemView.context, simple_spinner_dropdown_item, clothAttributeList)
        editAttributeItemBinding.clothAttributeSpinner.setSelection(clothAttributeList.indexOf(attributeValue))

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                listener(attributeName, clothAttributeList[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { editAttributeItemBinding.clothAttributeSpinner.onItemSelectedListener = it }
    }
}
