package com.example.postindustriaandroid.data.service

import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PhotoRepository {
    companion object{
        const val API_KEY = "c29e3b3725b0bd8a34ebcbe49deefe6e"
        const val BASE_URL = "https://www.flickr.com/services/rest/"
        const val API_FORMAT = "json"
        const val API_METHOD = "flickr.photos.search"
        const val NOJSONCALLBACK = "1"
    }

    @GET(".")
    fun getPhoto(
        @QueryMap options: Map<String, String>
    ): Call<FlickrPhotoResponce>
}