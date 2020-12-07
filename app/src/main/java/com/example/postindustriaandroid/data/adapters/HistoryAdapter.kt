package com.example.postindustriaandroid.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.entity.HistoryEntity

class HistoryAdapter(): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private lateinit var mHistoryList: ArrayList<HistoryEntity>

    constructor(listOfHistory: ArrayList<HistoryEntity>) : this() {
        this.mHistoryList = listOfHistory
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var searchText: TextView? = null
        init {
            searchText = itemView.findViewById(R.id.tvItemName)
        }

        fun bind(historyEntity: HistoryEntity){
            searchText?.text = historyEntity.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_searchtext, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(mHistoryList[position])
    }

    override fun getItemCount(): Int {
        return mHistoryList.size
    }
}