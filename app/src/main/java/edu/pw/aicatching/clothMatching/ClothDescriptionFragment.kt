package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentClothDescriptionBinding
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.fragment_cloth_description.*
import kotlinx.android.synthetic.main.fragment_cloth_description.view.*
import kotlinx.android.synthetic.main.item_cloth.view.*

class ClothDescriptionFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClothDescriptionBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = OutfitGalleryAdapter { cloth ->
            viewModel.mainCloth.value = cloth
            Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
        }

        viewModel.outfitList.observe(
            viewLifecycleOwner
        ) {
            adapter.setClothList(it)
        }
        viewModel.outfitErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d(this::class.simpleName, "Creating new observer on outfitErrorMessage") }

        viewModel.mainCloth.observe(
            viewLifecycleOwner
        ) { it ->
            val imgUri = it.imgSrcUrl.toUri().buildUpon()?.scheme("https")?.build()
            view.clothCategory.text = it.part ?: "Cloth"
            view.clothImage.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
            viewModel.getOutfit(it.garmentID)
        }

        viewModel.mainCloth.value?.garmentID?.let { viewModel.getAttributes(it) }
        view.outfitMatching.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        view.outfitMatching.adapter = adapter

        view.attributesListView.adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, createAttributesArray()) }
        viewModel.mainClothAttributes.observe(
            viewLifecycleOwner
        ) {
            view.attributesListView.adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, createAttributesArray()) }
        }

        return view
    }
    private fun String.capitalize() = this.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }
    private fun createAttributesArray() =
        viewModel.mainClothAttributes.value
            ?.asMap()
            ?.map { mapEntry ->
                val formattedKeys = mapEntry.key.split(Regex("(?=\\p{Upper})")).joinToString(separator = " ")
                val formattedValues = mapEntry.value.split("_")[0].capitalize()
                "$formattedKeys: $formattedValues"
            }
            ?: ClothAttributes::class.memberProperties.associateBy {
                it.name.capitalize()
            }.keys.toList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.editAttributesFragment)
        )
    }
}
