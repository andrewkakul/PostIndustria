package com.example.postindustriaandroid.data.adapters.upload

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.data.database.entity.UploadEntity

class SwipeToDeleteUpload(adapter: UploadAdapter, deleteUploadListener: DeleteUploadListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var adapter: UploadAdapter = adapter
    private var deleteUploadListener = deleteUploadListener

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.getItem(position)
        adapter.deleteItem(position)
        deleteUploadListener.deleteUploadFromDB(item)
        adapter.notifyItemRemoved(position)
    }
}

interface DeleteUploadListener{
    fun deleteUploadFromDB(uploadEntity: UploadEntity)
}