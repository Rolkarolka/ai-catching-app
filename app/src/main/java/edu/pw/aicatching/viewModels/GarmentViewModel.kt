package edu.pw.aicatching.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Garment
import edu.pw.aicatching.models.GarmentAttributes
import edu.pw.aicatching.network.AICatchingApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GarmentViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    val mainGarment = MutableLiveData<Garment>()
    val mainGarmentErrorMessage = MutableLiveData<String>()

    val mainGarmentAttributes = MutableLiveData<GarmentAttributes>()
    val mainGarmentAttributesErrorMessage = MutableLiveData<String>()

    val outfitList = MutableLiveData<List<Garment>>()
    val outfitErrorMessage = MutableLiveData<String>()

    val availableAttributesValues = MutableLiveData<Map<String, List<String>>>()
    val availableAttributesValuesErrorMessage = MutableLiveData<String>()

    val wardrobeList = MutableLiveData<List<Garment>>()
    val wardrobeErrorMessage = MutableLiveData<String>()

    val deleteErrorMessage = MutableLiveData<String>()

    fun getOutfit(garmentID: Int) {
        val response = service.getOutfit(garmentID)
        response.enqueue(object : Callback<List<Garment>> {
            override fun onResponse(call: Call<List<Garment>>, response: Response<List<Garment>>) {
                outfitList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Garment>>, t: Throwable) {
                outfitErrorMessage.postValue(t.message)
            }
        })
    }

    fun getWardrobe() {
        val response = service.getWardrobe()
        response.enqueue(object : Callback<List<Garment>> {
            override fun onResponse(call: Call<List<Garment>>, response: Response<List<Garment>>) {
                wardrobeList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<Garment>>, t: Throwable) {
                wardrobeErrorMessage.postValue(t.message)
            }
        })
    }

    fun createGarment(image: ByteArray?) {
        val reqFile: RequestBody? = image?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        val body = reqFile?.let { MultipartBody.Part.createFormData("photo", "photo-name", it) }
        val response = body?.let { service.postGarment(it) }
        response?.enqueue(object : Callback<Garment> {
            override fun onResponse(call: Call<Garment>, response: Response<Garment>) {
                mainGarment.postValue(response.body())
            }

            override fun onFailure(call: Call<Garment>, t: Throwable) {
                mainGarmentErrorMessage.postValue(t.message)
            }
        })
    }

    fun getValuesOfGarmentAttributes() {
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

    fun updateGarmentAttributes(garmentID: Int, updatedGarmentAttributes: GarmentAttributes) {
        val response = service.putEditAttributes(garmentID, updatedGarmentAttributes)
        response.enqueue(object : Callback<GarmentAttributes> {
            override fun onResponse(call: Call<GarmentAttributes>, response: Response<GarmentAttributes>) {
                mainGarmentAttributes.postValue(response.body())
            }
            override fun onFailure(call: Call<GarmentAttributes>, t: Throwable) {
                mainGarmentAttributesErrorMessage.postValue(t.message)
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

    fun getAttributes(garmentID: Int) {
        val response = service.getGarmentAttributes(garmentID)
        response.enqueue(object : Callback<GarmentAttributes> {
            override fun onResponse(call: Call<GarmentAttributes>, response: Response<GarmentAttributes>) {
                mainGarmentAttributes.postValue(response.body())
            }
            override fun onFailure(call: Call<GarmentAttributes>, t: Throwable) {
                mainGarmentAttributesErrorMessage.postValue(t.message)
            }
        })
    }
}
