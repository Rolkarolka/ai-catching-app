package edu.pw.aicatching.models

import com.squareup.moshi.Json

data class Cloth(
    val id: Int,
    @Json(name = "img_url")
    val imgSrcUrl: String
)
