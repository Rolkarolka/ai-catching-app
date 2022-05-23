package edu.pw.aicatching.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pw.aicatching.network.MainRepository

class WardrobeViewModelFactory constructor(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WardrobeViewModel::class.java)) {
            WardrobeViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
