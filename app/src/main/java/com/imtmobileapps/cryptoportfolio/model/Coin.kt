package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coin(
    @SerializedName("coin_id")
    val coinId: String? = null,

    @SerializedName("coin_name")
    val coinName: String? = null,

    @SerializedName("coin_symbol")
    val coinSymbol: String? = null,

    var smallCoinImageUrl: String? = null,

    var largeCoinImageUrl: String? = null

) : Parcelable {

    override fun toString(): String {
        return "Coin(coinId=$coinId, coinName=$coinName, coinSymbol=$coinSymbol, coinImageUrl=$smallCoinImageUrl)"
    }
}