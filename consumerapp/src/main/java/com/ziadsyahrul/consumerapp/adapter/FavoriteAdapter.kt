package com.ziadsyahrul.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ziadsyahrul.consumerapp.DetailActivity
import com.ziadsyahrul.consumerapp.ItemClick.OnItemClickListener
import com.ziadsyahrul.consumerapp.databinding.UserListBinding
import com.ziadsyahrul.consumerapp.model.FavoriteModel

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<FavoriteModel>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    inner class FavoriteViewHolder(private val binding: UserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteModel) {
            with(binding) {
                username.text = favorite.username
                name.text = favorite.name
                Picasso.get().load(favorite.photo).into(imageView)
                itemView.setOnClickListener(
                    OnItemClickListener(
                        adapterPosition,
                        object : OnItemClickListener.ItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_POSISI, position)
                                intent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)
                                activity.startActivity(intent)
                            }

                        })
                )

            }
        }
    }
}