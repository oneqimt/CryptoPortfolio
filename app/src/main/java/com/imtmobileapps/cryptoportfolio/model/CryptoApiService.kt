package com.imtmobileapps.cryptoportfolio.model

import com.google.gson.GsonBuilder
import com.imtmobileapps.cryptoportfolio.BuildConfig.BASE_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CryptoApiService {

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient().build())
        .build()
        .create(CryptoApi::class.java)
    
    
    // could add a listener for download progress as well
    // https://stackoverflow.com/questions/42118924/android-retrofit-download-progress/42119419#42119419
    private fun getOkHttpClient():OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(1, TimeUnit.SECONDS)
        builder.readTimeout(5, TimeUnit.MINUTES)
        
        
        return builder
    }

    fun getPersonCoins(personId: Int) : Single<List<CryptoValue>> {

        return api.getPersonCoins(personId)

    }
    
    fun addHolding(coinHolding: CoinHolding) : Single<Holdings>{
        
        return api.addHolding(coinHolding)
    }
    
    fun deleteHolding(holding: Holdings) : Single<Holdings>{
        return api.deleteHolding(holding)
    }
    
    fun updateHolding(coinHolding: CoinHolding) : Single<Holdings>{
        return api.updateHolding(coinHolding)
    }

    fun login(uname: String, pass : String) : Single<SignUp>{

        return api.login(uname, pass)

    }

    fun logout() : Single<Boolean>{
        return api.logout()
    }

    fun getTotals(personId: Int) : Single<TotalValues>{
        return api.getTotals(personId)
    }

    fun getCoinNews(coinName: String) : Single<List<Article>> {

        return api.getCoinNews(coinName)

    }
    
    fun signUpUser(signUp: SignUp): Single<SignUp>{
        return api.signUp(signUp)
    }
    
    fun resetPassword(email: String): Single<ReturnDTO>{
        return api.resetPassword(email)
    }
    
    fun getAllCoins() :Single<List<Coin>>{
        return api.getAllCoins()
    }

}