package edu.pw.aicatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_wardrobe.view.*

class WardrobeFragment : Fragment() {
    private lateinit var clothList: List<Cloth>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clothList = clothInfo()
        val view = inflater.inflate(R.layout.fragment_wardrobe, container, false)
        val mainActivity = this
        view.wardrobe_gallery.apply {
            layoutManager = GridLayoutManager(mainActivity.activity, 2)
            adapter = WardrobeGalleryAdapter(clothList)
        }
//        view.wardrobe_gallery?.focusedChild?.setOnClickListener(View.OnClickListener() {
//            @Override
//            fun onClick(view: View) {
//                Navigation.findNavController(view).navigate(R.id.clothDescriptionFragment)
//            }
//        })
        return view
    }

    private fun clothInfo(): List<Cloth> {
        val cloth_1 = Cloth(
            1,
            R.drawable.cloth,
            "Blouse"
        )
        val cloth_2 = Cloth(
            1,
            R.drawable.cloth,
            "Bluza"
        )
        val cloth_3 = Cloth(
            1,
            R.drawable.cloth,
            "Blouse"
        )
        val cloth_4 = Cloth(
            1,
            R.drawable.cloth,
            "Bluza"
        )

        return listOf(cloth_1, cloth_2, cloth_3, cloth_4, cloth_1, cloth_2, cloth_3, cloth_4)
    }
}