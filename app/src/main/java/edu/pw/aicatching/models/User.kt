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
    val garmentSize: GarmentSize? = null,
    @Json(name = "favorite_color")
    val favouriteColor: Color? = null
)

class Credentials(
    @Json(name = "username")
    val email: String,
    @Json(name = "password")
    val token: String?
)

enum class GarmentSize {
    XS, S, M, L, XL, XXL;
    companion object {
        infix fun from(value: String?): GarmentSize? =
            if (value != null)
                values().firstOrNull { it.name == value }
            else
                null
    }
}

enum class Color(val hexValue: String) {
    @Json(name = "aqua") AQUA("#00FFFF"),
    @Json(name = "black") BLACK("#000000"),
    @Json(name = "blue") BLUE("#0000FF"),
    @Json(name = "fuchsia") FUCHSIA("#FF00FF"),
    @Json(name = "green") GREEN("#008000"),
    @Json(name = "grey") GREY("#808080"),
    @Json(name = "lime") LIME("#00FF00"),
    @Json(name = "maroon") MAROON("#800000"),
    @Json(name = "navy") NAVY("#000080"),
    @Json(name = "olive") OLIVE("#808000"),
    @Json(name = "purple") PURPLE("#800080"),
    @Json(name = "red") RED("#FF0000"),
    @Json(name = "silver") SILVER("#C0C0C0"),
    @Json(name = "teal") TEAL("#008080"),
    @Json(name = "white") WHITE("#FFFFFF"),
    @Json(name = "yellow") YELLOW("#FFFF00");

    companion object {
        private const val INT_BASE = 16
        private const val RGB_AS_HEX_LENGTH = 6
        private const val HEX_START = 0
        private const val HEX_RED_END = 2
        private const val HEX_GREEN_END = 4
        private const val HEX_BLUE_END = 6
        private const val RGB_PLACES = 3

        private fun String.hexToIntArray(): IntArray {
            val code = this.removePrefix("#").takeLast(RGB_AS_HEX_LENGTH)
            val red = code.substring(HEX_START, HEX_RED_END)
            val green = code.substring(HEX_RED_END, HEX_GREEN_END)
            val blue = code.substring(HEX_GREEN_END, HEX_BLUE_END)
            return intArrayOf(red.toInt(INT_BASE), green.toInt(INT_BASE), blue.toInt(INT_BASE))
        }

        private fun countDistance(c1: IntArray, c2: IntArray): Double {
            return sqrt(
                (c2[0] - c1[0]).toDouble().pow(2) +
                    (c2[1] - c1[1]).toDouble().pow(2) +
                    (c2[2] - c1[2]).toDouble().pow(2)
            )
        }

        infix fun from(argbValue: IntArray?): Color? =
            if (argbValue != null) {
                var closest = AQUA
                for (color in values()) {
                    if (countDistance(
                            argbValue.takeLast(RGB_PLACES).toIntArray(),
                            closest.hexValue.hexToIntArray()
                        ) > countDistance(
                                argbValue.takeLast(RGB_PLACES).toIntArray(),
                                color.hexValue.hexToIntArray()
                            )
                    ) {
                        closest = color
                    }
                }

                closest
            } else null
    }
}
