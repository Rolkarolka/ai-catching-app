package edu.pw.aicatching.authorization

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

    fun getCreateNewUserObserver(): MutableLiveData<User?> {
        return userLiveData
    }


    fun logIn(credentials: Credentials) {
        val call = service.postLogIn(credentials)
        call.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                userLiveData.postValue(null)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    userLiveData.postValue(response.body())
                } else {
                    userLiveData.postValue(null)
                }
            }
        })
    }


}
