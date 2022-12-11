package edu.pw.aicatching.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AICatchingApiService {

    @GET("garment/wardrobe")
    fun getWardrobe(): Call<List<Cloth>>

    @GET("garment/wardrobe")
    fun getOutfit(): Call<List<Cloth>>

    @PUT("user/login_session")
    fun putLogIn(@Body credentials: Credentials): Call<User>

    @DELETE("user/logout_session")
    fun deleteSession()

    @DELETE("user/delete")
    fun deleteUser(): Call<Response>

//    @GET("garment/photo")
//    fun getGarmentPhoto(): Call<>

    @DELETE("garment/delete")
    fun deleteGarment(@Query("garment_id") garmentID: Int)

    @GET("garment/attributes")
    fun getGarmentAttributes(@Query("garment_id") garmentID: Int): Call<ClothAttributes>

    @GET("garment/available_attributes")
    fun getAttributesValue(): Call<Map<String, List<String>>>

    @Multipart
    @POST("garment/create")
    fun postGarment(@Part photo: MultipartBody.Part): Call<Cloth>

    companion object {
        private const val BASE_URL = "https://berrygood.hopto.org/api/v1/"
        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        private var aiCatchingApiService: AICatchingApiService? = null
        private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(CookieInterceptor())
            .build()

        fun getInstance(): AICatchingApiService {
            if (aiCatchingApiService == null) {
                val retrofit = Retrofit.Builder()
                    .callFactory(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(BASE_URL)
                    .build()
                aiCatchingApiService = retrofit.create(AICatchingApiService::class.java)
            }
            return aiCatchingApiService!!
        }
    }
}

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
        if (cookie == null) {
            cookie = response.headers("set-cookie")[0]
        }
        return response
    }
}
