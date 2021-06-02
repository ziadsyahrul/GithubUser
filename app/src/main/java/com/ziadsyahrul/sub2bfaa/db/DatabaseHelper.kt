package com.ziadsyahrul.sub2bfaa.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.COMPANY
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FAVORITE
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FOLLOWERS
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.FOLLOWING
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.LOCATION
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.NAME
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.PHOTO
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.REPOSITORY
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.TABLE_NAME
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.USERNAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "dbUser"
        private const val DATABASE_VERSION = 2
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " (${USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${NAME} TEXT NOT NULL," +
                " ${PHOTO} TEXT NOT NULL," +
                " ${COMPANY} TEXT NOT NULL," +
                " ${LOCATION} TEXT NOT NULL," +
                " ${REPOSITORY} TEXT NOT NULL," +
                " ${FOLLOWERS} TEXT NOT NULL," +
                " ${FOLLOWING} TEXT NOT NULL," +
                " ${FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldV: Int, newV: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

}