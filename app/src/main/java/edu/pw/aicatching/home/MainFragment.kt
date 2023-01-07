package edu.pw.aicatching.home

import android.content.res.ColorStateList
import android.graphics.Color
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
import edu.pw.aicatching.databinding.FragmentMainBinding
import edu.pw.aicatching.databinding.ViewTopSettingsBinding
import edu.pw.aicatching.models.ClothSize
import edu.pw.aicatching.models.Color as FavColor
import edu.pw.aicatching.models.User
import edu.pw.aicatching.viewModels.UserViewModel

class MainFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getInspiration()
        // TODO errorMessage getInspiration
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMainToolbarUserInfo()
        setInspirationImage()

        binding.showWardrobeButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.wardrobeFragment)
        )

        binding.openCameraButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.cameraFragment)
        )

        binding.mainPageToolbar.appendUserDetails.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.userDetailsFragment)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMainToolbarUserInfo() {
        if (viewModel.user.value != null) {
            viewModel.user.observe(viewLifecycleOwner) { user ->
                user?.preferences?.let { preferences ->
                    binding.mainPageToolbar.apply {
                        setUsername(user)
                        setPhotoUrl(preferences.photoUrl)
                        setFavColor(preferences.favouriteColor)
                        setGarmentSize(preferences.clothSize)
                        setShoeSize(preferences.shoeSize)
                    }
                }
            }
        }
        viewModel.userPreferences.observe(viewLifecycleOwner) {
            viewModel.user.value = viewModel.user.value?.copy(preferences = it)
        }
    }

    private fun ViewTopSettingsBinding.setUsername(user: User) {
        val username = user.name + " " + user.surname
        this.username.text = username
    }

    private fun ViewTopSettingsBinding.setPhotoUrl(photoUrl: String?) =
        photoUrl?.let { photo ->
            this.userAvatar.load(photo.toUri().buildUpon()?.scheme("https")?.build()) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_avatar)
            }
        }

    private fun ViewTopSettingsBinding.setFavColor(favouriteColor: FavColor?) {
        this.favColorAttribute.backgroundTintList = favouriteColor
            ?.let { ColorStateList.valueOf(Color.parseColor(it.hexValue)) }
    }

    private fun ViewTopSettingsBinding.setGarmentSize(clothSize: ClothSize?) {
        this.clothSizeAttribute.text = clothSize?.name ?: "Cloth\nSize"
    }

    private fun ViewTopSettingsBinding.setShoeSize(shoeSize: String?) {
        this.shoeSizeAttribute.text = if (shoeSize?.isNotEmpty() == true)
            shoeSize
        else
            "Shoe\nSize"
    }

    private fun setInspirationImage() {
        viewModel.inspiration.observe(viewLifecycleOwner) {
            val inspirationUrl = it["link"]
            val imgUri = inspirationUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
            binding.inspiration.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
        }
    }
}
