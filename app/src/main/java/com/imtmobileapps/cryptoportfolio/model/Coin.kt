package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.imtmobileapps.cryptoportfolio.util.BigDecimalDoubleTypeConverter
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
data class Coin(
    @SerializedName("coin_id")
    @ColumnInfo(name = "coin_id")
    val coinId: Int? = null,

    @SerializedName("coin_name")
    @ColumnInfo(name = "coin_name")
    val coinName: String? = null,

    @SerializedName("coin_symbol")
    @ColumnInfo(name = "coin_symbol")
    val coinSymbol: String? = null,
    
    @SerializedName("cmc_id")
    @ColumnInfo(name = "cmc_id")
    val cmcId : Int? = null,
    
    @SerializedName("slug")
    @ColumnInfo(name = "slug")
    val slug : String? = null,

    @SerializedName("small_coin_image_url")
    @ColumnInfo(name = "small_coin_image_url")
    var smallCoinImageUrl: String? = null,

    @SerializedName("large_coin_image_url")
    @ColumnInfo(name = "large_coin_image_url")
    var largeCoinImageUrl: String? = null,
    
    @SerializedName("market_cap")
    @ColumnInfo(name = "market_cap")
    val marketCap: BigDecimal? = null,
    
    @SerializedName("cmc_rank")
    @ColumnInfo(name = "cmc_rank")
    val cmcRank : Int? = null,
    
    @SerializedName("currentPrice")
    @ColumnInfo(name = "current_price")
    val currentPrice : BigDecimal? = null
    
) : Parcelable {

    // no need for primary key here
    // Room adds columns for this object to CryptoValue
    override fun toString(): String{
        return "Coin(coinId=$coinId, coinName=$coinName, coinSymbol=$coinSymbol, cmcId=$cmcId, slug=$slug, smallCoinImageUrl=$smallCoinImageUrl, largeCoinImageUrl=$largeCoinImageUrl, marketCap=$marketCap, cmcRank=$cmcRank, currentPrice=$currentPrice)"
    }
}