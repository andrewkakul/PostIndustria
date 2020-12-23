package com.example.postindustriaandroid.data.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity

class SwipeToDeleteFavouriteCallback(adapter: FavoritePhotoCardAdapter, deleteItemListener: DeleteItemListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var adapter: FavoritePhotoCardAdapter = adapter
    private var deleteItemListener = deleteItemListener

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.getItem(position)
        if (item is FavouritePhotoEntity) {
            adapter.deleteItem(position)
            if (adapter.checkPreviewElement(position))
                adapter.deleteItem(position - 1)
            deleteItemListener.deleteItemFromDB(item)
            adapter.notifyItemRemoved(position)
        }
    }
}

    interface DeleteItemListener{
        fun deleteItemFromDB(favouritePhotoEntity: FavouritePhotoEntity)
    }