package com.example.postindustriaandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.history.HistoryAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.viewmodel.HistoryViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = PhotoRoomDatabase.getDatabase(activity?.applicationContext!!)
        recyclerView = history_RV
        initViewModel()
    }

    private fun initViewModel(){
        val user_id = SharedPrefsManager.getUserID()
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.listOfHistory.observe(viewLifecycleOwner,{ hitoryList ->
            historyAdapter = HistoryAdapter(hitoryList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = historyAdapter
        })
        viewModel.getListHistory(user_id, db)
    }
}