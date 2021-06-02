package com.ziadsyahrul.sub2bfaa.ItemClick

import android.view.View

class OnItemClickListener (
    private val position: Int,
    private val onItemClickCallBack: ItemClickCallback
        ): View.OnClickListener {
    override fun onClick(p0: View?) {
        onItemClickCallBack.onItemClicked(p0!!, position)
    }


    interface ItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}