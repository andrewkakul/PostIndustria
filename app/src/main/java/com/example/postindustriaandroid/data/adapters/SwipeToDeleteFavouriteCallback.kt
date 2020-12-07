package com.example.postindustriaandroid.data.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity

class SwipeToDeleteFavouriteCallback(adapter: FavoritePhotoCardAdapter, deleteItemListener: DeleteItemListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var mAdapter: FavoritePhotoCardAdapter = adapter
    private var deleteItemListener = deleteItemListener

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = mAdapter.getItem(position)
        if (item is FavouritePhotoEntity) {
            mAdapter.deleteItem(position)
            if (mAdapter.checkPreviewElement(position))
                mAdapter.deleteItem(position - 1)
            deleteItemListener.deleteItemFromDB(item)
            mAdapter.notifyItemRemoved(position)
        }
    }
}

    interface DeleteItemListener{
        fun deleteItemFromDB(favouritePhotoEntity: FavouritePhotoEntity)
    }