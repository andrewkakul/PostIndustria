package com.example.postindustriaandroid.data.model

import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity

data class FavouritePhotoCard(
    var photos: ArrayList<FavouritePhotoEntity>,
    val searchText: String
)
