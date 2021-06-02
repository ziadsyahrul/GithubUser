package com.ziadsyahrul.sub2bfaa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var user_name: String? = null,
    var name: String? = null,
    var photo: String? = null,
    var company: String? = null,
    var location: String? = null,
    var repo: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var isFav: String? = null

) : Parcelable