package com.ziadsyahrul.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ziadsyahrul.consumerapp.databinding.UserListBinding
import com.ziadsyahrul.consumerapp.model.User


class FollowersAdapter(private val listData: ArrayList<User>): RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class FollowersViewHolder(private val binding: UserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Picasso.get().load(user.photo).into(imageView)
                name.text = user.name
                username.text = user.user_name
            }
        }
    }
}