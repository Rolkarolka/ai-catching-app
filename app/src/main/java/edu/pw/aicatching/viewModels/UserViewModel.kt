package edu.pw.aicatching.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.models.UserPreferences
import edu.pw.aicatching.network.AICatchingApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    var user: MutableLiveData<User?> = MutableLiveData()
    var userErrorMessage = MutableLiveData<String>()

    var userPreferences: MutableLiveData<UserPreferences> = MutableLiveData()
    var userPreferencesErrorMessage = MutableLiveData<String>()

    var inspiration: MutableLiveData<Map<String, String>> = MutableLiveData()
    var inspirationErrorMessage = MutableLiveData<String>()

    var loggingErrorMessage = MutableLiveData<String>()

    fun logIn(credentials: Credentials) {
        val response = service.putLogIn(credentials)
        response.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                userErrorMessage.postValue(t.message)
                user.postValue(null)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                } else {
                    userErrorMessage.postValue("${response.code()} ${response.message()}")
                    user.postValue(null)
                }
            }
        })
    }

    fun deleteUser() {
        val response = service.deleteUser()
        response.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    user.postValue(null)
                    userPreferences = MutableLiveData()
                } else {
                    loggingErrorMessage.postValue("${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                loggingErrorMessage.postValue(t.message)
            }
        })
    }

    fun logOut() {
        val response = service.deleteSession()
        response.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    user.postValue(null)
                } else {
                    loggingErrorMessage.postValue("${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                loggingErrorMessage.postValue(t.message)
            }
        })
    }

    fun updateUserPhoto(image: ByteArray) {
        val reqFile: RequestBody = image.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        val body = reqFile.let { MultipartBody.Part.createFormData("photo", "photo-name", it) }
        val response = body.let { service.updateUserPhoto(it) }
        response.enqueue(object : Callback<UserPreferences> {
            override fun onResponse(call: Call<UserPreferences>, response: Response<UserPreferences>) {
                if (response.isSuccessful) {
                    userPreferences.postValue(response.body())
                } else {
                    userPreferencesErrorMessage.postValue("${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserPreferences>, t: Throwable) {
                userPreferencesErrorMessage.postValue(t.message)
            }
        })
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        val response = service.updateUserPreferences(preferences)
        response.enqueue(object : Callback<UserPreferences> {
            override fun onFailure(call: Call<UserPreferences>, t: Throwable) {
                userPreferencesErrorMessage.postValue(t.message)
            }

            override fun onResponse(call: Call<UserPreferences>, response: Response<UserPreferences>) {
                if (response.isSuccessful) {
                    userPreferences.postValue(response.body())
                } else {
                    userPreferencesErrorMessage.postValue("${response.code()} ${response.message()}")
                }
            }
        })
    }

    fun getInspiration() {
        val response = service.getInspiration()
        response.enqueue(object : Callback<Map<String, String>> {
            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                inspirationErrorMessage.postValue(t.message)
            }

            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    inspiration.postValue(response.body())
                } else {
                    inspirationErrorMessage.postValue("${response.code()} ${response.message()}")
                }
            }
        })
    }
}
