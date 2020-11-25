package com.example.postindustriaandroid.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.model.FlickrPhotoCard

class PhotoCardAdapter(private val cardList: List<FlickrPhotoCard>): RecyclerView.Adapter<PhotoCardAdapter.PhotoViewHolder?>(){

    class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var textOnCard: TextView? = null
        var photoOnCard: ImageView? = null
        init {
            textOnCard = itemView.findViewById(R.id.text_on_card)
            photoOnCard = itemView.findViewById(R.id.photo_on_card)
        }

        fun bind(card: FlickrPhotoCard){
            textOnCard?.text = card.searchText
            photoOnCard?.let { Glide.with(itemView.context).load(card.photoUrl).into(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photocard, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val card: FlickrPhotoCard = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}