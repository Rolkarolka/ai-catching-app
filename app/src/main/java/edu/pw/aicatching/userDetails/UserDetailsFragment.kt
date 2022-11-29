package edu.pw.aicatching.userDetails

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager
import edu.pw.aicatching.R
import edu.pw.aicatching.viewModels.AuthorizationViewModel
import edu.pw.aicatching.models.UserPreferences
import kotlinx.android.synthetic.main.fragment_user_details.*

class UserDetailsFragment : Fragment() {
    private val viewModel: AuthorizationViewModel by activityViewModels()
    private lateinit var colorPickerManager: ColorPickerPreferenceManager
    private val pickMediaResult = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Log.d("UserDetailsFragment:PhotoPicker", "Selected URI: $uri")
            currentUserAvatar.setImageURI(uri)
            // TODO send image to server
            viewModel.userLiveData.value = viewModel.userLiveData.value?.copy(photoUrl = uri.toString())
        } else {
            Log.d("UserDetailsFragment:PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        colorPickerManager = ColorPickerPreferenceManager.getInstance(this.context)
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clothSizesArray = arrayListOf("", "XS", "S", "M", "L", "XL", "XXL")
        setClothSpinner(clothSizesArray)
        val shoeSizesArray = listOf("") + (35..45).toList().map { it.toString() }
        setShoeSpinner(shoeSizesArray)
        setColorPicker()
        setAvatar()

        logOutButton.setOnClickListener {
            // TODO call server
            viewModel.userLiveData = MutableLiveData()
            this.view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.authorizationFragment) }
        }

        deleteAccountButton.setOnClickListener {
            // TODO call server
            viewModel.userLiveData = MutableLiveData()
            this.view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.authorizationFragment) }
        }
    }

    private fun setClothSpinner(clothSizesArray: ArrayList<String>) {
        clothSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, clothSizesArray)

        viewModel.userLiveData.value?.preferences
            ?.let { clothSizesArray.indexOf(it.clothSize) }
            ?.let { clothSizeSpinner.setSelection(it) }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (viewModel.userLiveData.value?.preferences == null) {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(preferences = UserPreferences(clothSize = clothSizesArray[position]))
                } else {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(
                            preferences = viewModel.userLiveData.value
                                ?.preferences?.copy(clothSize = clothSizesArray[position])
                        )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { clothSizeSpinner.onItemSelectedListener = it }
    }

    private fun setShoeSpinner(shoeSizesArray: List<String>) {
        shoeSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, shoeSizesArray)
        viewModel.userLiveData.value?.preferences
            ?.let { shoeSizesArray.indexOf(it.shoeSize) }
            ?.let { shoeSizeSpinner.setSelection(it) }

        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (viewModel.userLiveData.value?.preferences == null) {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(preferences = UserPreferences(shoeSize = shoeSizesArray[position]))
                } else {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(
                            preferences = viewModel.userLiveData.value
                                ?.preferences?.copy(shoeSize = shoeSizesArray[position])
                        )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }.also { shoeSizeSpinner.onItemSelectedListener = it }
    }

    private fun setColorPicker() {
        favColorPickerView.preferenceName = "FavColorPicker"
        viewModel.userLiveData.value?.preferences?.let {
            it.favouriteColor?.let { favColor ->
                colorPickerManager.setColor("FavColorPicker", favColor)
            }
        }

        favColorPickerView.setColorListener(
            ColorListener { color, _ ->
                favouriteColorView.backgroundTintList = ColorStateList.valueOf(color)
                if (viewModel.userLiveData.value?.preferences == null) {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(preferences = UserPreferences(favouriteColor = color))
                } else {
                    viewModel.userLiveData.value = viewModel.userLiveData
                        .value?.copy(
                            preferences = viewModel.userLiveData.value
                                ?.preferences?.copy(favouriteColor = color)
                        )
                }
            }
        )
    }

    private fun setAvatar() {
        viewModel.userLiveData.value?.photoUrl?.let { photo ->
            currentUserAvatar.setImageURI(Uri.parse(photo))
        }
        changeUserPhotoButton.setOnClickListener {
            pickMediaResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}
