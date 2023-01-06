package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel

class EditAttributesFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()
    private val changedAttrValuesMap = mutableMapOf<String, String>()
    private var sendChangedAttributes = false
    private var _binding: FragmentEditAttributesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAttributesBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.getValuesOfClothAttributes()
        binding.editAttributesList.setAttributesList()

        viewModel.mainCloth.value?.let { garment ->
            binding.item.apply {
                loadImage(garment.imgSrcUrl)
                setCategory(garment.part)
            }
        }

        return view
    }

    override fun onDestroyView() {
        viewModel.mainCloth.value?.garmentID?.let { clothID ->
            compareGarmentChanges()?.let {
                viewModel.updateClothAttributes(clothID, it)
            }
        }
        super.onDestroyView()
        _binding = null
    }

    private fun RecyclerView.setAttributesList() {
        val editAttributesAdapter = EditAttributeAdapter { key, value ->
            changedAttrValuesMap[key] = value
        }
        viewModel.availableAttributesValues.observe(
            viewLifecycleOwner
        ) { editAttributesAdapter.setAttributesValues(it) }

        val attributes = viewModel.mainClothAttributes.value ?: ClothAttributes(null, null, null)
        editAttributesAdapter.setAttributesMap(attributes.asMap())

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = editAttributesAdapter
    }

    private fun ItemClothBinding.loadImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        clothImage.load(imgUri) {
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_damage_image)
        }
    }

    private fun ItemClothBinding.setCategory(category: String?) {
        clothCategory.text = category ?: "Cloth"
    }

    private fun compareGarmentChanges(): ClothAttributes? {
        viewModel.mainClothAttributes.value?.let { attributes ->
            val attributes = ClothAttributes(
                color = changedAttrValuesMap["color"]
                    .compareChange(attributes.color),
                texture = changedAttrValuesMap["texture"]
                    .compareChange(attributes.texture),
                sleeveLength = changedAttrValuesMap["sleeve_length"]
                    .compareChange(attributes.sleeveLength),
                garmentLength = changedAttrValuesMap["garment_length"]
                    .compareChange(attributes.garmentLength),
                necklineType = changedAttrValuesMap["neckline_type"]
                    .compareChange(attributes.necklineType),
                fabric = changedAttrValuesMap["fabric"]
                    .compareChange(attributes.fabric)
            )
            return if (sendChangedAttributes) attributes else null

        }
        return null
    }

    private fun String?.compareChange(prevValue: String?) =
        if (this == prevValue || this.isNullOrBlank()) null
        else {
            sendChangedAttributes = true
            this
        }

}
