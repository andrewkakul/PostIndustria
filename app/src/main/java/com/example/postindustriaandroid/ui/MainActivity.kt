package com.example.postindustriaandroid.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.SwipeToDeleteCardCallback
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.PhotoCardAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.data.service.PhotoRepository
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
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
    private lateinit var db: PhotoRoomDatabase
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input_text_ET.setText(SharedPrefsManager.getHistory())

        db = PhotoRoomDatabase.getDatabase(applicationContext)
        recyclerView = photo_cards_rv
        photoAdapter = PhotoCardAdapter(cardsList, this)
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCardCallback(photoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        IV_link_to_favourite.setOnClickListener{
            toFavoriteActivity()
        }

        IV_link_to_history.setOnClickListener{
            toHistoryActivity()
        }

        IV_link_to_map.setOnClickListener {
            toMapsActivity()
        }

        search_btn.setOnClickListener {
            if (input_text_ET.text.isNotEmpty())
                lifecycleScope.launch {
                    saveLastSearch(input_text_ET.text.toString())
                    executeSearch()
            }
        }
    }

    private fun saveLastSearch(searchText: String) {
        SharedPrefsManager.saveHistory(searchText)
        lifecycleScope.launch(Dispatchers.IO){
            val user_id = db.userDao().getUser(SharedPrefsManager.getLogin()).id
            db.historyDao().insert(HistoryEntity(0,searchText,user_id))
        }
    }

    private fun toHistoryActivity(){
        lifecycleScope.launch(Dispatchers.IO) {
            val user: UserEntity = db.userDao().getUser(login = SharedPrefsManager.getLogin())
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            intent.putExtra(WebViewActivity.USERID, user.id)

            startActivity(intent)
        }
    }

    private fun toFavoriteActivity(){
        lifecycleScope.launch(Dispatchers.IO) {
            val user: UserEntity = db.userDao().getUser(login = SharedPrefsManager.getLogin())
            val intent = Intent(this@MainActivity, FavouritePhotoActivity::class.java)
            intent.putExtra(WebViewActivity.USERID, user.id)
            startActivity(intent)
        }
    }

    private fun toMapsActivity(){
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
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
                Log.e(TAG, t.toString())
            }
        })
    }

   private fun createCardsList(element: FlickrPhotoResponce) {
       cardsList.clear()
       element.photos.photo.forEach {
           val photoCard = FlickrPhotoCard(textForSearch, it.generateUrl())
           cardsList.add(photoCard)
       }
   }

    override fun onCardClicked(position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user: UserEntity = db.userDao().getUser(login = SharedPrefsManager.getLogin())
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.PHOTOURL, cardsList[position].photoUrl)
            intent.putExtra(WebViewActivity.SEARCHTEXT, cardsList[position].searchText)
            intent.putExtra(WebViewActivity.USERID, user.id)
            startActivity(intent)
        }
    }
}