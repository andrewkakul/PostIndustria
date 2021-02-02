package com.example.postindustriaandroid.data.adapters.upload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.OnCardListener
import com.example.postindustriaandroid.data.database.entity.UploadEntity

class UploadAdapter(): RecyclerView.Adapter<UploadAdapter.ViewHolder>() {

    constructor(onCardListener: OnCardListener) : this() {
        this.cardListener = onCardListener
    }

    private lateinit var cardListener: OnCardListener
    private var cardList = ArrayList<UploadEntity>()

    class ViewHolder(view: View, onCardListener: OnCardListener): RecyclerView.ViewHolder(view), View.OnClickListener{
        private var textOnCard: TextView? = null
        private var photoOnCard: ImageView? = null
        private var onCardListener: OnCardListener

        init{
            photoOnCard = itemView.findViewById(R.id.photo_on_card)
            textOnCard = itemView.findViewById(R.id.text_on_card)
            this.onCardListener = onCardListener

            itemView.setOnClickListener(this)
        }
        fun bind(uploadEntity: UploadEntity){
            photoOnCard?.let { Glide.with(itemView.context).load(uploadEntity.photoUrl).override(720,720).into(it)
            textOnCard?.text = uploadEntity.searchText
            }
        }

        override fun onClick(v: View?) {
            onCardListener.onCardClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photocard, parent, false)
        return ViewHolder(itemView, cardListener )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun deleteItem(position: Int){
        cardList.remove(cardList[position])
    }

    fun getItem(position: Int): UploadEntity{
        return cardList[position]
    }

    fun setData(cardList: ArrayList<UploadEntity>){
        this.cardList = cardList
        notifyDataSetChanged()
    }
}