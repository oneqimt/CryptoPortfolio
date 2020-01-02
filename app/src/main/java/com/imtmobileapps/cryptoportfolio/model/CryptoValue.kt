package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CryptoValue(
    val USD: String,
    val coin: Coin,
    val holdingValue: String,
    val percentage: String,
    val cost: String,
    val increaseDecrease: String,
    val quantity: Double
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var uuid: Int = 0

    override fun toString(): String {
        return "CryptoValue(USD='$USD', coin=$coin, holdingValue='$holdingValue', percentage='$percentage', cost='$cost', increaseDecrease='$increaseDecrease', quantity=$quantity, uuid=$uuid)"
    }


}
