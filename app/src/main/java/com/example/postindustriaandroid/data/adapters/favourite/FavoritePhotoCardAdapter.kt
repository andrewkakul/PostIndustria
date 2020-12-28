package com.example.postindustriaandroid.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.android.synthetic.main.item_favourite_photocard.view.*
import kotlinx.android.synthetic.main.item_searchtext.view.*

class FavoritePhotoCardAdapter(): RecyclerView.Adapter<FavoritePhotoCardAdapter.FavoritePhotoViewHolder>() {

    companion object{
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTENT = 1
    }

    constructor(onFavouriteCardListener: OnFavouriteCardListener) : this() {
        this.CardListener = onFavouriteCardListener
    }

    private var CardList = ArrayList<Any>()
    private lateinit var CardListener: OnFavouriteCardListener

    abstract class FavoritePhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePhotoViewHolder {
        return when(viewType){
            TYPE_HEADER ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searchtext, parent, false)
                TitleViewHolder(view)
            }
            TYPE_CONTENT ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_photocard, parent, false)
                ContentViewHolder(view, CardListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: FavoritePhotoViewHolder, position: Int) {
        val element = CardList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(element as String)
            is ContentViewHolder -> holder.bind(element as FavouritePhotoEntity)
            else -> throw IllegalArgumentException()
        }
    }
    class TitleViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {
        fun bind(content: String) = with(itemView) {
            content.let {
                tvItemName.text = it
            }
        }
    }

    class ContentViewHolder(itemView: View, onFavouriteCardListener: OnFavouriteCardListener) : FavoritePhotoViewHolder(itemView) {
        private var onFavouriteCardListener = onFavouriteCardListener
        fun bind(content: FavouritePhotoEntity) = with(itemView) {
                content.let { photo ->
                    Glide.with(itemView.context)
                        .load(photo.photoUrl)
                        .into(photo_onFavourite_card)
                }
                delete_favorite_btn.setOnClickListener {
                    onFavouriteCardListener.onDeleteBtnClick(adapterPosition, content)
                }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (CardList[position]) {
            is String -> TYPE_HEADER
            is FavouritePhotoEntity -> TYPE_CONTENT
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int {
        return CardList.size
    }

    fun checkPreviewElement(position: Int): Boolean{
       return when(CardList[position-1]){
           is String -> true
           else -> false
       }
    }

    fun deleteItem(position: Int) {
        CardList.remove(CardList[position])
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Any{
        return CardList[position]
    }


    fun setData(favouriteData: ArrayList<Any>){
        CardList = favouriteData
        notifyDataSetChanged()
    }
}

    interface OnFavouriteCardListener{
        fun onDeleteBtnClick(position: Int, favouritePhotoEntity: FavouritePhotoEntity)
    }