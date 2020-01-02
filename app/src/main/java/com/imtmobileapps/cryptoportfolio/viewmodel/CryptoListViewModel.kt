package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.BuildConfig
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CoinDatabase
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.util.*

class CryptoListViewModel(application: Application) : BaseViewModel(application) {

    // 1 minutes
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
        //checkCacheDuration()
        val updateTime: Long? = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote(personId)
        }

    }

    fun refreshBypassCache(personId: Int) {
        fetchFromRemote(personId)
    }



    fun coinsRetrieved(coinsList: List<CryptoValue>){
        coins.value = coinsList
        cryptosLoadError.value = false
        loading.value = false
    }

    fun storeCoinsLocally(list: List<CryptoValue>){
        // coroutines

        launch {
            val dao = CoinDatabase(getApplication())
            CoinDatabase(getApplication()).coinDao().deleteAllCoins()
            val result: List<Long> = dao.coinDao().insertAll(*list.toTypedArray())
            println("DATABASE result is: ${result.toString()}")
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i

            }

            coinsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())


    }

    // Need to add Settings - THROWS a numberformatexception
    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

    }

    fun fetchFromDatabase(){
        loading.value = true
        launch {

            val personCoins = CoinDatabase(getApplication()).coinDao().getPersonCoins()
            coinsRetrieved(personCoins)
            Toast.makeText(getApplication(), "Coins retrieved from database", Toast.LENGTH_SHORT)
                .show()

        }

    }

    fun fetchFromRemote(personId: Int) {
        loading.value = true
        disposable.add(
            cryptoService.getPersonCoins(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CryptoValue>>() {
                    override fun onSuccess(cryptosList: List<CryptoValue>) {
                        Toast.makeText(
                            getApplication(),
                            "Coins retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()

                        // add the coin symbol here
                        for (crypto in cryptosList) {
                            val coinSymbol = crypto.coin.coinSymbol?.toLowerCase(Locale.getDefault())
                            val smallurl = BuildConfig.COIN_IMAGE_URL + "icon/$coinSymbol/50"
                            val largeurl = BuildConfig.COIN_IMAGE_URL + "icon/$coinSymbol/100"

                            crypto.coin.smallCoinImageUrl = smallurl
                            crypto.coin.largeCoinImageUrl = largeurl
                        }
                        storeCoinsLocally(cryptosList)

                    }

                    override fun onError(e: Throwable) {
                        cryptosLoadError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}