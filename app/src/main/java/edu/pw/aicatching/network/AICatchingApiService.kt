package edu.pw.aicatching.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "http://127.0.0.1"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AICatchingApiService {

    @GET("photos")
    suspend fun getPhotoUrls(): List<Cloth>
}

object AICatchingApi {
    val retrofitService: AICatchingApiService by lazy {
        retrofit.create(AICatchingApiService::class.java)
    }
}

