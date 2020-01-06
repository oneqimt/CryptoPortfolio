package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CoinDetailViewModel(application: Application) : BaseViewModel(application){

    private val disposable = CompositeDisposable()
    private val cryptoService = CryptoApiService()

    val cryptoLiveData = MutableLiveData<CryptoValue>()
    var totals = MutableLiveData<TotalValues>()
    val totalsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun setCrypto(crypto: CryptoValue?){

        cryptoLiveData.value = crypto

    }



    fun storeTotalsLocally(){

    }

    fun fetchFromDatabase(){

    }

    fun totalsRetrieved(totalValues: TotalValues){
        totals.value = totalValues
        totalsLoadError.value = false
        loading.value = false
    }

    fun getTotals(personId : Int){
        loading.value = true
        disposable.add(
            cryptoService.getTotals(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TotalValues>() {
                    override fun onSuccess(totalValues: TotalValues) {

                    }

                    override fun onError(e: Throwable) {
                        totalsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }
                })
        )

    }

}