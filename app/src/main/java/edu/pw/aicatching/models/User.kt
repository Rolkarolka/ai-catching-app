package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class User(
    @Json(name = "user_id")
    val userID: Int,
    @Json(name = "first_name")
    val name: String,
    val surname: String,
    val email: String,
    @Json(name = "preference")
    val preferences: UserPreferences? = null
)

data class UserPreferences(
    @Json(name = "photo_path")
    val photoUrl: String? = null,
    @Json(name = "shoe_size")
    val shoeSize: String? = null,
    @Json(name = "cloth_size")
    val clothSize: ClothSize? = null,
    val favouriteColor: Int? = null  // TODO fav color from api
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
