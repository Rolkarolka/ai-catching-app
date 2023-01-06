package edu.pw.aicatching.clothMatching

import android.os.Bundle
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
import edu.pw.aicatching.databinding.FragmentClothDescriptionBinding
import edu.pw.aicatching.databinding.ItemClothBinding
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel
import kotlin.reflect.full.memberProperties

class ClothDescriptionFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()
    private var _binding: FragmentClothDescriptionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClothDescriptionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.item.apply {
            clothImage.load(R.drawable.ic_loading)
            setCategory("")
        }

        viewModel.mainCloth.observe(
            viewLifecycleOwner
        ) {
            viewModel.getOutfit(it.garmentID)
            viewModel.getAttributes(it.garmentID)
            binding.item.apply {
                loadImage(it.imgSrcUrl)
                setCategory(it.part)
            }
        }

        binding.outfitMatching.setOutfitList(view)
        binding.attributesListView.setAttributesListView()

        return view

//        viewModel.outfitErrorMessage.observe( TODO for every errorMessage
//            viewLifecycleOwner
//        ) { Log.d(this::class.simpleName, "Creating new observer on outfitErrorMessage") }
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
        val outfitGalleryAdapter = OutfitGalleryAdapter { cloth ->
            viewModel.mainCloth.value = cloth
            Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
        }

        viewModel.outfitList.observe(
            viewLifecycleOwner
        ) {
            outfitGalleryAdapter.setClothList(it)
        }
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = outfitGalleryAdapter
    }

    private fun ItemClothBinding.setCategory(category: String?) {
        clothCategory.text = category ?: "Cloth"
    }

    private fun ListView.setAttributesListView() {
        adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, createAttributesArray()) }
        viewModel.mainClothAttributes.observe(
            viewLifecycleOwner
        ) {
            adapter = activity?.let {
                ArrayAdapter(it, R.layout.item_attribute, createAttributesArray())
            }
        }
    }

    private fun ItemClothBinding.loadImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        clothImage.load(imgUri) {
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_damage_image)
        }
    }

    private fun createAttributesArray() =
        viewModel.mainClothAttributes.value
            ?.asMap()
            ?.map { mapEntry ->
                val formattedKeys = mapEntry.key.split(Regex(PATTERN)).joinToString(separator = " ")
                val formattedValues = mapEntry.value.split("_")[0].capitalize()
                "$formattedKeys: $formattedValues"
            }
            ?: ClothAttributes::class.memberProperties.associateBy {
                it.name.capitalize()
            }.keys.toList()

    private fun String.capitalize() = this.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }

    companion object {
        const val PATTERN = "(?=\\p{Upper})"
    }
}
