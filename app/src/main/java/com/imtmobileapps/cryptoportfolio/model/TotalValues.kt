package com.imtmobileapps.cryptoportfolio.model

import com.google.gson.annotations.SerializedName

data class TotalValues(

    val totalCost: String?,
    val totalValue: String?,

    @SerializedName("totalPercentageIncreaseDecrease")
    val totalChange: String?,

    val increaseDecrease: String?
) {

    override fun toString(): String {
        return "TotalValues(totalCost=$totalCost, totalValue=$totalValue, totalChange=$totalChange, increaseDecrease=$increaseDecrease)"
    }
}