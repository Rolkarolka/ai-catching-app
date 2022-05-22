package edu.pw.aicatching.network

import com.squareup.moshi.Json

data class Cloth(
    val id: String,
    @Json(name = "img_src")
    val imgSrcUrl: String
)

