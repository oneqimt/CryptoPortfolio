package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.BuildConfig
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ManageHoldingsViewModel(application: Application):BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private val disposable = CompositeDisposable()
    var coinsFromServer = MutableLiveData<List<Coin>>()
    val coinsLoadError = MutableLiveData<Boolean>()
    val loadingCoinsFromServer = MutableLiveData<Boolean>()
    
    fun fetchCoinsFromServer(){
        loadingCoinsFromServer.value = true
        disposable.add(
            cryptoService.getAllCoins().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Coin>>() {
                    override fun onSuccess(coinsList: List<Coin>) {
                        loadingCoinsFromServer.value = false
                        coinsLoadError.value = false
                        val updatedList = arrayListOf<Coin>()
                        for (coin :Coin in coinsList){
                            val logo = BuildConfig.CMC_LOGO_URL +coin.cmcId+".png"
                            coin.smallCoinImageUrl = logo
                            coin.largeCoinImageUrl = logo
                            
                            updatedList.add(coin)
                        }
                        
                        coinsFromServer.value = updatedList
                    }
                
                    override fun onError(e: Throwable) {
                        loadingCoinsFromServer.value = false
                        coinsLoadError.value = true
                        e.printStackTrace()
                    
                    }
                })
        )
    }
    
    
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = ManageHoldingsViewModel::class.java.simpleName
    }
    
}