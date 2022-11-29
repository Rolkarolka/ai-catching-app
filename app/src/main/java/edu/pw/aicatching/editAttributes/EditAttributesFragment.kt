package edu.pw.aicatching.editAttributes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import edu.pw.aicatching.databinding.FragmentEditAttributesBinding
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.asMap
import edu.pw.aicatching.viewModels.ClothViewModel
import kotlinx.android.synthetic.main.fragment_edit_attributes.*
import kotlinx.android.synthetic.main.fragment_edit_attributes.view.*
import kotlinx.android.synthetic.main.item_cloth.view.*

class EditAttributesFragment : Fragment() {
    private val viewModel: ClothViewModel by activityViewModels()
    private val changedAttrValuesMap = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditAttributesBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = EditAttributeAdapter(viewModel.getValuesOfClothAttributes()) { key, value ->
            changedAttrValuesMap[key] = value
        }

        if (viewModel.mainCloth.value?.attributes != null) {
            viewModel.mainCloth.value?.attributes?.asMap()?.let { adapter.setAttributesMap(it) }
        } else {
            ClothAttributes(null, null).asMap().let { adapter.setAttributesMap(it) }
        }

        view.editAttributesList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        view.editAttributesList.adapter = adapter

        val imgUri = viewModel.mainCloth.value?.imgSrcUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        view.clothCategory.text = viewModel.mainCloth.value?.category ?: "Cloth"
        view.clothImage.load(imgUri)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveAttributesValueButton.setOnClickListener {
            viewModel.mainCloth.value = viewModel.mainCloth.value?.copy(
                attributes = ClothAttributes(
                    color = changedAttrValuesMap["Color"].equalOrBlank(viewModel.mainCloth.value?.attributes?.color),
                    pattern = changedAttrValuesMap["Pattern"].equalOrBlank(viewModel.mainCloth.value?.attributes?.pattern),
                )
            )
            viewModel.updateClothAttributes()
        }
    }

    private fun String?.equalOrBlank(prevValue: String?) =
        if (this.isNullOrEmpty() || this == prevValue) prevValue else this
}
