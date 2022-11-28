package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
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
    }
}

