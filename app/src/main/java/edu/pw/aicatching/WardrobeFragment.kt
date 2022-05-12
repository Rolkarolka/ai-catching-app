package edu.pw.aicatching

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import edu.pw.aicatching.databinding.FragmentWardrobeBinding


class WardrobeFragment : Fragment() {

    lateinit var clothList: List<Cloth>
    private lateinit var binding: FragmentWardrobeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clothList = clothInfo()
        binding = FragmentWardrobeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        val mainActivity = this

        binding.wardrobeGallery.apply {
            layoutManager = GridLayoutManager(mainActivity.context, 3)
            adapter = WardrobeGalleryAdapter(clothList)
        }
        return inflater.inflate(R.layout.fragment_wardrobe, container, false)
    }

    private fun clothInfo(): List<Cloth> {
        val cloth_1 = Cloth(
            1,
            Uri.parse("https://1.bp.blogspot.com/-32Htq53fZ2U/V4fNaNSN0DI/AAAAAAAAD38/_F9scm6i-a8IfcLZPzSOx8x0oL7AyMgfgCLcB/s1600/spodnica%2Bz%2Bwysokim%2Bstanem-2.jpg"),
            "Blouse"
        )
        val cloth_2 = Cloth(
            1,
            Uri.parse("https://1.bp.blogspot.com/-32Htq53fZ2U/V4fNaNSN0DI/AAAAAAAAD38/_F9scm6i-a8IfcLZPzSOx8x0oL7AyMgfgCLcB/s1600/spodnica%2Bz%2Bwysokim%2Bstanem-2.jpg"),
            "Bluza"
        )
        val cloth_3 = Cloth(
            1,
            Uri.parse("https://1.bp.blogspot.com/-32Htq53fZ2U/V4fNaNSN0DI/AAAAAAAAD38/_F9scm6i-a8IfcLZPzSOx8x0oL7AyMgfgCLcB/s1600/spodnica%2Bz%2Bwysokim%2Bstanem-2.jpg"),
            "Blouse"
        )
        val cloth_4 = Cloth(
            1,
            Uri.parse("https://1.bp.blogspot.com/-32Htq53fZ2U/V4fNaNSN0DI/AAAAAAAAD38/_F9scm6i-a8IfcLZPzSOx8x0oL7AyMgfgCLcB/s1600/spodnica%2Bz%2Bwysokim%2Bstanem-2.jpg"),
            "Bluza"
        )

        return listOf(cloth_1, cloth_2, cloth_3, cloth_4)
    }


}