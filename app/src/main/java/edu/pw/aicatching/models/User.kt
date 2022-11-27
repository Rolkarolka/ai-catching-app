package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class User(
    val name: String,
    val surname: String,
    val email: String
)

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)
