package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class Cloth(
    val id: String,
    @Json(name = "img_src")
    val imgSrcUrl: String
)
