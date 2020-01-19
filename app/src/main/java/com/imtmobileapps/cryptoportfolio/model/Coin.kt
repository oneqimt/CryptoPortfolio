package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Coin(
    @SerializedName("coin_id")
    @ColumnInfo(name = "coin_id")
    val coinId: String? = null,

    @SerializedName("coin_name")
    @ColumnInfo(name = "coin_name")
    val coinName: String? = null,

    @SerializedName("coin_symbol")
    @ColumnInfo(name = "coin_symbol")
    val coinSymbol: String? = null,

    @SerializedName("small_coin_image_url")
    @ColumnInfo(name = "small_coin_image_url")
    var smallCoinImageUrl: String? = null,

    @SerializedName("large_coin_image_url")
    @ColumnInfo(name = "large_coin_image_url")
    var largeCoinImageUrl: String? = null,

    @SerializedName("name_id")
    @ColumnInfo(name = "name_id")
    val nameId : String? = null

) : Parcelable {

    // no need for primary key here
    // Room adds columns for this object to CryptoValue
    override fun toString(): String{
        return "Coin(coinId=$coinId, coinName=$coinName, coinSymbol=$coinSymbol, smallCoinImageUrl=$smallCoinImageUrl, largeCoinImageUrl=$largeCoinImageUrl, nameId=$nameId)"
    }
}