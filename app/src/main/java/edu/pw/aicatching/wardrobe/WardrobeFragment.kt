package edu.pw.aicatching.wardrobe

import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentWardrobeBinding
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.viewModels.ClothViewModel

class WardrobeFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()

    private lateinit var adapter: WardrobeGalleryAdapter
    private var clothListCopy = mutableListOf<Cloth>()
    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        val view = binding.root
        val mainActivity = this
        binding.wardrobeGallery.apply {
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
        binding.wardrobeGallery.adapter = adapter

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.toolbar_main, menu)
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

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean { return false }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(text: String) {
        val filteredCloths = mutableListOf<Cloth>()

        if (text.isNotEmpty()) {
            for (cloth in clothListCopy) {
                if (cloth.part?.lowercase()?.contains(text.lowercase()) == true) {
                    filteredCloths.add(cloth)
                }
            }
        } else {
            filteredCloths += clothListCopy
        }
        adapter.filterList(filteredCloths)
    }
}
