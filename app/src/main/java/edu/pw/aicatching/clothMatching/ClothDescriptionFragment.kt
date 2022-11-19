package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import coil.load
import edu.pw.aicatching.MainActivity
import edu.pw.aicatching.R
import kotlinx.android.synthetic.main.fragment_cloth_description.*
import kotlinx.android.synthetic.main.item_cloth.view.*

class ClothDescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cloth_description, container, false)
        val clothCategory: String = arguments?.get("clothCategory") as String
        val clothImageURL: String = arguments?.get("clothImage") as String
        val imgUri = clothImageURL.toUri().buildUpon().scheme("https").build()

        view.clothCategory.text = clothCategory
        view.clothImage.load(imgUri)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attributesArray = arrayListOf("Type", "Color", "Length")
        attributesListView.adapter = activity?.let { ArrayAdapter(it, R.layout.item_attribute, attributesArray) }
    }
}
