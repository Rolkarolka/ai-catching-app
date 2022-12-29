package edu.pw.aicatching.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.pw.aicatching.models.Cloth
import edu.pw.aicatching.models.ClothAttributes
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.models.User
import edu.pw.aicatching.models.UserPreferences
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AICatchingApiService {

    @GET("user/inspiration")
    fun getInspiration(): Call<Map<String, String>>

    @Multipart
    @POST("user/preferences/update_photo")
    fun updateUserPhoto(@Part photo: MultipartBody.Part): Call<UserPreferences>

    @POST("user/preferences/update")
    fun updateUserPreferences(@Body new_preferences: UserPreferences): Call<UserPreferences>

    @PUT("user/login_session")
    fun putLogIn(@Body credentials: Credentials): Call<User>

    @DELETE("user/logout_session")
    fun deleteSession(): Call<Void>

    @DELETE("user/delete")
    fun deleteUser(): Call<Void>

    @GET("garment/wardrobe")
    fun getWardrobe(): Call<List<Cloth>>

    @GET("garment/wardrobe")
    fun getOutfit(): Call<List<Cloth>>

    @GET("garment/attributes")
    fun getGarmentAttributes(@Query("garment_id") garmentID: Int): Call<ClothAttributes>

    @GET("garment/available_attributes")
    fun getAttributesValue(): Call<Map<String, List<String>>>

    @Multipart
    @POST("garment/create")
    fun postGarment(@Part photo: MultipartBody.Part): Call<Cloth>

    @PUT("garment/edit")
    fun putEditAttributes(@Query("garment_id") garmentID: Int, @Body attributes: ClothAttributes): Call<Cloth>

    @DELETE("garment/delete")
    fun deleteGarment(@Query("garment_id") garmentID: Int)

    @GET("garment/attributes")
    fun getGarmentAttributes(@Query("garment_id") garmentID: Int): Call<ClothAttributes>

    @GET("garment/available_attributes")
    fun getAttributesValue(): Call<Map<String, List<String>>>

    @Multipart
    @POST("garment/create")
    fun postGarment(@Part photo: MultipartBody.Part): Call<Cloth>

    @PUT("garment/edit")
    fun putEditAttributes(@Query("garment_id") garmentID: Int, @Body attributes: ClothAttributes): Call<Map<String, Int>>

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
