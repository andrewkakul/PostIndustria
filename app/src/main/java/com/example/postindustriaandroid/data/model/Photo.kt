package com.example.postindustriaandroid.data.model

data class Photo(
    val  id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String,
    val ispublic: String,
    val isfriend: String,
    val isfamily: String
) {
    fun generateUrl(): String {
        return "https://live.staticflickr.com/${server}/${id}_${secret}_w.jpg"
    }
}
