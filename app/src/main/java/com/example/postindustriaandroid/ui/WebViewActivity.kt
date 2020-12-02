package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {

    private var db: PhotoRoomDatabase? = null

    companion object{
        const val PHOTOURL = "photoUrlKey"
        const val SEARCHTEXT = "searchTextKey"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        SharedPrefsManager.init(this)

        db = PhotoRoomDatabase.getDatabase(this)
        text_webview_activity.text = intent.getStringExtra(SEARCHTEXT)
        webview.loadUrl(intent.getStringExtra(PHOTOURL).toString())

        chose_to_favorite_btn.setOnClickListener {
            isFavorite()

        }
    }

    private fun isFavorite(){
        lifecycleScope.launch(Dispatchers.IO) {
            val photoUrl: String = intent.getStringExtra(PHOTOURL).toString()
            val searchText: String = intent.getStringExtra(SEARCHTEXT).toString()
            val user: UserEntity = db?.userDao()?.userAuthorization(login = SharedPrefsManager.getLogin())!!
            val favouritePhotoEntity = db?.photoCardDao()?.checkIsFavourite(photoUrl = photoUrl, userID = user.id)
            if (favouritePhotoEntity == null) {
                toFavourite(user, photoUrl, searchText)
            }else {
                db?.photoCardDao()?.delete(favouritePhotoEntity)
            }
        }
    }

    private fun toFavourite(userEntity: UserEntity, photoUrl: String, searchText: String){
        db?.photoCardDao()?.insert(FavouritePhotoEntity(0, userEntity.id, photoUrl, searchText))
    }
}