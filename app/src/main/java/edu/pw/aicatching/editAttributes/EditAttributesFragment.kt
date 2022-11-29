package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel
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

        val adapter = EditAttributeAdapter(viewModel.getValuesOfClothAttributes())

        if (viewModel.mainCloth.value?.attributes != null) {
            viewModel.mainCloth.value?.attributes?.asMap()?.let { adapter.setAttributesMap(it) }
        } else {
            ClothAttributes(null, null).asMap().let { adapter.setAttributesMap(it) }
        }

        binding.root.editAttributesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.root.editAttributesList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveAttributesValueButton.setOnClickListener {
            viewModel.mainCloth.value = updateCloth()
        }
    }

    private fun updateCloth(): Cloth? {
        // TODO read attributes from fields, validate and set
        return viewModel.mainCloth.value
    }
}
