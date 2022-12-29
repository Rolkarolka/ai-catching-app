package edu.pw.aicatching.models

import com.squareup.moshi.Json
import kotlin.math.pow
import kotlin.math.sqrt

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
    @Json(name = "garment_size")
    val clothSize: ClothSize? = null,
    @Json(name="favorite_color")
    val favouriteColor: Color? = null
) {
//    constructor(photoUrl: String?, shoeSize: String?, clothSize: String?, favouriteColor: String?) :this(photoUrl, shoeSize, ClothSize.from(clothSize), Color.from(favouriteColor))
}

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)

enum class ClothSize {
    XS, S, M, L, XL, XXL;
    companion object {
        infix fun from(value: String?): ClothSize? = if (value != null) ClothSize.values().firstOrNull { it.name == value } else null
    }
}

enum class Color(val hexValue: String) {
    AQUA("#00FFFF"),
    BLACK("#000000"),
    BLUE( "#0000FF"),
    FUCHSIA( "#FF00FF"),
    GREEN( "#008000"),
    GREY( "#808080"),
    LIME( "#00FF00"),
    MAROON( "#800000"),
    NAVY( "#000080"),
    OLIVE( "#808000"),
    PURPLE( "#800080"),
    RED( "#FF0000"),
    SILVER( "#C0C0C0"),
    TEAL( "#008080"),
    WHITE( "#FFFFFF"),
    YELLOW( "#FFFF00");


    companion object {
        private fun String.hexToIntArray(): IntArray {
            val code = this.removePrefix("#").takeLast(6)
            val red = code.substring(0,2)
            val green = code.substring(2,4)
            val blue = code.substring(4,6)
            return intArrayOf(red.toInt(16), green.toInt(16), blue.toInt(16))
        }

        private fun countDistance(c1: IntArray, c2: IntArray): Double {
            return sqrt((c2[0]-c1[0]).toDouble().pow(2) + (c2[1]-c1[1]).toDouble().pow(2) + (c2[2]-c1[2]).toDouble().pow(2))
        }

        infix fun from(argbValue: IntArray?): Color? =
            if (argbValue != null) {
                var closest = AQUA
                for (color in values()) {
                    if (countDistance(argbValue.takeLast(3).toIntArray(), closest.hexValue.hexToIntArray()) > countDistance(argbValue.takeLast(3).toIntArray(), color.hexValue.hexToIntArray())) {
                        closest = color
                    }
                }

                closest
            } else null
        }

}
