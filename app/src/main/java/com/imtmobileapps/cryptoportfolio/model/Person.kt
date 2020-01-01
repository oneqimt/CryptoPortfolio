package com.imtmobileapps.cryptoportfolio.model

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("person_id")
    val personId: Int?,

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("last_name")
    val lastName: String?,

    @SerializedName("quantity")
    val quantity: Float,

    @SerializedName("cost")
    val cost: Double
){
    override fun toString(): String {
        return "Person(personId=$personId, firstName=$firstName, lastName=$lastName, quantity=$quantity, cost=$cost)"
    }
}