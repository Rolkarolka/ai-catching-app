package edu.pw.aicatching.userDetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager
import edu.pw.aicatching.R
import kotlinx.android.synthetic.main.fragment_user_details.*


class UserDetailsFragment: Fragment() {
    private val pickMediaResult = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("UserDetailsFragment:PhotoPicker", "Selected URI: $uri")
            currentUserAvatar.setImageURI(uri)
            // TODO send image to server, and set in user object

        } else {
            Log.d("UserDetailsFragment:PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeUserPhotoButton.setOnClickListener {
            pickMediaResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        val clothSizesArray = arrayListOf("XS", "S", "M", "L", "XL", "XXL")
        clothSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, clothSizesArray)
        clothSizeSpinner.setSelection(clothSizesArray.indexOf("M")) // TODO from user
        val shoeSizesArray = (35..45).toList().map { it.toString() }
        shoeSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, shoeSizesArray)
        shoeSizeSpinner.setSelection(clothSizesArray.indexOf("37")) // TODO from user
        favColorPickerView.preferenceName = "FavColorPicker";
        val manager = ColorPickerPreferenceManager.getInstance(this.context)
        manager.setColor("FavColorPicker", -4391136) // TODO set fav user color
        favColorPickerView.setColorListener(ColorListener { color, _ ->
            favouriteColorView.backgroundTintList = ColorStateList.valueOf(color)
        })
    }
}

