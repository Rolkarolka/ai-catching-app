package edu.pw.aicatching.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.repositories.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val mainCloth = MutableLiveData<Cloth>()
    val outfitList = MutableLiveData<List<Cloth>>()
    val outfitErrorMessage = MutableLiveData<String>()

    val wardrobeList = MutableLiveData<List<Cloth>>()
    val wardrobeErrorMessage = MutableLiveData<String>()

    fun getOutfit() {
        val response = repository.getOutfit()
        response.enqueue(object : Callback<List<Cloth>> {
            override fun onResponse(call: Call<List<Cloth>>, response: Response<List<Cloth>>) {
                outfitList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Cloth>>, t: Throwable) {
                outfitErrorMessage.postValue(t.message)
            }
        })
    }

    fun getWardrobe() {
        val response = repository.getWardrobe()
        response.enqueue(object : Callback<List<Cloth>> {
            override fun onResponse(call: Call<List<Cloth>>, response: Response<List<Cloth>>) {
                wardrobeList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Cloth>>, t: Throwable) {
                wardrobeErrorMessage.postValue(t.message)
            }
        })
    }



}
