package edu.pw.aicatching.editAttributes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemEditAttriibuteBinding

class EditAttributeAdapter(
    private val availableValues: Map<String, List<String>>,
    private val listener: (String, String) -> Unit
) : RecyclerView.Adapter<AttributeViewHolder>() {

    private var attributes = mutableMapOf<String, String>()

    fun setAttributesMap(attributes: Map<String, String>) {
        this.attributes = attributes.toMutableMap()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemEditAttriibuteBinding.inflate(from, parent, false)
        return AttributeViewHolder(binding)
    }

    override fun getItemCount(): Int = attributes.size

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        val key = attributes.keys.toList()[position]
        val value = attributes[key.lowercase()]
        availableValues[key.lowercase()]?.let { holder.bind(key, value, it, listener) }
    }
}
