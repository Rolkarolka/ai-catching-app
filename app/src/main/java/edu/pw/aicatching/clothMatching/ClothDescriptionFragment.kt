package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.pw.aicatching.R
import kotlinx.android.synthetic.main.fragment_cloth_description.view.*

class ClothDescriptionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cloth_description, container, false)
        val clothCategory: String = arguments?.get("clothCategory") as String
        val clothImage: Int = arguments?.get("clothImage") as Int
        view.cloth_image.setImageResource(clothImage)
        view.cloth_category.text = clothCategory
        return view
    }
}