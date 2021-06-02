package com.ziadsyahrul.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FavoriteModel(
    var username: String? = null,
    var name: String? = null,
    var photo: String? = null,
    var company: String? = null,
    var location: String? = null,
    var repo: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var isFav: String? = null

): Parcelable