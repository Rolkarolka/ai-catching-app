package edu.pw.aicatching.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.network.AICatchingApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    val mainCloth = MutableLiveData<Cloth>()
    val mainClothErrorMessage = MutableLiveData<String>()

    val mainClothAttributes = MutableLiveData<ClothAttributes>()
    val mainClothAttributesErrorMessage = MutableLiveData<String>()

    val outfitList = MutableLiveData<List<Cloth>>()
    val outfitErrorMessage = MutableLiveData<String>()

    val availableAttributesValues = MutableLiveData<Map<String, List<String>>>()
    val availableAttributesValuesErrorMessage = MutableLiveData<String>()

    val wardrobeList = MutableLiveData<List<Cloth>>()
    val wardrobeErrorMessage = MutableLiveData<String>()

    val deleteErrorMessage = MutableLiveData<String>()

    fun getOutfit(garmentID: Int) {
        val response = service.getOutfit(garmentID)
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

    fun createGarment(image: ByteArray?) {
        val reqFile: RequestBody? = image?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        val body = reqFile?.let { MultipartBody.Part.createFormData("photo", "photo-name", it) }
        val response = body?.let { service.postGarment(it) }
        response?.enqueue(object : Callback<Cloth> {
            override fun onResponse(call: Call<Cloth>, response: Response<Cloth>) {
                mainCloth.postValue(response.body())
            }

            override fun onFailure(call: Call<Cloth>, t: Throwable) {
                mainClothErrorMessage.postValue(t.message)
            }
        })
    }

    fun getValuesOfClothAttributes() {
        val response = service.getAttributesValue()
        response.enqueue(object : Callback<Map<String, List<String>>> {
            override fun onResponse(
                call: Call<Map<String, List<String>>>,
                response: Response<Map<String, List<String>>>
            ) {
                availableAttributesValues.postValue(response.body())
            }
            override fun onFailure(call: Call<Map<String, List<String>>>, t: Throwable) {
                availableAttributesValuesErrorMessage.postValue(t.message)
            }
        })
    }

    fun updateClothAttributes(garmentID: Int, updatedClothesAttributes: ClothAttributes) {
        val response = service.putEditAttributes(garmentID, updatedClothesAttributes)
        response.enqueue(object : Callback<ClothAttributes> {
            override fun onResponse(call: Call<ClothAttributes>, response: Response<ClothAttributes>) {
                mainClothAttributes.postValue(response.body())
            }
            override fun onFailure(call: Call<ClothAttributes>, t: Throwable) {
                mainClothAttributesErrorMessage.postValue(t.message)
            }
        })
    }

    fun deleteGarment(garmentID: Int) {
        val response = service.deleteGarment(garmentID)
        response.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) { }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                deleteErrorMessage.postValue(t.message)
            }
        })
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
