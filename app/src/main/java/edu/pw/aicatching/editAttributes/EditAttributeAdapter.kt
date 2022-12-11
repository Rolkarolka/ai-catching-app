package edu.pw.aicatching.editAttributes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pw.aicatching.databinding.ItemEditAttriibuteBinding

class EditAttributeAdapter(
    private val listener: (String, String) -> Unit
) : RecyclerView.Adapter<AttributeViewHolder>() {

    private var attributes = mutableMapOf<String, String>()
    private var availableValues = mapOf<String, List<String>>()

    fun setAttributesMap(attributes: Map<String, String>) {
        this.attributes = attributes.toMutableMap()
        notifyDataSetChanged()
    }

    fun setAttributesValues(availableValues: Map<String, List<String>>) {
        this.availableValues = availableValues
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
        val formattedKey =  key.split(Regex("(?<=.)(?=\\p{Lu})")).joinToString(separator=" ").lowercase()
        val currentValue = attributes[key]
        val availableValuesOfKey = availableValues[formattedKey]
        if (availableValuesOfKey != null) {
            holder.bind(formattedKey, currentValue, availableValuesOfKey, listener)
        }
    }
}
