package edu.pw.aicatching.wardrobe

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentWardrobeBinding
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.network.AICatchingApiService
import edu.pw.aicatching.repositories.MainRepository
import kotlinx.android.synthetic.main.fragment_wardrobe.view.*


class WardrobeFragment : Fragment() {
    private val service = AICatchingApiService.getInstance()
    lateinit var viewModel: WardrobeViewModel
    private lateinit var adapter: WardrobeGalleryAdapter
    private var clothListCopy = mutableListOf<Cloth>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWardrobeBinding.inflate(inflater)
        val mainActivity = this
        val view = binding.root
        view.wardrobeGallery.apply {
            layoutManager = GridLayoutManager(mainActivity.activity, 2)
        }

        adapter = WardrobeGalleryAdapter()
        view.wardrobeGallery.adapter = adapter

        viewModel = ViewModelProvider(this, WardrobeViewModelFactory(MainRepository(service)))[WardrobeViewModel::class.java]
        viewModel.wardrobeList.observe(
            viewLifecycleOwner
        ) {
            clothListCopy = it.toMutableList()
            adapter.setClothList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { }
        viewModel.getWardrobe()
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu,inflater)
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredCloths = mutableListOf<Cloth>()

        if (text.isNotEmpty()) {
            for (item in clothListCopy) {
                if (item.id.toString() == text) { // TODO filter with more attributes
                    filteredCloths.add(item)
                }
            }
        } else {
            filteredCloths += clothListCopy
        }
        adapter.filterList(filteredCloths)
    }
}
