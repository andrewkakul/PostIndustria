package com.example.postindustriaandroid.data.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.data.database.entity.FilesEntity

class SwipeToDeleteDownloadCard(adapter: DownloadFilesAdapter, deleteFileListener: DeleteFileListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var adapter: DownloadFilesAdapter = adapter
    private var deleteFileListener = deleteFileListener

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.getItem(position)
        adapter.deleteItem(position)
        deleteFileListener.deleteFileFromDB(item)
        adapter.notifyItemRemoved(position)
    }
}

interface DeleteFileListener{
    fun deleteFileFromDB(filesEntity: FilesEntity)
}