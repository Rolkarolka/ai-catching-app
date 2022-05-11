package edu.pw.aicatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class WardrobeFragment : Fragment() {

    lateinit var clothList: List<Cloth>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clothList = clothInfo()
        return inflater.inflate(R.layout.fragment_wardrobe, container, false)
    }

    private fun clothInfo(): List<Cloth> {
        val cloth_1 = Cloth(
            1,
            "cloth.jpg",
            "Blouse"
        )
        val cloth_2 = Cloth(
            1,
            "cloth.jpg",
            "Bluza"
        )
        val cloth_3 = Cloth(
            1,
            "cloth.jpg",
            "Blouse"
        )
        val cloth_4 = Cloth(
            1,
            "cloth.jpg",
            "Bluza"
        )

        return listOf(cloth_1, cloth_2, cloth_3, cloth_4)
    }


}