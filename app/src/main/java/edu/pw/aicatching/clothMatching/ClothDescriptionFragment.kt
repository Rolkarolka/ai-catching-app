package edu.pw.aicatching.clothMatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.pw.aicatching.R
import kotlinx.android.synthetic.main.item_cloth.view.*

class ClothDescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cloth_description, container, false)
        val clothCategory: String = arguments?.get("clothCategory") as String
        val clothImage: Int = arguments?.get("clothImage") as Int
        view.clothImage.setImageResource(clothImage)
        view.clothCategory.text = clothCategory
        return view
    }
}
