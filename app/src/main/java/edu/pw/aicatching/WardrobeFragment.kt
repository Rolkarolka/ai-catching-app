package edu.pw.aicatching

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class WardrobeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_wardrobe, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}