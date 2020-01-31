package com.imtmobileapps.cryptoportfolio.model

data class Auth(
    val auth_id: Int?,
    val username: String?,
    val password: String?,
    val person_id: Int?,
    val role:String?,
    val enabled: Int?
) {
}