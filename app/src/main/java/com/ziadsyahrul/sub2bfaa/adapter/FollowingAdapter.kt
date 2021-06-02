package com.ziadsyahrul.sub2bfaa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ziadsyahrul.sub2bfaa.User
import com.ziadsyahrul.sub2bfaa.databinding.UserListBinding

class FollowingAdapter(private val listData: ArrayList<User>): RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class FollowingViewHolder(private val binding: UserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Picasso.get().load(user.photo).into(imageView)
                name.text = user.name
                username.text = user.user_name
            }
        }
    }
}