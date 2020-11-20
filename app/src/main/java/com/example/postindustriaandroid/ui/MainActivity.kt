package com.example.postindustriaandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.model.FlickrPhotoResponce
import com.example.postindustriaandroid.service.PhotoRepository
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    companion object{
        const val FLICK_PHOTO_BASE_URL = "https://www.flickr.com/photos/"
        const val FLICK_PHOTO_END_URL = "/in/dateposted"
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_btn.setOnClickListener {
            if (input_text_ET.text.isNotEmpty())
                lifecycleScope.launch {
                    executeSearch()
            }
        }
    }

    private fun executeSearch(){
        val text = input_text_ET.text.toString()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(PhotoRepository.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(PhotoRepository::class.java)
        
        val data: MutableMap<String, String> = HashMap()
            data["method"] = PhotoRepository.API_METHOD
            data["api_key"] = PhotoRepository.API_KEY
            data["format"] = PhotoRepository.API_FORMAT
            data["nojsoncallback"] = PhotoRepository.NOJSONCALLBACK
            data["text"] = text
        val call = service.getPhoto(data)

        call.enqueue(object : Callback<FlickrPhotoResponce> {
            override fun onResponse(call: Call<FlickrPhotoResponce>, response: Response<FlickrPhotoResponce>) {
                Log.d(TAG, "onResponse: $response")
                convertJsonToUrl(response.body()!!)
            }
            override fun onFailure(call: Call<FlickrPhotoResponce>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }
        })
    }

    fun convertJsonToUrl(element: FlickrPhotoResponce){
        var textToView = ""
        val sizeOfList: Int = element.photos.photo.size

        for(i in 0 until sizeOfList){
            textToView = textToView +
                    FLICK_PHOTO_BASE_URL +
                    element.photos.photo[i].owner+"/"+
                    element.photos.photo[i].id +
                    FLICK_PHOTO_END_URL + "\n\n"
        }
        textview.text = textToView
    }

}