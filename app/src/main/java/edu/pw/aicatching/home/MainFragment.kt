package edu.pw.aicatching.home

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import coil.load
import edu.pw.aicatching.R
import edu.pw.aicatching.models.ClothSize
import edu.pw.aicatching.viewModels.UserViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_cloth.view.*
import kotlinx.android.synthetic.main.view_top_settings.*

class MainFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()

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
                user?.preferences?.photoUrl?.let { photo ->
                    userAvatar.setImageURI(Uri.parse(photo))
                }
                favColorAttribute.backgroundTintList = user?.preferences?.favouriteColor
                    ?.let { ColorStateList.valueOf(it) }
                clothSizeAttribute.text = user?.preferences?.clothSize?.let { it.name } ?: "Cloth\nSize"
                shoeSizeAttribute.text = if (user?.preferences?.shoeSize?.isNotEmpty() == true)
                    user.preferences.shoeSize
                else
                    "Shoe\nSize"
            }
        }

        val inspirationUrl = viewModel.getInspiration()
        val imgUri = inspirationUrl.toUri().buildUpon()?.scheme("https")?.build()
        view.inspiration.load(imgUri)

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
