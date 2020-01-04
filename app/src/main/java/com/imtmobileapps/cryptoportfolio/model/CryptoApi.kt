package com.imtmobileapps.cryptoportfolio.model

import io.reactivex.Single
import retrofit2.http.*

interface CryptoApi {

    @GET("holdings")
    fun getPersonCoins(@Query("person_id") person_id: Int): Single<List<CryptoValue>>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("uname") uname: String?,
        @Field("pass") pass: String?
    ): Single<Person>


}