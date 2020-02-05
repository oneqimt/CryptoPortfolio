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
    ): Single<SignUp>

    @POST("logout")
    fun logout() : Single<Boolean>

    @GET("totals")
    fun getTotals(@Query("person_id") person_id: Int): Single<TotalValues>

    @GET("coinnews")
    fun getCoinNews(@Query("coin_name") coin_name: String): Single<List<Article>>
    
    //https://stackoverflow.com/questions/45675527/retrofit-with-kotlin-unable-to-create-body
    @POST("signup")
    fun signUp(@Body signUp: SignUp): Single<SignUp>


}