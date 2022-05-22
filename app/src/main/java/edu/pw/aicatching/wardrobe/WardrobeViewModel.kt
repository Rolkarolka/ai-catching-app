package edu.pw.aicatching.wardrobe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pw.aicatching.network.AICatchingApi
import java.lang.Exception
import kotlinx.coroutines.launch

class WardrobeViewModel: ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    init {
        getClothInformation()
    }

    private fun getClothInformation() {
        viewModelScope.launch {
            try {
                val listResult = AICatchingApi.retrofitService.getPhotoUrls()
                _status.value = "Success: ${listResult.size} Mars photos retrieved"
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }

        }
    }

}
