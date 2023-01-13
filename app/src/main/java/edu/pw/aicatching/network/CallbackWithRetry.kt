package edu.pw.aicatching.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback

abstract class CallbackWithRetry<T> : Callback<T> {
    private var retryCount = 0

    override fun onFailure(call: Call<T>, t: Throwable) {
        t.message?.let { Log.e("CallbackWithRetry:onFailure", it) }
        retryCall(call)
    }

    fun retryCall(call: Call<T>) {
        if (retryCount++ < TOTAL_RETRIES) {
            Log.i("CallbackWithRetry:onFailure", "Retry $retryCount out of $TOTAL_RETRIES")
            Thread.sleep(WAIT_UNTIL_RETRY_MILLIS.toLong()) // TODO?
            retry(call)
        }
    }

    private fun retry(call: Call<T>) {
        call.clone().enqueue(this)
    }

    companion object {
        private const val TOTAL_RETRIES = 10
        private const val WAIT_UNTIL_RETRY_MILLIS = 500
    }
}
