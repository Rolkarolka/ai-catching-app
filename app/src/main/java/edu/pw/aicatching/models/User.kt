package edu.pw.aicatching.models

import com.squareup.moshi.Json

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
    constructor(photoUrl: String?, shoeSize: String?, clothSize: String?, favouriteColor: String?) :this(photoUrl, shoeSize, ClothSize.from(clothSize), Color.from(favouriteColor))
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
        infix fun from(hexValue: String?): Color? = if (hexValue != null) Color.values().firstOrNull { it.hexValue == hexValue } else null // TODO nearby
    }
}
