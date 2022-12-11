package edu.pw.aicatching.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.network.AICatchingApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    var userLiveData: MutableLiveData<User?> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    fun logIn(credentials: Credentials) {
        val response = service.putLogIn(credentials)
        response.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                errorMessage.postValue(t.message)
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

    fun deleteUser() { // TODO
//        val response = service.deleteUser()
//        response.enqueue(object : Callback<Response> {
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                if (response.isSuccessful) {
//                    userLiveData = MutableLiveData()
//                } else {
//                    errorMessage.postValue(response.code().toString())
//                }
//            }
//        })
    }

    fun logOut() {} // TODO

    fun updateUserPhoto(uri: Uri) {} // TODO
    fun getInspiration(): String { // TODO
        return "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
    }

    fun updateUserPreferences() {} // TODO
}
