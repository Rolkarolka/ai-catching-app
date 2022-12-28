package edu.pw.aicatching.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.models.UserPreferences
import edu.pw.aicatching.network.AICatchingApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    var userLiveData: MutableLiveData<User?> = MutableLiveData()
    val userPreferencesLiveData: MutableLiveData<UserPreferences> = MutableLiveData()
    var userErrorMessage = MutableLiveData<String>()
    var loggingMessage = MutableLiveData<Map<String, String>?>()
    var loggingErrorMessage = MutableLiveData<String>()

    var inspirationLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()
    var inspirationErrorMessage = MutableLiveData<String>()

    fun logIn(credentials: Credentials) {
        val response = service.putLogIn(credentials)
        response.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                userErrorMessage.postValue(t.message)
                userLiveData.postValue(null)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    userLiveData.postValue(response.body())
                } else {
                    userLiveData.postValue(null)
                }
            }
        })
    }

    fun deleteUser() {
        val response = service.deleteUser()
        userLiveData.postValue(null)
        response.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) { }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                loggingErrorMessage.postValue(t.message)
            }
        })
    }

    fun logOut() {
        val response = service.deleteSession()
        userLiveData.postValue(null)
        response.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) { }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                loggingErrorMessage.postValue(t.message)
            }
        })
    }

    fun updateUserPhoto(uri: Uri) {
        print(uri)
//        val response = service.updateUserPhoto()
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        val response = service.updateUserPreferences(preferences)
        response.enqueue(object : Callback<UserPreferences> {
            override fun onFailure(call: Call<UserPreferences>, t: Throwable) {
                userErrorMessage.postValue(t.message)
            }

            override fun onResponse(call: Call<UserPreferences>, response: Response<UserPreferences>) {
                if (response.isSuccessful) {
                    userPreferencesLiveData.postValue(response.body())
                } else {
                    userPreferencesLiveData.postValue(null)
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
                    inspirationLiveData.postValue(response.body())
                }
            }
        })
    }
}
