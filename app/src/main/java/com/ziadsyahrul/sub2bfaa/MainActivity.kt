package com.ziadsyahrul.sub2bfaa

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ziadsyahrul.sub2bfaa.ItemClick.OnItemClickCallback
import com.ziadsyahrul.sub2bfaa.adapter.Adapter
import com.ziadsyahrul.sub2bfaa.databinding.ActivityMainBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listData: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getGithubUser()

    }

    private fun getGithubUser() {
        showLoading(true)
        val clint = AsyncHttpClient()
        clint.addHeader("Authorization", "token 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")
        clint.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        clint.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val listUser = ArrayList<User>()
                showLoading(false)
                val result = String(responseBody!!)
                try {

                    val array = JSONArray(result)
                    for (i in 0 until array.length()) {
                        val item = array.getJSONObject(i)
                        val username = item.getString("login")
                        val user = User()
                        listUser.add(user)
                        getDetail(username)
                    }

                } catch (e: Exception) {
                    Log.d("TAG", "onSuccess: ")
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                Log.d("GAGALGETUSER", "onFailure()")
            }

        })
    }

    private fun getDetail(username: String){
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val listUser = ArrayList<User>()
                showLoading(false)
                try {

                    val json = JSONObject(String(responseBody!!))
                    listData.add(
                        User(
                            json.getString("login"),
                            json.getString("name"),
                            json.getString("avatar_url"),
                            json.getString("company"),
                            json.getString("public_repos"),
                            json.getString("location"),
                            json.getString("followers"),
                            json.getString("following"),
                        )
                    )

                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.setHasFixedSize(true)
                    val adapter = Adapter(listData)
                    binding.recyclerView.adapter = adapter

                    adapter.setOnItemClick(object : OnItemClickCallback {
                        override fun onItemClicked(user: User) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_USER, listUser)
                            startActivity(intent)
                        }
                    })

                } catch (e: Exception) {
                    Log.d("onsuccess", "onSuccess: ")
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                Log.d("OnFAILURE", "onFailure()")
            }

        })

    }

    private fun getSearch(username: String){
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                var listUser = ArrayList<User>()

                showLoading(false)
                try {

                    val responseObject = JSONObject(String(responseBody!!))
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val user = User()
                        user.user_name = username
                        listUser.add(user)
                        getDetail(username)
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "catch", Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                Log.d("GAGALSEARCH", "onFailure()")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater  = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menu)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listData.clear()
                getSearch(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.favorite_menu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.setting_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showLoading(state: Boolean){
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}