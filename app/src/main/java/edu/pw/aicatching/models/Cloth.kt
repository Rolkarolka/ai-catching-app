package edu.pw.aicatching.models

import com.squareup.moshi.Json
import kotlin.reflect.full.memberProperties

data class Cloth(
    val id: Int,
    @Json(name = "img_url")
    val imgSrcUrl: String,
    val category: String?,
    val attributes: ClothAttributes?
)

data class ClothAttributes(
    val pattern: String?,
    val color: String?
)

fun ClothAttributes.asMap() : Map<String, String> {
    val properties= ClothAttributes::class.memberProperties.associateBy { it.name }
    return properties.keys.associate {
        val value = properties[it]?.get(this)
        if (value == null) it to "" else it to value.toString()
    }
}
