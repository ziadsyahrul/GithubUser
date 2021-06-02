package com.ziadsyahrul.sub2bfaa.helper

import android.database.Cursor
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract
import com.ziadsyahrul.sub2bfaa.model.FavoriteModel

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<FavoriteModel>{
        val listFavorite = ArrayList<FavoriteModel>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.NAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.PHOTO))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.FOLLOWING))
                val favorite = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumn.FAVORITE))
                listFavorite.add(FavoriteModel(username, name, photo,company, location, repository, followers, following, favorite))
            }
        }
        return listFavorite
    }

}