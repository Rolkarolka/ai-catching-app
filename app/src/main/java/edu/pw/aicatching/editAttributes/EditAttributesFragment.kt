package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel

class EditAttributesFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()
    private val changedAttrValuesMap = mutableMapOf<String, String>()
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
        val adapter = EditAttributeAdapter { key, value ->
            changedAttrValuesMap[key] = value
        }
        viewModel.availableAttributesValues.observe(
            viewLifecycleOwner
        ) {
            adapter.setAttributesValues(it)
        }

        if (viewModel.mainClothAttributes.value != null) {
            viewModel.mainClothAttributes.value?.asMap()?.let { adapter.setAttributesMap(it) }
        } else {
            ClothAttributes(null, null, null).asMap().let { adapter.setAttributesMap(it) }
        }

        binding.editAttributesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.editAttributesList.adapter = adapter

        val imgUri = viewModel.mainCloth.value?.imgSrcUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        binding.item.clothCategory.text = viewModel.mainCloth.value?.part ?: "Cloth"
        binding.item.clothImage.load(imgUri)

        return view
    }

    private fun String?.compareChange(prevValue: String?) =
        if (this == prevValue || this.isNullOrBlank()) null else this

    override fun onDestroyView() {
        val updatedClothesAttributes = ClothAttributes(
            color = changedAttrValuesMap["color"]
                .compareChange(viewModel.mainClothAttributes.value?.color),
            texture = changedAttrValuesMap["texture"]
                .compareChange(viewModel.mainClothAttributes.value?.texture),
            sleeveLength = changedAttrValuesMap["sleeve_length"]
                .compareChange(viewModel.mainClothAttributes.value?.sleeveLength),
            garmentLength = changedAttrValuesMap["garment_length"]
                .compareChange(viewModel.mainClothAttributes.value?.garmentLength),
            necklineType = changedAttrValuesMap["neckline_type"]
                .compareChange(viewModel.mainClothAttributes.value?.necklineType),
            fabric = changedAttrValuesMap["fabric"]
                .compareChange(viewModel.mainClothAttributes.value?.fabric)
        )
        viewModel.mainCloth.value?.garmentID?.let { clothID ->
            viewModel.updateClothAttributes(clothID, updatedClothesAttributes)
        }
        super.onDestroyView()
        _binding = null
    }
}
