package edu.pw.aicatching.clothMatching

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.repositories.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutfitViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val outfitList = MutableLiveData<List<Cloth>>()
    val errorMessage = MutableLiveData<String>()

    fun getOutfit() {
        val response = repository.getWardrobe()
        response.enqueue(object : Callback<List<Cloth>> {
            override fun onResponse(call: Call<List<Cloth>>, response: Response<List<Cloth>>) {
                outfitList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Cloth>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
