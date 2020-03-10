package com.imtmobileapps.cryptoportfolio.model


import io.reactivex.Single
import retrofit2.http.*

interface CryptoApi {

    @GET("managecoins?action=personcmccoins")
    fun getPersonCoins(@Query("person_id") person_id: Int): Single<List<CryptoValue>>
    
    @POST("holdings?action=addholding")
    fun addHolding(@Body coinHolding: CoinHolding) : Single<Holdings>
    
    @POST("holdings?action=deleteholding")
    fun deleteHolding(@Body holdings: Holdings) : Single<Holdings>
    
    @POST("holdings?action=updateholding")
    fun updateHolding(@Body coinHolding: CoinHolding) : Single<Holdings>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("uname") uname: String?,
        @Field("pass") pass: String?
    ): Single<SignUp>
    
    @FormUrlEncoded
    @POST("resetpassword")
    fun resetPassword(
        @Field("email") email: String?
    ): Single<ReturnDTO>

    @POST("logout")
    fun logout() : Single<Boolean>

    @GET("totals")
    fun getTotals(@Query("person_id") person_id: Int): Single<TotalValues>

    @GET("coinnews")
    fun getCoinNews(@Query("coin_name") coin_name: String): Single<List<Article>>
    
    //https://stackoverflow.com/questions/45675527/retrofit-with-kotlin-unable-to-create-body
    @POST("signup")
    fun signUp(@Body signUp: SignUp): Single<SignUp>
    
    @GET("managecoins?action=cmccoins")
    fun getAllCoins() : Single<List<Coin>>


}