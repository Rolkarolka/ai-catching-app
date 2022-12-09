package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class User(
    @Json(name = "user_id")
    val userID: Int,
    @Json(name = "first_name")
    val name: String,
    val surname: String,
    val email: String,
    val preferences: UserPreferences? = null
)

data class UserPreferences(
    val photoUrl: String? = null,
    val shoeSize: String? = null,
    val clothSize: ClothSize? = null,
    val favouriteColor: Int? = null
)

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)

enum class ClothSize {
    XS, S, M, L, XL, XXL
}
