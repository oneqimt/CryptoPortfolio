package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    val _id : String?,
    val name : String?,
    val url : String
) : Parcelable{
}