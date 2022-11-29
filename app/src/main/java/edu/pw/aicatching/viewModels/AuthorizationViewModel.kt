package edu.pw.aicatching.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.network.AICatchingApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizationViewModel : ViewModel() {
    private val service = AICatchingApiService.getInstance()

    var userLiveData: MutableLiveData<User?> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    fun logIn(credentials: Credentials) {
        val response = service.postLogIn(credentials)
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
}
