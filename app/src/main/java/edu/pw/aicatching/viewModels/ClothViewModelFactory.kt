package edu.pw.aicatching.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pw.aicatching.repositories.MainRepository

class ClothViewModelFactory constructor(private val repository: MainRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ClothViewModel::class.java)) {
            ClothViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
