package com.example.postindustriaandroid.data.adapters.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.entity.FilesEntity

class DownloadFilesAdapter(): RecyclerView.Adapter<DownloadFilesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var photoOnCard: ImageView? = null
        init{
            photoOnCard = itemView.findViewById(R.id.photo_on_download_card)
        }
        fun bind(filesEntity: FilesEntity){
            photoOnCard?.let { Glide.with(itemView.context).load(filesEntity.uri.toUri()).override(720,720).into(it)}
        }
    }

    private var CardList = ArrayList<FilesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_download_card,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(CardList[position])
    }

    override fun getItemCount(): Int {
        return CardList.size
    }

    fun setData(cardList: ArrayList<FilesEntity>){
        CardList = cardList
        notifyDataSetChanged()
    }

    fun getItem(position: Int): FilesEntity{
        return CardList[position]
    }

    fun deleteItem(position: Int) {
        CardList.remove(CardList[position])
        notifyItemRemoved(position)
    }
}