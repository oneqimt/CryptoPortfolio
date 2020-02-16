package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.BuildConfig
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.Holdings
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class ManageHoldingsViewModel(application: Application):BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    
    private val disposable = CompositeDisposable()
    var coinsFromServer = MutableLiveData<List<Coin>>()
    val coinsLoadError = MutableLiveData<Boolean>()
    val loadingCoinsFromServer = MutableLiveData<Boolean>()
    
    
    fun addHolding(holding: Holdings) {
        disposable.add(
            cryptoService.addHolding(holding).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<Holdings>() {
                override fun onSuccess(t: Holdings) {
                    
                    println("${TAG} ${CoinApp.TEST_APP}, In addHolding success and holding is: $t")
                    
                }
                
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    println("${TAG} ${CoinApp.TEST_APP}, In addHolding ERROR and error is: ${e.localizedMessage}")
                }
            }
            ))
    }
    
    fun fetchCoinsFromServer(){
    
        disposable.add(
            cryptoService.getAllCoins().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Coin>>() {
                    override fun onSuccess(coinsList: List<Coin>) {
                        println("$TAG ${CoinApp.TEST_APP} in getAllCoins() SUCCESS")
                        val updatedList = arrayListOf<Coin>()
                        for (coin :Coin in coinsList){
                            
                            // NOTE: we may have to get these FROM COIN MARKET CAP AS WELL - do on server
                            //METADATA CALL - has image icons IF NEEDED
                            //"logo": "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png",
                            val coinSymbol = coin.coinSymbol?.toLowerCase(Locale.getDefault())
                            val smallurl = BuildConfig.COIN_IMAGE_URL + "icon/$coinSymbol/40"
                            val largeurl = BuildConfig.COIN_IMAGE_URL + "icon/$coinSymbol/60"
    
                            coin.smallCoinImageUrl = smallurl
                            coin.largeCoinImageUrl = largeurl
                            
                            updatedList.add(coin)
                            
                        }
                        
                        for (coin: Coin in updatedList){
                            println("$TAG ${CoinApp.TEST_APP} UPDATED LIST COIN is: $coin")
                        }
    
                        coinsFromServer.value = updatedList
                    }
                
                    override fun onError(e: Throwable) {
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