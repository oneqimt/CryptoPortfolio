package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.BuildConfig
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CryptoListViewModel(application: Application) : BaseViewModel(application) {

    // 5 minutes
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val cryptoService = CryptoApiService()
    // observes the OBSERVABLE that the api gives us - the SINGLE
    // helps with memory leaks (reactiveX)
    private val disposable = CompositeDisposable()

    var coins = MutableLiveData<List<CryptoValue>>()
    val cryptosLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(personId: Int){
        fetchFromRemote(personId)
    }

    fun fetchFromRemote(personId : Int) {
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

                        for (crypto in cryptosList){
                            var coinSymbol = crypto.coin.coinSymbol?.toLowerCase()
                            var url = BuildConfig.COIN_IMAGE_URL +"icon/$coinSymbol/50"

                            crypto.coin.coinImageUrl = url
                        }

                        coins.value = cryptosList
                        loading.value = false


                        // NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        cryptosLoadError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }
                })
        )
    }



}