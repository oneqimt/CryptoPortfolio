package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CryptoValue

class CoinDetailViewModel(application: Application) : BaseViewModel(application){

    val cryptoLiveData = MutableLiveData<CryptoValue>()

    fun setCrypto(crypto: CryptoValue?){

        cryptoLiveData.value = crypto

    }

}