package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentClothDescriptionBinding
import edu.pw.aicatching.network.AICatchingApiService
import edu.pw.aicatching.repositories.MainRepository
import kotlinx.android.synthetic.main.fragment_cloth_description.view.*
import kotlinx.android.synthetic.main.item_cloth.view.*

class ClothDescriptionFragment : Fragment() {
    private val service = AICatchingApiService.getInstance()
    lateinit var viewModel: OutfitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentClothDescriptionBinding.inflate(inflater, container, false)

        val adapter = OutfitGalleryAdapter {
            val bundle = bundleOf("clothCategory" to it.id, "clothImage" to it.imgSrcUrl)
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.clothDescriptionFragment, bundle) }
        }
        viewModel = ViewModelProvider(this, OutfitViewModelFactory(MainRepository(service))).get(OutfitViewModel::class.java)
        viewModel.outfitList.observe(
            viewLifecycleOwner
        ) {
            adapter.setClothList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { }
        viewModel.getOutfit()

        val view = binding.root
        val clothCategory: String = arguments?.get("clothCategory") as String
        val clothImageURL: String = arguments?.get("clothImage") as String
        val imgUri = clothImageURL.toUri().buildUpon().scheme("https").build()
        view.clothCategory.text = clothCategory
        view.clothImage.load(imgUri)

        view.outfitMatching.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        view.outfitMatching.adapter = adapter

        return view
    }
}
