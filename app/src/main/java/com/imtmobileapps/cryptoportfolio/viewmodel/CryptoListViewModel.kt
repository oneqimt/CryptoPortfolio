package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.BuildConfig
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CoinDatabase
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CryptoListViewModel(application: Application) : BaseViewModel(application) {
    
    // 1 minute
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L
    
    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    
    // observes the OBSERVABLE (the SINGLE) that the api gives us
    // guards against memory leaks (reactiveX)
    private val disposable = CompositeDisposable()
    
    var coins = MutableLiveData<List<CryptoValue>>()
    val cryptosLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    
    fun refresh(personId: Int) {
        checkCacheDuration()
        val updateTime: Long? = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote(personId)
        }
        
    }
    
    fun refreshBypassCache(personId: Int) {
        println("$TAG ${CoinApp.TEST_APP} in refreshBypassCache")
        fetchFromRemote(personId)
    }
    
    fun coinsRetrieved(coinsList: List<CryptoValue>) {
        coins.value = coinsList
        cryptosLoadError.value = false
        loading.value = false
    }
    
    fun storeCoinsLocally(list: List<CryptoValue>) {
        // coroutines
        
        launch {
            val dao = CoinDatabase(getApplication())
            CoinDatabase(getApplication()).coinDao().deleteAllCoins()
            val result: List<Long> = dao.coinDao().insertAll(*list.toTypedArray())
            println("$TAG ${CoinApp.TEST_APP} DATABASE result is: ${result.toString()}")
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
                
            }
            
            val person = CoinDatabase(getApplication()).personDao()
                .getPerson(prefHelper.getCurrentPersonId()!!)
            println("$TAG ${CoinApp.TEST_APP} ListViewModel DATABASE CACHE and person is $person")
            
            coinsRetrieved(list)
        }
        
        prefHelper.saveUpdateTime(System.nanoTime())
        
    }
    
    fun checkIfPersonHasHoldings(): Boolean {
        
        var holdings: List<CryptoValue> = arrayListOf()
        launch {
            holdings = CoinDatabase(getApplication()).coinDao().getPersonCoins()
            
        }
        
        println("$TAG ${CoinApp.TEST_APP} holdings is database length ${holdings.size}")
        
        return holdings.isNotEmpty()
        
    }
    
    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        println("$TAG ${CoinApp.TEST_APP} Cached Duration is : ${cachePreference}")
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 1 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        
    }
    
    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            
            val personCoins = CoinDatabase(getApplication()).coinDao().getPersonCoins()
            coinsRetrieved(personCoins)
            
        }
        
    }
    
    private fun fetchFromRemote(personId: Int) {
        loading.value = true
        disposable.add(
            cryptoService.getPersonCoins(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CryptoValue>>() {
                    override fun onSuccess(cryptosList: List<CryptoValue>) {
                        // if the user does not have any coins yet
                        if (cryptosList.isEmpty()) {
                            println("$TAG ${CoinApp.TEST_APP} getPersonCoins and cryptosList is EMPTY $cryptosList")
                            coinsRetrieved(cryptosList)
                        } else {
                            // add the coin symbol here
                            for (crypto in cryptosList) {
                                val logo = BuildConfig.CMC_LOGO_URL + crypto.coin.cmcId + ".png"
                                crypto.coin.smallCoinImageUrl = logo
                                crypto.coin.largeCoinImageUrl = logo
                            }
                            
                            // NOTE filter the list by coin.cmcRank, the only problem is that we have a CryptoValue obj to work with, we need a Coin obj
                            storeCoinsLocally(cryptosList)
                        }
                        
                    }
                    
                    override fun onError(e: Throwable) {
                        cryptosLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }
    
    fun clearDatabase(){
        launch {
            CoinDatabase(getApplication()).coinDao().deleteAllCoins()
            println("$TAG ${CoinApp.TEST_APP} LOGOUT clearDatabase() deleteAllCoins()")
        }
    }
    
    fun logout() {
        disposable.add(
            cryptoService.logout().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(t: Boolean) {
                        if (t) {
                            println("$TAG ${CoinApp.TEST_APP} LOGOUT and returned value is: $t")
                            clearDatabase()
                        }
                    }
                    
                    override fun onError(e: Throwable) {
                        
                        println(e.localizedMessage)
                    }
                })
        )
        
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = CryptoListViewModel::class.java.simpleName
    }
    
    
}