package edu.pw.aicatching.wardrobe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import edu.pw.aicatching.models.Garment
import edu.pw.aicatching.viewModels.GarmentViewModel

class WardrobeFragment : Fragment() {
    private val viewModel: GarmentViewModel by activityViewModels()

    private lateinit var wardrobeGalleryAdapter: WardrobeGalleryAdapter
    private var garmentListCopy = mutableListOf<Garment>()
    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.getWardrobe()
        // TODO errorMessage getWardrobe
        prepareWardrobeGalleryAdapter(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMenuProvider()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteGarmentListener(): (Garment) -> Unit {
        val actionModeListener: (Garment) -> Unit = { garment ->
            viewModel.deleteGarment(garment.garmentID)
            viewModel.wardrobeList.value = viewModel.wardrobeList.value?.filter { it != garment }
        }
        // TODO errorMessage deleteGarment
        return actionModeListener
    }

    private fun chooseGarmentListener(view: View): (Garment) -> Unit {
        val listener: (Garment) -> Unit = { garment ->
            viewModel.mainGarment.value = garment
            Navigation.findNavController(view).navigate(R.id.garmentDescriptionFragment)
        }
        return listener
    }

    private fun prepareWardrobeGalleryAdapter(view: View) {
        wardrobeGalleryAdapter = WardrobeGalleryAdapter(chooseGarmentListener(view), deleteGarmentListener())
        viewModel.wardrobeList.observe(
            viewLifecycleOwner
        ) {
            garmentListCopy = it.toMutableList()
            wardrobeGalleryAdapter.setGarmentList(it)
        }

        binding.wardrobeGallery.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = wardrobeGalleryAdapter
        }
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
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
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun filter(text: String) {
        val filteredGarments = mutableListOf<Garment>()

        if (text.isNotEmpty()) {
            for (garment in garmentListCopy) {
                if (garment.part?.lowercase()?.contains(text.lowercase()) == true) {
                    filteredGarments.add(garment)
                }
            }
        } else {
            filteredGarments += garmentListCopy
        }
        wardrobeGalleryAdapter.filterList(filteredGarments)
    }
}
