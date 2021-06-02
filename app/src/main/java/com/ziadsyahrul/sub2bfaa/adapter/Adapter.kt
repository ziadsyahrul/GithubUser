package com.ziadsyahrul.sub2bfaa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ziadsyahrul.sub2bfaa.DetailActivity
import com.ziadsyahrul.sub2bfaa.ItemClick.OnItemClickCallback
import com.ziadsyahrul.sub2bfaa.User
import com.ziadsyahrul.sub2bfaa.databinding.UserListBinding

class Adapter(private val listData: ArrayList<User>): RecyclerView.Adapter<Adapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return UserViewHolder(binding)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClick(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class UserViewHolder(private val binding: UserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Picasso.get().load(user.photo).into(imageView)
                name.text = user.name
                username.text = user.user_name
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                intent.putExtra(DetailActivity.EXTRA_FAV, user)
                itemView.context.startActivity(intent)
            }

        }
    }
}