package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class CryptoValue(
    @ColumnInfo(name = "coin_current_price_usd")
    val USD: String,
    @Embedded
    val coin: Coin,
    @ColumnInfo(name = "holding_value")
    val holdingValue: String,
    val percentage: String,
    val cost: String,
    @ColumnInfo(name = "increase_decrease")
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
