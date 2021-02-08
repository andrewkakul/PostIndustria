package com.example.postindustriaandroid.utils

import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {

    fun createService(): PhotoRepository {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(PhotoRepository.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val service = retrofit.create(PhotoRepository::class.java)
        return service
    }

    fun createData(latLng: LatLng): MutableMap<String, String>{
        val data: MutableMap<String, String> = HashMap()
        data["method"] = PhotoRepository.API_METHOD
        data["api_key"] = PhotoRepository.API_KEY
        data["format"] = PhotoRepository.API_FORMAT
        data["nojsoncallback"] = PhotoRepository.NOJSONCALLBACK
        data["lat"] = latLng.latitude.toString()
        data["lon"] = latLng.longitude.toString()
        return data
    }

    fun createData(textForSearch: String): MutableMap<String, String>{
        val data: MutableMap<String, String> = HashMap()
        data["method"] = PhotoRepository.API_METHOD
        data["api_key"] = PhotoRepository.API_KEY
        data["format"] = PhotoRepository.API_FORMAT
        data["nojsoncallback"] = PhotoRepository.NOJSONCALLBACK
        data["text"] = textForSearch
        return data
    }
}