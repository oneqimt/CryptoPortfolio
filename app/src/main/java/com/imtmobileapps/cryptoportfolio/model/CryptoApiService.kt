package com.imtmobileapps.cryptoportfolio.model

import com.google.gson.GsonBuilder
import com.imtmobileapps.cryptoportfolio.BuildConfig.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CryptoApiService {

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CryptoApi::class.java)

    fun getPersonCoins(personId: Int) : Single<List<CryptoValue>> {

        return api.getPersonCoins(personId)

    }

    fun login(uname: String, pass : String) : Single<Person>{

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

}