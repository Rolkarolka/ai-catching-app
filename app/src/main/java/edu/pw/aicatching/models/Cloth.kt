package edu.pw.aicatching.models

import com.squareup.moshi.Json
import kotlin.reflect.full.memberProperties

data class Cloth(
    @Json(name = "photo_url")
    val imgSrcUrl: String,
    val part: String,
    val attributes: ClothAttributes? = null
)

data class ClothAttributes(
    val pattern: String? = null,
    val color: String? = null,
    val texture: String? = null,
    val sleeveLength: String? = null,
    val clothLength: String? = null,
    val necklineType: String? = null,
    val fabric: String? = null
)

fun ClothAttributes.asMap(): Map<String, String> {
    val properties = ClothAttributes::class.memberProperties.associateBy { it ->
        it.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
    return properties.keys.associate {
        val value = properties[it]?.get(this)
        if (value == null) it to "" else it to value.toString()
    }
}
