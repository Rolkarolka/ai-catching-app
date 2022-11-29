package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class User(
    val name: String,
    val surname: String,
    val email: String,
    val photoUrl: String?,
    val preferences: UserPreferences?
)

data class UserPreferences(
    val shoeSize: String? = null,
    val clothSize: ClothSize? = ClothSize.UNKNOWN,
    val favouriteColor: Int? = null
)

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)


enum class ClothSize{
    UNKNOWN, XS, S, M, L, XL, XXL
}

