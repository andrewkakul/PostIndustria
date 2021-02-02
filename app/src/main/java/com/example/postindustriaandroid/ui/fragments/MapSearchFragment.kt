package com.example.postindustriaandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.PhotoCardAdapter
import com.example.postindustriaandroid.data.adapters.search.SwipeToDeleteCardCallback
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoResponce
import com.example.postindustriaandroid.utils.NetworkManager
import com.example.postindustriaandroid.data.viewmodel.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map_search.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapSearchFragment : Fragment(), OnCardListener {

    private lateinit var photoAdapter: PhotoCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: PhotoRoomDatabase
    private val TAG = "MapSearchActivity"
    private val mapSearchList: ArrayList<FlickrPhotoCard> = ArrayList()
    private val args: MapSearchFragmentArgs by navArgs()
    private var twoPain = false
    lateinit var model: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = PhotoRoomDatabase.getDatabase(activity?.applicationContext!!)
        recyclerView = map_search_rv
        photoAdapter = PhotoCardAdapter(mapSearchList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = photoAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCardCallback(photoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        model = ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)

        if (map_search_detail_container != null) {
            twoPain = true;
        }

        lifecycleScope.launch {
            executeSearch()
        }
    }

    private fun executeSearch() {
        val latLng = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
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
        val photo_url = mapSearchList[position].photoUrl
        val search_text = mapSearchList[position].searchText
        model.setData(photo_url, search_text)
        if(twoPain){
            val fragment = WebViewFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.map_search_detail_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            findNavController().navigate(R.id.action_mapSearchFragment_to_webViewFragment)
        }
    }
}