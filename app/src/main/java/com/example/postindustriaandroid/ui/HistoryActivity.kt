package com.example.postindustriaandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.HistoryAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.viewmodel.HistoryViewModel
import com.example.postindustriaandroid.data.viewmodel.PreviewFavouriteViewModel
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        db = PhotoRoomDatabase.getDatabase(this)
        recyclerView = history_RV
        initViewModel()
    }

    private fun initViewModel(){
        val user_id = intent.getLongExtra(WebViewActivity.USERID, -1)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.listOfHistory.observe(this,{ hitoryList ->
            historyAdapter = HistoryAdapter(hitoryList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = historyAdapter
        })
        viewModel.getListHistory(user_id, db)
    }
}