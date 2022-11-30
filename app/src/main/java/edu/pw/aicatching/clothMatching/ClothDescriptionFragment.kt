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
        viewModel.getOutfit()

        val imgUri = viewModel.mainCloth.value?.imgSrcUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        view.clothCategory.text = viewModel.mainCloth.value?.category ?: "Cloth"
        view.clothImage.load(imgUri)

        view.outfitMatching.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        view.outfitMatching.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attributesArray = viewModel.mainCloth.value?.attributes?.asMap()?.map { mapEntry -> "${mapEntry.key}: ${mapEntry.value}" } ?: ClothAttributes::class.memberProperties.associateBy { it ->
            it.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }.keys.toList()

        view.attributesListView.adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, attributesArray) }
        editButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.editAttributesFragment)
        )
    }
}
