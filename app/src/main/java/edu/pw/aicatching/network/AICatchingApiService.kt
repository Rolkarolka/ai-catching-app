package edu.pw.aicatching.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.pw.aicatching.models.Cloth
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface AICatchingApiService {

    @GET("user/wardrobe")
    fun getWardrobe(): Call<List<Cloth>>

    @GET("user/wardrobe")
    fun getOutfit(): Call<List<Cloth>>

    companion object {
        private const val BASE_URL = "https://berrygood.hopto.org/api/v1/"
        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        var aiCatchingApiService: AICatchingApiService? = null

        fun getInstance(): AICatchingApiService {
            if (aiCatchingApiService == null) {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(BASE_URL)
                    .build()
                aiCatchingApiService = retrofit.create(AICatchingApiService::class.java)
            }
            return aiCatchingApiService!! // TODO without !!
        }
    }
}
