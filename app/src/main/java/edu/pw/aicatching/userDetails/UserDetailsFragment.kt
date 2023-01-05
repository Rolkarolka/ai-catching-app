package edu.pw.aicatching.userDetails

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import coil.load
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentClothDescriptionBinding
import edu.pw.aicatching.databinding.FragmentUserDetailsBinding
import edu.pw.aicatching.models.ClothSize
import edu.pw.aicatching.models.Color
import edu.pw.aicatching.models.UserPreferences
import edu.pw.aicatching.viewModels.UserViewModel
import java.io.ByteArrayOutputStream

class UserDetailsFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var colorPickerManager: ColorPickerPreferenceManager
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val pickMediaResult = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.currentUserAvatar.setImageURI(uri)
            Log.d("UserDetailsFragment:PhotoPicker", "Selected URI: $uri")
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri))
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            viewModel.updateUserPhoto(stream.toByteArray())
        } else {
            Log.d("UserDetailsFragment:PhotoPicker", "No media selected")
        }
    }
    private val changedPrefValuesMap: MutableMap<String, Any> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        colorPickerManager = ColorPickerPreferenceManager.getInstance(this.context)
        return view
    }

    override fun onDestroyView() {
        val updatedUserPreferences = UserPreferences(
            shoeSize = changedPrefValuesMap["shoeSize"].toString().compareChange(viewModel.userLiveData.value?.preferences?.shoeSize.toString()),
            clothSize = ClothSize.from(changedPrefValuesMap["clothSize"].toString().compareChange(viewModel.userLiveData.value?.preferences?.clothSize?.name.toString())),
            favouriteColor = changedPrefValuesMap["favouriteColor"].toString().compareChange(viewModel.userLiveData.value?.preferences?.favouriteColor?.name.toString())
                ?.let { Color.valueOf(it) }
        )
        viewModel.updateUserPreferences(updatedUserPreferences)
        super.onDestroyView()
        _binding = null

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClothSpinner(ClothSize.values().map { it.toString() }.toList())
        setShoeSpinner((35..45).toList().map { it.toString() })
        setColorPicker()
        setAvatar()

        binding.logOutButton.setOnClickListener {
            viewModel.logOut()
            this.view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.authorizationFragment) }
        }

        binding.deleteAccountButton.setOnClickListener {
            viewModel.deleteUser()
            this.view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.authorizationFragment) }
        }
    }

    private fun setClothSpinner(clothSizesArray: List<String>) {
        binding.clothSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, clothSizesArray)

        viewModel.userLiveData.value?.preferences
            ?.let { clothSizesArray.indexOf(it.clothSize.toString()) }
            ?.let { binding.clothSizeSpinner.setSelection(it) }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                changedPrefValuesMap["clothSize"] = ClothSize.valueOf(clothSizesArray[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { binding.clothSizeSpinner.onItemSelectedListener = it }
    }

    private fun setShoeSpinner(shoeSizesArray: List<String>) {
        binding.shoeSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, shoeSizesArray)
        viewModel.userLiveData.value?.preferences
            ?.let { shoeSizesArray.indexOf(it.shoeSize) }
            ?.let { binding.shoeSizeSpinner.setSelection(it) }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                changedPrefValuesMap["shoeSize"] = shoeSizesArray[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { binding.shoeSizeSpinner.onItemSelectedListener = it }
    }

    private fun setColorPicker() {
        binding.favColorPickerView.preferenceName = "FavColorPicker"
        viewModel.userLiveData.value?.preferences?.let {
            it.favouriteColor?.let { favColor ->
                colorPickerManager.setColor("FavColorPicker", android.graphics.Color.parseColor(favColor.hexValue))
            }
        }

        binding.favColorPickerView.setColorListener(
            ColorEnvelopeListener { envelope, _ ->
                binding.favouriteColorView.backgroundTintList = ColorStateList.valueOf(envelope.color)
                val color = Color.from(envelope.argb)
                if (color != null) {
                    changedPrefValuesMap["favouriteColor"] = color.name
                }
            }
        )
    }

    private fun setAvatar() {
        viewModel.userLiveData.value?.preferences?.photoUrl?.let { photo ->
            binding.currentUserAvatar.load(photo.toUri().buildUpon()?.scheme("https")?.build()) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_avatar)
            }
        }
        binding.changeUserPhotoButton.setOnClickListener {
            pickMediaResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun String?.compareChange(prevValue: String?) =
        if (this == prevValue || this.isNullOrBlank()) null else this
}
