package com.example.postindustriaandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.model.PhotoRepository
import com.example.postindustriaandroid.model.photos
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

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
        val interceptor = HttpLoggingInterceptor()

        val gson = GsonBuilder()
                .setLenient()
                .create()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl(PhotoRepository.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build()

        val service = retrofit.create(PhotoRepository::class.java)
        val data: MutableMap<String, String> = HashMap()
            data["method"] = PhotoRepository.API_METHOD
            data["api_key"] = PhotoRepository.API_KEY
            data["format"] = PhotoRepository.API_FORMAT
            data["nojsoncallback"] = PhotoRepository.NOJSONCALLBACK
            data["text"] = text
        val call = service.getPhoto(data)

        call.enqueue(object : Callback<photos> {
            override fun onResponse(call: Call<photos>, response: Response<photos>) {
                Log.d(TAG, "onResponse: $response")
                textview.text = response.body().toString()
            }
            override fun onFailure(call: Call<photos>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }
        })
    }
}