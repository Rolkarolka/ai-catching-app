package edu.pw.aicatching.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.pw.aicatching.models.Garment
import edu.pw.aicatching.models.GarmentAttributes
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.models.UserPreferences
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface AICatchingApiService {

    @GET("user/inspiration")
    fun getInspiration(): Call<Map<String, String>>

    @Multipart
    @POST("user/preferences/update_photo")
    fun updateUserPhoto(@Part photo: MultipartBody.Part): Call<UserPreferences>

    @POST("user/preferences/update")
    fun updateUserPreferences(@Body newPreferences: UserPreferences): Call<UserPreferences>

    @PUT("user/login_session")
    fun putLogIn(@Body credentials: Credentials): Call<User>

    @DELETE("user/logout_session")
    fun deleteSession(): Call<Void>

    @DELETE("user/delete")
    fun deleteUser(): Call<Void>

    @GET("garment/wardrobe")
    fun getWardrobe(): Call<List<Garment>>

    @GET("garment/outfit")
    fun getOutfit(@Query("garment_id") garmentID: Int): Call<List<Garment>>

    @GET("garment/attributes")
    fun getGarmentAttributes(@Query("garment_id") garmentID: Int): Call<GarmentAttributes>

    @GET("garment/available_attributes")
    fun getAttributesValue(): Call<Map<String, List<String>>>

    @Multipart
    @POST("garment/create")
    fun postGarment(@Part photo: MultipartBody.Part): Call<Garment>

    @DELETE("garment/delete")
    fun deleteGarment(@Query("garment_id") garmentID: Int): Call<Void>

    @PUT("garment/edit")
    fun putEditAttributes(
        @Query("garment_id") garmentID: Int,
        @Body newAttributes: GarmentAttributes
    ): Call<GarmentAttributes>

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
        val isCookieHeader = response.headers().get("set-cookie") != null
        if (cookie == null && isCookieHeader) {
            cookie = response.headers("set-cookie")[0]
        }
        return response
    }
}
