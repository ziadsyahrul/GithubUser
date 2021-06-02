package com.ziadsyahrul.sub2bfaa

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ziadsyahrul.sub2bfaa.adapter.FavoriteAdapter
import com.ziadsyahrul.sub2bfaa.databinding.ActivityFavoriteBinding
import com.ziadsyahrul.sub2bfaa.db.DatabaseContract.FavoriteColumn.Companion.CONTENT_URI
import com.ziadsyahrul.sub2bfaa.helper.MappingHelper
import com.ziadsyahrul.sub2bfaa.model.FavoriteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite User"

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.recyclerViewFavorite.adapter = adapter


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadFavoriteAsync()
        } else{
            val list = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_STATE)
            if (list != null){
                adapter.listNotes = list
            }
        }

    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main){
            binding.progressBarFavorite.visibility = View.VISIBLE
            val defferedFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val data = defferedFavorite.await()
            binding.progressBarFavorite.visibility = View.GONE
            if (data.size > 0){
                adapter.listNotes = data
            }else{
                adapter.listNotes = ArrayList()
                showSnackBar()
            }
        }
    }

    private fun showSnackBar() {
        Snackbar.make(binding.recyclerViewFavorite, "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }
}