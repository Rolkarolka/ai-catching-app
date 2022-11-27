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
    val shoeSize: String? = null, // TODO enum?
    val clothSize: String? = null, // TODO enum?
    val favouriteColor: Int? = null
)

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)
