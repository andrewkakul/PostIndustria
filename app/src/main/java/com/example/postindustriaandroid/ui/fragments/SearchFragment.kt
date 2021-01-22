package com.example.postindustriaandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.PhotoCardAdapter
import com.example.postindustriaandroid.data.adapters.search.SwipeToDeleteCardCallback
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.data.service.NetworkManager
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(), OnCardListener {

    private lateinit var textForSearch: String
    private var cardsList: ArrayList<FlickrPhotoCard> = ArrayList()
    private lateinit var photoAdapter: PhotoCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: PhotoRoomDatabase

    private val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input_text_ET.setText(SharedPrefsManager.getHistory())

        db = activity?.applicationContext?.let { PhotoRoomDatabase.getDatabase(it) }!!
        recyclerView = photo_cards_rv
        photoAdapter = PhotoCardAdapter(cardsList, this)
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCardCallback(photoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        search_btn.setOnClickListener {
            if (input_text_ET.text?.isNotEmpty() == true)
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
            val photo_url = cardsList[position].photoUrl
            val search_text = cardsList[position].searchText
            val user_id = SharedPrefsManager.getUserID()
            val action = SearchFragmentDirections.actionSearchFragmentToWebViewFragment(photo_url,search_text,user_id)
            findNavController().navigate(action)
    }
}