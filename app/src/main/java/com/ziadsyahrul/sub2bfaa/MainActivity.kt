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
import com.ziadsyahrul.sub2bfaa.helper.Constant
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
        searchUser()

    }

    private fun searchUser() { binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                listData.clear()
                getSearch(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
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

    private fun getDetail(username: String) {
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
                            json.getString(Constant.login),
                            json.getString(Constant.name),
                            json.getString(Constant.avatar_url),
                            json.getString(Constant.company),
                            json.getString(Constant.public_repos),
                            json.getString(Constant.location),
                            json.getString(Constant.followers),
                            json.getString(Constant.following),
                        )
                    )

                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.setHasFixedSize(true)
                    val adapter = Adapter(listData)
                    binding.recyclerView.adapter = adapter

                    adapter.setOnItemClick(object : OnItemClickCallback {
                        override fun onItemClicked(user: User) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra(Constant.EXTRA_USER, listUser)
                            startActivity(intent)
                        }
                    })

                } catch (e: Exception) {
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
            }

        })

    }

    private fun getSearch(username: String) {
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
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}