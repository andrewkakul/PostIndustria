package com.example.postindustriaandroid.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.search.SwipeToDeleteCardCallback
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.PhotoCardAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.data.service.NetworkManager
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

        IV_link_to_gallery.setOnClickListener {
            toGalleryActivity()
        }

        IV_link_to_map.setOnClickListener {
            toMapsActivity()
        }

        search_btn.setOnClickListener {
            if (input_text_ET.text?.isNotEmpty() == true)
                lifecycleScope.launch {
                    saveLastSearch(input_text_ET.text.toString())
                    executeSearch()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuThemeChange -> {
                val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (currentNightMode) {
                    Configuration.UI_MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    Configuration.UI_MODE_NIGHT_NO ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
        return true
    }


    private fun saveLastSearch(searchText: String) {
        SharedPrefsManager.saveHistory(searchText)
        lifecycleScope.launch(Dispatchers.IO){
            val user_id = db.userDao().getUser(SharedPrefsManager.getLogin()).id
            db.historyDao().insert(HistoryEntity(0,searchText,user_id))
        }
    }

    private fun  toGalleryActivity(){
        lifecycleScope.launch {
            val user: UserEntity = db.userDao().getUser(login = SharedPrefsManager.getLogin())
            val intent = Intent(this@MainActivity, GalleryActivity::class.java)
            intent.putExtra(WebViewActivity.USERID, user.id)
            startActivity(intent)
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
        val service = NetworkManager.createService()
        val data = NetworkManager.createData(textForSearch)
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