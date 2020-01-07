package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity
data class TotalValues(

    @ColumnInfo(name = "person_id")
    val personId: Int?,

    @ColumnInfo(name = "total_cost")
    val totalCost: String?,

    @ColumnInfo(name = "total_value")
    val totalValue: String?,

    @SerializedName("totalPercentageIncreaseDecrease")
    @ColumnInfo(name = "total_change")
    val totalChange: String?,

    @ColumnInfo(name = "increase_decrease")
    val increaseDecrease: String?
) {

    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

    override fun toString(): String {
        return "TotalValues(personId=$personId, totalCost=$totalCost, totalValue=$totalValue, totalChange=$totalChange, increaseDecrease=$increaseDecrease, uuid=$uuid)"
    }


}