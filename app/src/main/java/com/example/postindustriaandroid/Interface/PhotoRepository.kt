package com.example.postindustriaandroid.Interface

import com.example.postindustriaandroid.Model.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PhotoRepository {
    companion object{
        const val BASE_URL = "https://www.flickr.com/services/api/"
    }

    @GET("flickr.photos.search.html")
    fun getFhoto(
        @QueryMap options: Map<String, String>
    ): Call<MutableList<Photo>>
}