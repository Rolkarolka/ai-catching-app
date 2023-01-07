package edu.pw.aicatching.models

import com.squareup.moshi.Json
import kotlin.reflect.full.memberProperties

data class Garment(
    @Json(name = "garment_id")
    val garmentID: Int,
    @Json(name = "photo_url")
    val imgSrcUrl: String,
    val part: String?
)

data class GarmentAttributes(
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

fun GarmentAttributes.asMap(): Map<String, String> {
    val properties = GarmentAttributes::class.memberProperties.associateBy {
        it.name.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }
    }
    return properties.keys.associate {
        val value = properties[it]?.get(this)
        if (value == null) it to "" else it to value.toString()
    }
}
