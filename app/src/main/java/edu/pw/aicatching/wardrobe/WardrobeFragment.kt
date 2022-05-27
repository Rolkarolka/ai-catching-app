package edu.pw.aicatching.wardrobe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentWardrobeBinding
import edu.pw.aicatching.network.AICatchingApiService
import edu.pw.aicatching.repositories.MainRepository
import kotlinx.android.synthetic.main.fragment_wardrobe.view.*

class WardrobeFragment : Fragment() {
    private val service = AICatchingApiService.getInstance()
    lateinit var viewModel: WardrobeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWardrobeBinding.inflate(inflater)

        val adapter = WardrobeGalleryAdapter {
            val bundle = bundleOf("clothCategory" to it.id, "clothImage" to it.imgSrcUrl)
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.clothDescriptionFragment, bundle) }
        }

        viewModel = ViewModelProvider(this, WardrobeViewModelFactory(MainRepository(service))).get(WardrobeViewModel::class.java)
        viewModel.wardrobeList.observe(
            viewLifecycleOwner
        ) {
            adapter.setClothList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { }
        viewModel.getWardrobe()

        val mainActivity = this
        val view = binding.root

        view.wardrobe_gallery.apply {
            layoutManager = GridLayoutManager(mainActivity.activity, 2)
        }
        view.wardrobe_gallery.adapter = adapter
        return view
    }
}
