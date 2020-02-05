package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Auth(
    val auth_id: Int?,
    var username: String,
    var password: String,
    val person_id: Int?,
    val role:String?,
    val enabled: Int?
) :Parcelable{


}