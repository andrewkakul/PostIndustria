package com.example.postindustriaandroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapter.SwipeToDeleteCallback
import com.example.postindustriaandroid.data.adapter.OnCardListener
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


class MainActivity : AppCompatActivity(), OnCardListener {

    private lateinit var textForSearch: String
    private var cardsList: ArrayList<FlickrPhotoCard> = ArrayList()
    private lateinit var photoAdapter: PhotoCardAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = photo_cards_rv
        photoAdapter = PhotoCardAdapter(cardsList, this)
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(photoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

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
                createCardsList(response.body()!!)
                photo_cards_rv.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<FlickrPhotoResponce>, t: Throwable) {
            }
        })
    }

   private fun createCardsList(element: FlickrPhotoResponce) {
       cardsList.clear()
       element.photos.photo.forEach() {
           val photoCard = FlickrPhotoCard("", "")
           photoCard.photoUrl = it.generateUrl()
           photoCard.searchText = textForSearch
           cardsList.add(photoCard)
       }
   }

    override fun onCardClicked(position: Int) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.PHOTOURL, cardsList[position].photoUrl)
        intent.putExtra(WebViewActivity.SEARCHTEXT, cardsList[position].searchText)
        startActivity(intent)
    }
}