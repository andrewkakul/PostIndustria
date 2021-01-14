package com.example.postindustriaandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.PhotoCardAdapter
import com.example.postindustriaandroid.data.adapters.search.SwipeToDeleteCardCallback
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.UserEntity
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.data.service.NetworkManager
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_map_search.*
import kotlinx.coroutines.Dispatchers

class MapSearchActivity : AppCompatActivity(), OnCardListener {

    companion object{
        const val PHOTO_LAT = "photo_lat"
        const val PHOTO_lON = "photo_lon"
    }

    private lateinit var photoAdapter: PhotoCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: PhotoRoomDatabase
    private val TAG = "MapSearchActivity"
    private val mapSearchList: ArrayList<FlickrPhotoCard> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefsManager.getTheme() == MainActivity.NIGHT)
            setTheme(R.style.Theme_PostindustriaAndroid_Dark)
        setContentView(R.layout.activity_map_search)

        db = PhotoRoomDatabase.getDatabase(applicationContext)
        recyclerView = map_serach_rv
        photoAdapter = PhotoCardAdapter(mapSearchList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = photoAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCardCallback(photoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        lifecycleScope.launch {
            executeSearch()
        }
    }

    private fun executeSearch() {
        val latLng = LatLng(intent.getDoubleExtra(PHOTO_LAT, -1.1), intent.getDoubleExtra(PHOTO_lON, -1.1))
        val service = NetworkManager.createService()
        val data = NetworkManager.createData(latLng)
        val call = service.getPhoto(data)

        call.enqueue(object : Callback<FlickrPhotoResponce> {
            override fun onResponse(call: Call<FlickrPhotoResponce>, response: Response<FlickrPhotoResponce>) {
                createCardsList(response.body()!!)
                photoAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<FlickrPhotoResponce>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

    private fun createCardsList(element: FlickrPhotoResponce) {
        mapSearchList.clear()
        element.photos.photo.forEach {
            val photoCard = FlickrPhotoCard(it.title, it.generateUrl())
            mapSearchList.add(photoCard)
        }
    }

    override fun onCardClicked(position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user: UserEntity = db.userDao().getUser(login = SharedPrefsManager.getLogin())
            val intent = Intent(this@MapSearchActivity, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.PHOTOURL, mapSearchList[position].photoUrl)
            intent.putExtra(WebViewActivity.SEARCHTEXT, mapSearchList[position].searchText)
            intent.putExtra(WebViewActivity.USERID, user.id)
            startActivity(intent)
        }
    }
}