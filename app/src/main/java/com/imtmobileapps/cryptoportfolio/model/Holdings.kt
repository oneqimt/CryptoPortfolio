package com.imtmobileapps.cryptoportfolio.model

import com.google.gson.annotations.SerializedName

data class Holdings(
    
    val holding_id : Int?,
    
    val coin_id: Int?,
    val quantity: Double?,
    val cost: Double?,
   
    val person_id: Int?
) {

}