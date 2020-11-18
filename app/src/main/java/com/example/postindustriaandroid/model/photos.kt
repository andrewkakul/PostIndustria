package com.example.postindustriaandroid.model

data class photos(
        val page: String,
        val pages: String,
        val perpage: String,
        val total: String,
        val photo: List<Photo>
)
