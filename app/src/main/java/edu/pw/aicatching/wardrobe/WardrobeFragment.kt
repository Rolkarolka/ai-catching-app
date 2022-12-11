package edu.pw.aicatching.wardrobe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentWardrobeBinding
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.viewModels.ClothViewModel
import kotlinx.android.synthetic.main.fragment_wardrobe.view.*

class WardrobeFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()

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

        val listener: (Cloth) -> Unit = { cloth ->
            viewModel.mainCloth.value = cloth
            Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
        }
        val actionModeListener: (Cloth) -> Unit = { garment ->
            viewModel.deleteGarment(garment.garmentID)
            viewModel.wardrobeList.value = viewModel.wardrobeList.value?.filter { it != garment }
        }
        adapter = WardrobeGalleryAdapter(listener, actionModeListener)
        view.wardrobeGallery.adapter = adapter

        viewModel.wardrobeList.observe(
            viewLifecycleOwner
        ) {
            clothListCopy = it.toMutableList()
            adapter.setClothList(it)
        }
        viewModel.wardrobeErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d(this::class.simpleName, "Creating new observer on wardrobeErrorMessage") }
        viewModel.getWardrobe()
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
            for (cloth in clothListCopy) {
                if (cloth.part == text) {
                    filteredCloths.add(cloth)
                }
            }
        } else {
            filteredCloths += clothListCopy
        }
        adapter.filterList(filteredCloths)
    }
}
