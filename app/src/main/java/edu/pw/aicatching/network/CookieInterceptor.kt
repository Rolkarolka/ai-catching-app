package edu.pw.aicatching.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class CookieInterceptor : Interceptor {
    @Volatile
    var cookie: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        cookie?.let {
            request = request.newBuilder()
                .header("Cookie", it)
                .build()
        }

        val response = chain.proceed(request)
        val isCookieHeader = response.headers().get("set-cookie") != null
        if (isCookieHeader) {
            val cookieValue = response.headers("set-cookie")[0]
            val cookieElements = cookieValue.split(";").map { str -> str.split("=")}.associate {
                it[0] to it[1]
            }

            cookie = if (cookieElements["session_token"].equals("\"\"")) null else cookieValue
        }
        return response
    }
}
