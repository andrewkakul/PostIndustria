package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.postindustriaandroid.PreviewFavouriteDataBinding
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.viewmodel.PreviewFavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: PreviewFavouriteViewModel
    companion object{
        const val PHOTOURL = "photoUrlKey"
        const val SEARCHTEXT = "searchTextKey"
        const val USERID = "userID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = PhotoRoomDatabase.getDatabase(this)
        SharedPrefsManager.init(this)
        initViewModel()

        text_webview_activity.text = intent.getStringExtra(SEARCHTEXT)
        webview.loadUrl(intent.getStringExtra(PHOTOURL).toString())
    }

    private fun initViewModel(){
        val photoUrl: String = intent.getStringExtra(PHOTOURL).toString()
        val searchText: String = intent.getStringExtra(SEARCHTEXT).toString()
        val userId: Long = intent.getLongExtra(USERID, -1)

        viewModel = ViewModelProvider(this).get(PreviewFavouriteViewModel::class.java)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_webview) as PreviewFavouriteDataBinding
        binding.viewmodel = viewModel

        viewModel.isFavorite.observe(this, { isFavorite ->
            viewModel.saveData(photoUrl,searchText, db, userId)
        })
        viewModel.loadData(photoUrl, userId, db)
    }
}