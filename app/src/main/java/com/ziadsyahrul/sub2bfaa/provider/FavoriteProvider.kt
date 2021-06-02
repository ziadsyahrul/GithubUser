package com.ziadsyahrul.sub2bfaa.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.AUTHORITY
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.CONTENT_URI
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.TABLE_NAME
import com.ziadsyahrul.sub2bfaa.helper.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var favHelper: FavoriteHelper
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            uriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                FAV_ID
            )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when(FAV_ID) {
            uriMatcher.match(uri) -> favHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(FAV){
            uriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            FAV -> favHelper.queryAll()
            FAV_ID -> favHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when(FAV_ID) {
            uriMatcher.match(uri) -> favHelper.update(uri.lastPathSegment.toString(), values!!)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}