package edu.pw.aicatching.garmentMatching

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentGarmentDescriptionBinding
import edu.pw.aicatching.databinding.ItemGarmentBinding
import edu.pw.aicatching.models.GarmentAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.GarmentViewModel
import kotlin.reflect.full.memberProperties

class GarmentDescriptionFragment : Fragment() {
    private val viewModel: GarmentViewModel by activityViewModels()
    private var _binding: FragmentGarmentDescriptionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGarmentDescriptionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.item.apply {
            garmentImage.load(R.drawable.ic_loading)
            setCategory("")
        }

        viewModel.mainGarment.observe(
            viewLifecycleOwner
        ) {
            viewModel.getOutfit(it.garmentID)
            viewModel.getAttributes(it.garmentID)
            binding.item.apply {
                loadImage(it.imgSrcUrl)
                setCategory(it.part)
            }
        }

        handleOutfitErrorMessage()
        handleGetAttributesErrorMessage()

        binding.outfitMatching.setOutfitList(view)
        binding.attributesListView.setAttributesListView()

        return view
    }

    private fun handleOutfitErrorMessage() {
        viewModel.outfitErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("GarmentDescriptionFragment:onCreateView:getOutfit", it) }
    }

    private fun handleGetAttributesErrorMessage() {
        viewModel.outfitErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("GarmentDescriptionFragment:onCreateView:getAttributes", it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.editAttributesFragment)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun RecyclerView.setOutfitList(view: View) {
        val outfitGalleryAdapter = OutfitGalleryAdapter { garment ->
            viewModel.mainGarment.value = garment
            Navigation.findNavController(view).navigate(R.id.garmentDescriptionFragment)
        }

        outfitGalleryAdapter.setGarmentList(emptyList())
        viewModel.outfitList.observe(
            viewLifecycleOwner
        ) {
            outfitGalleryAdapter.setGarmentList(it)
        }

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = outfitGalleryAdapter
    }

    private fun ItemGarmentBinding.setCategory(category: String?) {
        garmentCategory.text = category ?: "Garment"
    }

    private fun ListView.setAttributesListView() {
        adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, createEmptyAttributesArray()) }
        viewModel.mainGarmentAttributes.observe(
            viewLifecycleOwner
        ) {
            adapter = activity?.let {
                ArrayAdapter(it, R.layout.item_attribute, createAttributesArray())
            }
        }
    }

    private fun ItemGarmentBinding.loadImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        garmentImage.load(imgUri) {
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_damage_image)
        }
    }

    private fun createAttributesArray() =
        viewModel.mainGarmentAttributes.value
            ?.asMap()
            ?.map { mapEntry ->
                val formattedKeys = mapEntry.key.split(Regex(CAMELCASE_PATTERN_TO_SPLITTED_WORLDS))
                    .joinToString(separator = " ")
                val formattedValues = mapEntry.value.split("_")[0].capitalize()
                "$formattedKeys: $formattedValues"
            }
            ?: GarmentAttributes::class.memberProperties.associateBy {
                it.name.capitalize()
            }.keys.toList()

    private fun createEmptyAttributesArray() =
        GarmentAttributes::class.memberProperties.associateBy {
                it.name.capitalize()
            }.keys.toList()

    private fun String.capitalize() = this.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }

    companion object {
        const val CAMELCASE_PATTERN_TO_SPLITTED_WORLDS = "(?=\\p{Upper})"
    }
}
