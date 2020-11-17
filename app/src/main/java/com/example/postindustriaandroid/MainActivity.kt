package com.example.postindustriaandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.Interface.PhotoRepository
import com.example.postindustriaandroid.Model.Photo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.QueryMap

class MainActivity : AppCompatActivity() {
    val BASE_URL = "https://www.flickr.com/services/api/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_btn.setOnClickListener {
            if(input_text_ET.text.isNotEmpty()){
                lifecycleScope.launch {
                    executeSearch()
                }
            }
        }
    }

    private fun executeSearch(){
        val text = input_text_ET.text.toString()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val service = retrofit.create(PhotoRepository::class.java)
        val data: MutableMap<String, String> = HashMap()
            data["text"] = text
        val call = service.getFhoto(data)

        call.enqueue(object: Callback<MutableList<Photo>>{
            override fun onResponse(call: Call<MutableList<Photo>>, response: Response<MutableList<Photo>>) {
            }

            override fun onFailure(call: Call<MutableList<Photo>>, t: Throwable) {
            }

        })
    }
}