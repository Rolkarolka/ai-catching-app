package edu.pw.aicatching.wardrobe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.network.Cloth
import edu.pw.aicatching.network.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WardrobeViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val wardrobeList = MutableLiveData<List<Cloth>>()
    val errorMessage = MutableLiveData<String>()

    fun getWardrobe() {
        val response = repository.getWardrobe()
        response.enqueue(object : Callback<List<Cloth>> {
            override fun onResponse(call: Call<List<Cloth>>, response: Response<List<Cloth>>) {
                wardrobeList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Cloth>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
