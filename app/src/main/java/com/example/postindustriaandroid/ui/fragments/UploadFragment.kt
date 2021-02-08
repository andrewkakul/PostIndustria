package com.example.postindustriaandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.adapters.upload.DeleteUploadListener
import com.example.postindustriaandroid.data.adapters.upload.SwipeToDeleteUpload
import com.example.postindustriaandroid.data.adapters.upload.UploadAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.UploadEntity
import com.example.postindustriaandroid.data.viewmodel.BaseViewModel
import com.example.postindustriaandroid.data.viewmodel.UploadViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadFragment : Fragment(), OnCardListener, DeleteUploadListener{

    private lateinit var adapter: UploadAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: UploadViewModel
    private lateinit var model: BaseViewModel
    private var twoPain = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = PhotoRoomDatabase.getDatabase(activity?.applicationContext!!)
        adapter = UploadAdapter(this)
        recyclerView = upload_RV
        initViewModel()
        if (search_detail_container != null) {
            twoPain = true;
        }
    }

    private fun initViewModel(){
        model = ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteUpload(adapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)
        viewModel.uploadPhoto.observe(viewLifecycleOwner,{
            adapter.setData(it)
        })
        viewModel.getListUpload(SharedPrefsManager.getUserID(), db)
    }

    override fun onCardClicked(position: Int) {
       val uploadEntity = adapter.getItem(position)
        model.setData(uploadEntity.photoUrl, uploadEntity.searchText)
        if(twoPain){
            val fragment = WebViewFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.upload_detail_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            findNavController().navigate(R.id.action_uploadFragment_to_webViewFragment)
        }
    }

    override fun deleteUploadFromDB(uploadEntity: UploadEntity) {
        lifecycleScope.launch(Dispatchers.IO){
            db.uploadDao().delete(uploadEntity)
        }
    }
}