package edu.pw.aicatching.userDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
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
        val shoeSizesArray = (35..45).toList().map { it.toString() }
        shoeSizeSpinner.adapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, shoeSizesArray)

    }
}
