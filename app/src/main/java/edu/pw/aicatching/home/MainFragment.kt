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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.userLiveData.value != null) {
            viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                val username = user?.name + " " + user?.surname
                binding.mainPageToolbar.username.text = username
                user?.preferences?.photoUrl?.let { photo ->
                    binding.mainPageToolbar.userAvatar.load(photo.toUri().buildUpon()?.scheme("https")?.build()) {
                        placeholder(R.drawable.ic_loading)
                        error(R.drawable.ic_avatar)
                    }
                }
                binding.mainPageToolbar.favColorAttribute.backgroundTintList = user?.preferences?.favouriteColor
                    ?.let { ColorStateList.valueOf(Color.parseColor(it.hexValue)) }
                binding.mainPageToolbar.clothSizeAttribute.text = user?.preferences?.clothSize?.name ?: "Cloth\nSize"
                binding.mainPageToolbar.shoeSizeAttribute.text = if (user?.preferences?.shoeSize?.isNotEmpty() == true)
                    user.preferences.shoeSize
                else
                    "Shoe\nSize"
            }
        }

        viewModel.userPreferencesLiveData.observe(viewLifecycleOwner) {
            viewModel.userLiveData.value = viewModel.userLiveData
                .value?.copy(preferences = it)
        }

        viewModel.inspirationLiveData.observe(viewLifecycleOwner) {
            val inspirationUrl = it["link"]
            val imgUri = inspirationUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
            binding.inspiration.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_damage_image)
            }
        }

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
}
