package com.sphara.slide.wallpaper.data

data class ImageData(
    val image: Int,
    val uuid: String = "",
    var isFavorite: Boolean = false
) {
}