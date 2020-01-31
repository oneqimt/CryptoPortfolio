package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
class State(
    val id:Int?=null,
    val name:String?=null,
    val country:String?=null,
    val abbreviation:String?=null
):Parcelable {
    
    // no need for primary key here
    
    override fun toString(): String {
        return "State(id=$id, name=$name, country=$country, abbreviation=$abbreviation)"
    }
}