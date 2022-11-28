package edu.pw.aicatching.editAttributes

import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemEditAttriibuteBinding

class AttributeViewHolder (
    private val editAttributeItemBinding: ItemEditAttriibuteBinding,
) : RecyclerView.ViewHolder(editAttributeItemBinding.root) {

    fun bind(attributeName: String, attributeValue: String) {
        editAttributeItemBinding.clothAttributeLabel.text = attributeName
        editAttributeItemBinding.clothAttributeEditText.setText(attributeValue)
    }
}

