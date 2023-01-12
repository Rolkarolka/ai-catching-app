package edu.pw.aicatching.userDetails

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import edu.pw.aicatching.databinding.FragmentUserDetailsBinding
import edu.pw.aicatching.models.Color
import edu.pw.aicatching.models.GarmentSize
import edu.pw.aicatching.models.UserPreferences
import edu.pw.aicatching.viewModels.UserViewModel
import java.io.ByteArrayOutputStream

class UserDetailsFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var colorPickerManager: ColorPickerPreferenceManager
    private var sendChangedAttributes = false
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream)
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
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        colorPickerManager = ColorPickerPreferenceManager.getInstance(this.context)
        handleLoggingErrorMessage()
        handleUpdateUserPhotoErrorMessage()
        handleUpdateUserPreferences()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGarmentSpinner(GarmentSize.values().map { it.toString() }.toList())
        setShoeSpinner((MIN_SHOE_SIZE..MAX_SHOE_SIZE).toList().map { it.toString() })
        setColorPicker()
        setAvatar()

        binding.logOutButton.setOnClickListener {
            viewModel.logOut()
            showProgressBar()
        }

        binding.deleteAccountButton.setOnClickListener {
            viewModel.deleteUser()
            showProgressBar()
        }

        viewModel.user.observe(
            viewLifecycleOwner
        ) { user ->
            if (user == null) {
                this.view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.authorizationFragment) }
            }
            hideProgressBar()
        }
    }

    override fun onDestroyView() {
        compareUserPreferences()?.let { viewModel.updateUserPreferences(it) }
        super.onDestroyView()
        _binding = null
    }

    private fun compareUserPreferences(): UserPreferences? {
        viewModel.user.value?.preferences?.let { preferences ->
            val editedPreferences = UserPreferences(
                shoeSize = changedPrefValuesMap["shoeSize"].toString()
                    .compareChange(preferences.shoeSize.toString()),
                garmentSize = GarmentSize.from(
                    changedPrefValuesMap["garmentSize"]
                        .toString()
                        .compareChange(preferences.garmentSize?.name.toString())
                ),
                favouriteColor = changedPrefValuesMap["favouriteColor"]
                    .toString()
                    .compareChange(preferences.favouriteColor?.name.toString())
                    ?.let { Color.valueOf(it)}
            )
            return if (sendChangedAttributes) editedPreferences else null
        }
        return null
    }

    private fun setGarmentSpinner(garmentSizesArray: List<String>) {
        binding.garmentSizeSpinner.adapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            garmentSizesArray
        )

        viewModel.user.value?.preferences
            ?.let { garmentSizesArray.indexOf(it.garmentSize.toString()) }
            ?.let { binding.garmentSizeSpinner.setSelection(it) }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                changedPrefValuesMap["garmentSize"] = GarmentSize.valueOf(garmentSizesArray[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { binding.garmentSizeSpinner.onItemSelectedListener = it }
    }

    private fun setShoeSpinner(shoeSizesArray: List<String>) {
        binding.shoeSizeSpinner.adapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            shoeSizesArray
        )
        viewModel.user.value?.preferences
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
        viewModel.user.value?.preferences?.let {
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
        viewModel.user.value?.preferences?.photoUrl?.let { photo ->
            binding.currentUserAvatar.load(photo.toUri().buildUpon()?.scheme("https")?.build()) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_avatar)
            }
        }
        binding.changeUserPhotoButton.setOnClickListener {
            pickMediaResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showProgressBar() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun handleLoggingErrorMessage() {
        viewModel.loggingErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("UserDetailsFragment:onCreateView", it) }
    }

    private fun handleUpdateUserPhotoErrorMessage() {
        viewModel.userPreferencesErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("UserDetailsFragment:onCreateView:updateUserPhoto", it) }
    }

    private fun handleUpdateUserPreferences() {
        viewModel.userPreferencesErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.d("UserDetailsFragment:onCreateView:updateUserPreferences", it) }
    }

    private fun String?.compareChange(prevValue: String?) =
        if (this == prevValue || this.isNullOrBlank()) null
        else {
            sendChangedAttributes = true
            this
        }

    companion object {
        const val IMAGE_QUALITY = 100
        const val MAX_SHOE_SIZE = 45
        const val MIN_SHOE_SIZE = 35
    }
}
