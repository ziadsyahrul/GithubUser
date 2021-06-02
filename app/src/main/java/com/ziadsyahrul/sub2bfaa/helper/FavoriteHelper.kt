package com.ziadsyahrul.sub2bfaa.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.TABLE_NAME
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.USERNAME
import com.ziadsyahrul.sub2bfaa.db.DatabaseHelper
import java.sql.SQLException
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var db: SQLiteDatabase = databaseHelper.writableDatabase

    companion object{
        private const val DB_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open(){
        db = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if (db.isOpen)
            db.close()
    }

    fun queryAll(): Cursor {
        return db.query(
            DB_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$USERNAME ASC",
            null
        )
    }

    fun queryByUsername(username: String): Cursor{
        return db.query(
            DB_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long{
        return db.insert(DB_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int{
        return db.delete(
            DB_TABLE,
            "$USERNAME = '$username'",
            null
        )
    }

    fun update(username: String, values:ContentValues): Int{
        return db.update(
            DB_TABLE,
            values,
            "$USERNAME =?",
            arrayOf(username)
        )
    }

}