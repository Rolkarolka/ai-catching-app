package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.fragment_edit_attributes.*
import kotlinx.android.synthetic.main.fragment_edit_attributes.view.*


class EditAttributesFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditAttributesBinding.inflate(inflater, container, false)

        val adapter = EditAttributeAdapter()
        viewModel.mainCloth.value?.attributes?.asMap()?.let { adapter.setAttributesMap(it) }

        binding.root.editAttributesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.root.editAttributesList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveAttributesValueButton.setOnClickListener{
            viewModel.mainCloth.value = updateCloth()
            Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
        }
    }

    private fun updateCloth(): Cloth? {
        // TODO read attributes from fields, validate and set
        // if changes send new object to server
        return viewModel.mainCloth.value
    }

}
