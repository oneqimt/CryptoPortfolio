package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Person(
    @SerializedName("person_id")
    @ColumnInfo(name = "person_id")
    val personId: Int?,

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    val firstName: String?,

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    
    val email: String?,
    
    val phone: String?,
    
    val address: String?,
    
    val city: String?,
    
    val zip: String?,
    
    @Embedded
    val state:State?

    
):Parcelable {

    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var personuuid : Int = 0
    
    override fun toString(): String {
        return "Person(personId=$personId, firstName=$firstName, lastName=$lastName, email=$email, phone=$phone, address=$address, city=$city, zip=$zip, state=$state, personuuid=$personuuid)"
    }
    
    
}