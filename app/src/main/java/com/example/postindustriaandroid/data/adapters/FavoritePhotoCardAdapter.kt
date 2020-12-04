package com.example.postindustriaandroid.data.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.model.FavouritePhotoCard
import kotlinx.android.synthetic.main.item_favourite_photocard.view.*
import kotlinx.android.synthetic.main.item_searchtext.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

class FavoritePhotoCardAdapter: RecyclerView.Adapter<FavoritePhotoCardAdapter.FavoritePhotoViewHolder?>() {

    companion object {
        private const val TITLE_VIEW_TYPE = 1
        private const val CONTENT_VIEW_TYPE = 2
    }

    private var mCardList = ArrayList<Any>()

    abstract class FavoritePhotoViewHolder(view: View):RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePhotoViewHolder {
        return when (viewType) {
            TITLE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searchtext, parent, false)
                TitleViewHolder(view)
            }
            CONTENT_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_photocard, parent, false)
                ContentViewHolder(view)
            }
            else ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_photocard, parent, false)
                DefaultViewHolder(view)
            }
        }
    }

    class TitleViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {
        fun bind(content: FavouritePhotoCard?) = with(itemView) {
            content?.let {
                tvItemName.text = it.searchText
            }
        }
    }

    class ContentViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {
        var onItemRemoveClick: (() -> Unit)? = null
        fun bind(content: FavouritePhotoEntity?) = with(itemView) {
            content?.let { photo ->
                Glide.with(itemView.context)
                    .load(photo.photoUrl)
                    .into(photo_onFavourite_card)
                tvItemName.text = photo.searchText
            }
            delete_favorite_btn.setOnClickListener {
                onItemRemoveClick?.invoke()
            }
        }
    }

    class DefaultViewHolder(itemView: View) : FavoritePhotoViewHolder(itemView) {}

    override fun onBindViewHolder(holder: FavoritePhotoViewHolder, position: Int) {
        val item = mCardList[position]
        if (holder is TitleViewHolder && item is FavouritePhotoCard) {
            holder.bind(item)
        } else if (holder is ContentViewHolder && item is FavouritePhotoEntity) {
            holder.bind(item)
            holder.onItemRemoveClick = {

            }
        }
    }

    override fun getItemCount(): Int {
        return mCardList.size
    }

     fun setData(newArray: List<FavouritePhotoCard>) {
         val newContent = arrayListOf<Any>()
         newArray.forEach {
             newContent.add(it)
             it.photos.forEach { photo ->
                 newContent.add(photo)
             }
         }
         mCardList.clear()
         mCardList.addAll(newContent)
     }
}