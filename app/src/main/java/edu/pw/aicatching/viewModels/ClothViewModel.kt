package edu.pw.aicatching.viewModels

import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.network.AICatchingApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    val mainCloth = MutableLiveData<Cloth>()
    val mainClothAttributes = MutableLiveData<ClothAttributes>()
    val outfitList = MutableLiveData<List<Cloth>>()
    val outfitErrorMessage = MutableLiveData<String>()
    val availableAttributesValues = MutableLiveData<Map<String, List<String>>>()

    val wardrobeList = MutableLiveData<List<Cloth>>()
    val wardrobeErrorMessage = MutableLiveData<String>()
    val mainClothAttributesErrorMessage = MutableLiveData<String>()
    val availableAttributesValuesErrorMessage = MutableLiveData<String>()

    fun getOutfit() {
        val response = service.getOutfit()
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
        val response = service.getWardrobe()
        response.enqueue(object : Callback<List<Cloth>> {
            override fun onResponse(call: Call<List<Cloth>>, response: Response<List<Cloth>>) {
                wardrobeList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Cloth>>, t: Throwable) {
                wardrobeErrorMessage.postValue(t.message)
            }
        })
    }

    fun sendPhoto(image: ImageProxy): Cloth? {
        // TODO send image to server
        return Cloth(imgSrcUrl = "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg", part = "cat", garmentID = 1111)
    }

    fun getValuesOfClothAttributes() {
        val response = service.getAttributesValue()
        response.enqueue(object : Callback<Map<String, List<String>>> {
            override fun onResponse(call: Call<Map<String, List<String>>>, response: Response<Map<String, List<String>>>) {
                availableAttributesValues.postValue(response.body())
            }
            override fun onFailure(call: Call<Map<String, List<String>>>, t: Throwable) {
                availableAttributesValuesErrorMessage.postValue(t.message)
            }
        })
    }

    fun updateClothAttributes() {
        // TODO
    }

    fun deleteCloth(cloth: Cloth) {
        // TODO
    }

    fun getAttributes(clothID: Int) {
        val response = service.getGarmentAttributes(clothID)
        response.enqueue(object : Callback<ClothAttributes> {
            override fun onResponse(call: Call<ClothAttributes>, response: Response<ClothAttributes>) {
                mainClothAttributes.postValue(response.body())
            }
            override fun onFailure(call: Call<ClothAttributes>, t: Throwable) {
                mainClothAttributesErrorMessage.postValue(t.message)
            }
        })
    }
}
