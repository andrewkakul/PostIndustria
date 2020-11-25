package com.example.postindustriaandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapter.PhotoCardAdapter
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.data.service.PhotoRepository
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var textForSearch: String = ""
    private var cardsList: MutableList<FlickrPhotoCard> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photo_cards_rv.layoutManager = LinearLayoutManager(this)
        photo_cards_rv.adapter = PhotoCardAdapter(cardsList)

        search_btn.setOnClickListener {
            if (input_text_ET.text.isNotEmpty())
                lifecycleScope.launch {
                    executeSearch()
            }
        }
    }


    private fun executeSearch(){
        textForSearch = input_text_ET.text.toString()
        val gson = GsonBuilder().setLenient().create()

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
            data["text"] = textForSearch
        val call = service.getPhoto(data)

        call.enqueue(object : Callback<FlickrPhotoResponce> {
            override fun onResponse(call: Call<FlickrPhotoResponce>, response: Response<FlickrPhotoResponce>) {
                Log.d(TAG, "onResponse: $response")
                createCardsList(response.body()!!)
            }

            override fun onFailure(call: Call<FlickrPhotoResponce>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }
        })
    }

   private fun createCardsList(element: FlickrPhotoResponce) {
        val sizeOfList: Int = element.photos.photo.size
        val photoCard = FlickrPhotoCard("","")
        for(i in 0 until sizeOfList){
            photoCard.photoUrl = PhotoRepository.FLICK_PHOTO_BASE_URL +
                    element.photos.photo[i].owner + "/" +
                    element.photos.photo[i].id +
                    PhotoRepository.FLICK_PHOTO_END_URL
            photoCard.searchText = textForSearch
            cardsList.add(i, photoCard)
        }
    }
}