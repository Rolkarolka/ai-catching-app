package edu.pw.aicatching.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import edu.pw.aicatching.R
import edu.pw.aicatching.authorization.AuthorizationViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.view_top_settings.*

class MainFragment : Fragment() {
    private val viewModel: AuthorizationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.userLiveData.value != null) {
            username.text = "${viewModel.userLiveData.value!!.name} ${viewModel.userLiveData.value!!.surname}"
//            viewModel.filters.observe(viewLifecycleOwner, Observer { set ->
//                // Update the selected filters UI
//            }
            viewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
                favColorAttribute.backgroundTintList = user?.preferences?.favouriteColor
                    ?.let { ColorStateList.valueOf(it) }
            })
//            clothSizeAttribute
//            shoeSizeAttribute
//            favColorAttribute
//            userAvatar
        }


        showWardrobeButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.wardrobeFragment)
        )

        openCameraButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.cameraFragment)
        )

        appendUserDetails.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.userDetailsFragment)
        )
    }
}
