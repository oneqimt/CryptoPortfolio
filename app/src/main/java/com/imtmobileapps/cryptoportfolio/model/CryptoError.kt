package com.imtmobileapps.cryptoportfolio.model

data class CryptoError(
    val  errorId:Int?,
    val errorName:String?,
    val errorDescription: String
) {
    override fun toString(): String {
        return "CryptoError(errorId=$errorId, errorName=$errorName, errorDescription='$errorDescription')"
    }
}