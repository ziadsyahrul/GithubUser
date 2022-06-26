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
import com.ziadsyahrul.consumerapp.adapter.FollowingAdapter
import com.ziadsyahrul.consumerapp.databinding.FragmentFollowingBinding
import com.ziadsyahrul.consumerapp.model.FavoriteModel
import com.ziadsyahrul.consumerapp.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"

    }

    private var listFollowing: ArrayList<User> = ArrayList()
    private lateinit var followingadapter: FollowingAdapter

    private var favourites: FavoriteModel? = null
    private lateinit var favoriteUser: FavoriteModel
    private lateinit var dataUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingadapter = FollowingAdapter(listFollowing)

        favourites = activity?.intent?.getParcelableExtra(DetailActivity.EXTRA_FAVORITE)!!
        if (favourites != null){
            getFollowing(activity?.intent?.getParcelableExtra<FavoriteModel>(EXTRA_FAVORITE)?.username.toString())
        } else{
            getFollowing(activity?.intent?.getParcelableExtra<User>(EXTRA_USER)?.user_name.toString())
        }
    }
    private fun getFollowing(username: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val listUser = ArrayList<User>()
                showLoading(false)
                try {

                    val array = JSONArray(String(responseBody))
                    for (i in 0 until array.length()) {
                        val item = array.getJSONObject(i)
                        val username = item.getString("login")
                        val user = User()
                        listUser.add(user)
                        getDetailFollowing(username)

                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable
            ) {
                showLoading(false)
                Log.d("GAGAL GET FOLLOWING", "onFailure: ")
            }
        })
    }

    private fun getDetailFollowing(username: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                showLoading(false)
                try {

                    val json = JSONObject(String(responseBody))
                    listFollowing.add(
                        User(
                            json.getString("login"),
                            json.getString("name"),
                            json.getString("avatar_url"),
                            json.getString("followers"),
                            json.getString("following"),
                        )
                    )

                    binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
                    binding.rvFollowing.adapter = followingadapter

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                Log.d("GAGAL_GET_DETAIL", "onFailure() ")
            }
        })
    }

    fun showLoading(state: Boolean){
        if (state){
            binding.pbFollowing.visibility = View.VISIBLE
        } else{
            binding.pbFollowing.visibility = View.GONE
        }
    }
}