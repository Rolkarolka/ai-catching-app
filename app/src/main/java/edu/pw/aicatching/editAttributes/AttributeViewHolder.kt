package edu.pw.aicatching.editAttributes

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemEditAttriibuteBinding

class AttributeViewHolder (
    private val editAttributeItemBinding: ItemEditAttriibuteBinding,
) : RecyclerView.ViewHolder(editAttributeItemBinding.root) {
    var selectedValue: String? = null

    fun bind(attributeName: String, attributeValue: String?, availableValues: List<String>) {
        editAttributeItemBinding.clothAttributeLabel.text = attributeName
        selectedValue = attributeValue
        setClothAttributeSpinner(attributeValue, listOf("") + availableValues)
    }

    private fun setClothAttributeSpinner(value: String?, clothAttributeList: List<String>) {
        editAttributeItemBinding.clothAttributeSpinner.adapter = ArrayAdapter(this.itemView.context, R.layout.simple_spinner_dropdown_item, clothAttributeList)
        editAttributeItemBinding.clothAttributeSpinner.setSelection(clothAttributeList.indexOf(value))

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedValue= clothAttributeList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { editAttributeItemBinding.clothAttributeSpinner.onItemSelectedListener = it }
    }
}

