package edu.pw.aicatching.models

import com.squareup.moshi.Json
import kotlin.reflect.full.memberProperties

data class Cloth(
    @Json(name = "garment_id")
    val garmentID: Int,
    @Json(name = "photo_url")
    val imgSrcUrl: String,
    val part: String?
)

data class ClothAttributes(
    val texture: String? = null,
    @Json(name = "sleeve_length")
    val sleeveLength: String? = null,
    @Json(name = "garment_length")
    val garmentLength: String? = null,
    @Json(name = "neckline_type")
    val necklineType: String? = null,
    val fabric: String? = null,
    val color: String? = null,
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
