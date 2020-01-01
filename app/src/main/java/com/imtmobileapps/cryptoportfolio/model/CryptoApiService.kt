package com.imtmobileapps.cryptoportfolio.model

import com.imtmobileapps.cryptoportfolio.BuildConfig.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApiService {

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CryptoApi::class.java)

    fun getPersonCoins(personId: Int) : Single<List<CryptoValue>> {

        return api.getPersonCoins(personId)

    }
}