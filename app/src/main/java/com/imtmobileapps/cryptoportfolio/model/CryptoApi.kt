package com.imtmobileapps.cryptoportfolio.model

import io.cryptocontrol.cryptonewsapi.models.Article
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

    @POST("logout")
    fun logout() : Single<Boolean>

    @GET("totals")
    fun getTotals(@Query("person_id") person_id: Int): Single<TotalValues>

    @GET("coinnews")
    fun getCoinNews(@Query("coin_name") coin_name: String): Single<List<Article>>


}