package com.ziadsyahrul.consumerapp.ItemClick

import com.ziadsyahrul.consumerapp.model.User


interface OnItemClickCallback {
    fun onItemClicked(user: User)
}
