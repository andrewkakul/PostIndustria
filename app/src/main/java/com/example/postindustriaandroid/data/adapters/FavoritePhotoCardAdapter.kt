package com.example.postindustriaandroid.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.model.FavouritePhotoCard
import com.example.postindustriaandroid.data.model.FlickrPhotoCard
import kotlinx.android.synthetic.main.item_favourite_photocard.view.*
import kotlinx.android.synthetic.main.item_searchtext.view.*

class FavoritePhotoCardAdapter(): RecyclerView.Adapter<FavoritePhotoCardAdapter.FavoritePhotoViewHolder>() {

    companion object{
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTENT = 1
    }

    private var mCardList = ArrayList<Any>()

    constructor(favouriteList: ArrayList<Any>) : this() {
        this.mCardList = favouriteList
    }

    open class FavoritePhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePhotoViewHolder {
        return when(viewType){
            TYPE_HEADER ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searchtext, parent, false)
                TitleViewHolder(view)
            }
            TYPE_CONTENT ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_photocard, parent, false)
                ContentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: FavoritePhotoViewHolder, position: Int) {
        val element = mCardList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(element as FavouritePhotoCard)
            is ContentViewHolder -> holder.bind(element as FavouritePhotoEntity)
            else -> throw IllegalArgumentException()
        }
    }
    class TitleViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {
        fun bind(content: FavouritePhotoCard) = with(itemView) {
            content.let {
                tvItemName.text = it.searchText
            }
        }
    }

    class ContentViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {
        fun bind(content: FavouritePhotoEntity) = with(itemView) {
                content.let { photo ->
                    Glide.with(itemView.context)
                        .load(photo.photoUrl)
                        .into(photo_onFavourite_card)
                }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (mCardList[position]) {
            is FavouritePhotoCard -> TYPE_HEADER
            is FavouritePhotoEntity -> TYPE_CONTENT
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }


    override fun getItemCount(): Int {
        return mCardList.size
    }

}