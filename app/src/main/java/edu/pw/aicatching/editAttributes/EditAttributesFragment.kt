package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import kotlinx.android.synthetic.main.fragment_edit_attributes.*
import kotlinx.android.synthetic.main.fragment_edit_attributes.view.*


class EditAttributesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditAttributesBinding.inflate(inflater, container, false)

        val adapter = EditAttributeAdapter()
        adapter.setAttributesMap(mapOf("Color" to "Blue", "Type" to "Dots", "Example" to "it")) // TODO list from cloth

        binding.root.editAttributesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.root.editAttributesList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveAttributesValueButton.setOnClickListener{
            val bundle = bundleOf("clothCategory" to 1, "clothImage" to "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg")
            Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment, bundle)
        }
    }
}

