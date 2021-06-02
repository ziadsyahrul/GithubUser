package com.ziadsyahrul.consumerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ziadsyahrul.consumerapp.adapter.FollowersAdapter
import com.ziadsyahrul.consumerapp.databinding.FragmentFollowersBinding
import com.ziadsyahrul.consumerapp.model.FavoriteModel
import com.ziadsyahrul.consumerapp.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
    }

    private var listFollower: ArrayList<User> = ArrayList()
    private lateinit var followersAdapter: FollowersAdapter
    private var favorite: FavoriteModel? = null
    private lateinit var favoriteUser: FavoriteModel
    private lateinit var dataUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersAdapter = FollowersAdapter(listFollower)

        favorite = activity?.intent?.getParcelableExtra(DetailActivity.EXTRA_FAVORITE)
        if (favorite != null){
            getFollowers(activity?.intent?.getParcelableExtra<FavoriteModel>(EXTRA_FAVORITE)?.username.toString())
        } else{
            getFollowers(activity?.intent?.getParcelableExtra<User>(EXTRA_USER)?.user_name.toString())
        }

    }

    private fun getFollowers(username: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/followers"
        client.get(
            url,
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val listUser = ArrayList<User>()
                    showLoading(false)
                    try {
                        val array = JSONArray(String(responseBody))
                        for (i in 0 until array.length()){
                            val item = array.getJSONObject(i)
                            val username = item.getString("login")
                            val user = User()
                            listUser.add(user)
                            getDetail(username)
                        }

                    } catch (e: Exception) {
                        Log.d("onSuccess", "berhasilgetfol")
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
                    Log.d("GAGALGETFOLLOWERS", "onFailure()")
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
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray
            ) {
                showLoading(false)
                val result = String(responseBody)
                Log.d("BERHASILGETDETAIL", result)
                try {

                    val json = JSONObject(String(responseBody))
                    listFollower.add(User(
                        json.getString("login"),
                        json.getString("name"),
                        json.getString("avatar_url"),
                        json.getString("followers"),
                        json.getString("following"),
                    ))

                    binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
                    binding.rvFollowers.adapter = followersAdapter

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                Log.d("GAGAL GETDETAIL", "onFailure()")
            }
        })
    }

    fun showLoading(state: Boolean){
        if (state){
            binding.pbFollowers.visibility = View.VISIBLE
        }else{
            binding.pbFollowers.visibility = View.GONE
        }
    }


}