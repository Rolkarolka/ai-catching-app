package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentWardrobeBinding
import edu.pw.aicatching.network.AICatchingApiService
import edu.pw.aicatching.repositories.MainRepository
import kotlinx.android.synthetic.main.fragment_cloth_description.view.*

class OutfitFragment : Fragment() {
    private val service = AICatchingApiService.getInstance()
    lateinit var viewModel: OutfitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWardrobeBinding.inflate(inflater)

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

        val mainActivity = this
        val view = binding.root
        view.outfitMatching.apply {
            layoutManager = GridLayoutManager(mainActivity.activity, 2)
        }
        view.outfitMatching.adapter = adapter
        return view
    }
}
