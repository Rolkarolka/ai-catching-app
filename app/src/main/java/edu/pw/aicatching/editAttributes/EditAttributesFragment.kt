package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.util.Log
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
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.GarmentAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.GarmentViewModel

class EditAttributesFragment : Fragment() {
    private val viewModel: GarmentViewModel by activityViewModels()
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

        viewModel.getValuesOfGarmentAttributes()
        handleGetValuesOfGarmentAttributesErrorMessage()
        handleUpdateGarmentAttributesErrorMessage()
        binding.editAttributesList.setAttributesList()

        viewModel.mainGarment.value?.let { garment ->
            binding.item.apply {
                loadImage(garment.imgSrcUrl)
                setCategory(garment.part)
            }
        }

        return view
    }

    private fun handleGetValuesOfGarmentAttributesErrorMessage() {
        viewModel.availableAttributesValuesErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("EditAttributesFragment:onCreateView:getValuesOfGarmentAttributes", it) }
    }

    private fun handleUpdateGarmentAttributesErrorMessage() {
        viewModel.mainGarmentAttributesErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("EditAttributesFragment:onCreateView:updateGarmentAttributes", it) }
    }

    override fun onDestroyView() {
        viewModel.mainGarment.value?.garmentID?.let { garmentID ->
            compareGarmentChanges()?.let {
                viewModel.updateGarmentAttributes(garmentID, it)
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

        val attributes = viewModel.mainGarmentAttributes.value ?: GarmentAttributes(null, null, null)
        editAttributesAdapter.setAttributesMap(attributes.asMap())

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = editAttributesAdapter
    }

    private fun ItemGarmentBinding.loadImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        garmentImage.load(imgUri) {
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_damage_image)
        }
    }

    private fun ItemGarmentBinding.setCategory(category: String?) {
        garmentCategory.text = category ?: "Garment"
    }

    private fun compareGarmentChanges(): GarmentAttributes? {
        viewModel.mainGarmentAttributes.value?.let { attributes ->
            val editedAttributes = GarmentAttributes(
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
            return if (sendChangedAttributes) editedAttributes else null
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
