package edu.pw.aicatching.home

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import edu.pw.aicatching.R
import edu.pw.aicatching.models.ClothSize
import edu.pw.aicatching.viewModels.AuthorizationViewModel
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
            viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                username.text = user?.name + " " + user?.surname
                user?.photoUrl?.let { photo ->
                    userAvatar.setImageURI(Uri.parse(photo))
                }
                favColorAttribute.backgroundTintList = user?.preferences?.favouriteColor
                    ?.let { ColorStateList.valueOf(it) }
                clothSizeAttribute.text = user?.preferences?.clothSize?.let {
                        it -> if (it != ClothSize.UNKNOWN) it.name else "Cloth\nSize"
                } ?: "Cloth\nSize"
                shoeSizeAttribute.text = if (user?.preferences?.shoeSize?.isNotEmpty() == true)
                    user.preferences.shoeSize
                else
                    "Shoe\nSize"
            }
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
