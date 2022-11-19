package edu.pw.aicatching.clothMatching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pw.aicatching.repositories.MainRepository

class OutfitViewModelFactory constructor(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OutfitViewModel::class.java)) {
            OutfitViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
