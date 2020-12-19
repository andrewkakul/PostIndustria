package com.example.postindustriaandroid.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.model.FlickrPhotoCard

class PhotoCardAdapter(): RecyclerView.Adapter<PhotoCardAdapter.PhotoViewHolder?>(){

    private lateinit var CardList: ArrayList<FlickrPhotoCard>
    private lateinit var CardListener: OnCardListener

    constructor(cardList: ArrayList<FlickrPhotoCard>, onCardListener: OnCardListener) : this() {
        this.CardList = cardList
        this.CardListener = onCardListener
    }

    class PhotoViewHolder(view: View, onCardListener: OnCardListener): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var textOnCard: TextView? = null
        private var photoOnCard: ImageView? = null
        private var onCardListener: OnCardListener

        init{
            textOnCard = itemView.findViewById(R.id.text_on_card)
            photoOnCard = itemView.findViewById(R.id.photo_on_card)
            this.onCardListener = onCardListener

            itemView.setOnClickListener(this)
        }
        fun bind(card: FlickrPhotoCard){
            textOnCard?.text = card.searchText
            photoOnCard?.let { Glide.with(itemView.context).load(card.photoUrl).override(720,720).into(it) }
        }
        override fun onClick(v: View?) {
            onCardListener.onCardClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photocard, parent, false)
        return PhotoViewHolder(itemView, CardListener)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(CardList[position])
    }

    override fun getItemCount(): Int {
        return CardList.size
    }

    fun deleteItem(position: Int) {
        CardList.remove(CardList[position])
        notifyItemRemoved(position)
    }
}

    interface OnCardListener {
        fun onCardClicked(position: Int)
    }
