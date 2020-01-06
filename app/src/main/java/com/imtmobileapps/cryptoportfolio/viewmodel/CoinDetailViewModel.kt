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

    fun setCrypto(crypto: CryptoValue?){

        cryptoLiveData.value = crypto

    }

    fun logout(){
        cryptoService.logout().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Boolean>(){
                override fun onSuccess(t: Boolean) {
                    println("LOGOUT and returned value is: $t")
                }

                override fun onError(e: Throwable) {

                }
            })

    }

    fun getTotals(personId : Int){

        disposable.add(
            cryptoService.getTotals(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TotalValues>() {
                    override fun onSuccess(totalValues: TotalValues) {
                        Toast.makeText(
                            getApplication(),
                            "TOTAL VALUES retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()

                        totals.value = totalValues

                        println("TOTAL VALUES are : $totalValues")

                    }

                    override fun onError(e: Throwable) {
                        /*cryptosLoadError.value = true
                        loading.value = false*/
                        e.printStackTrace()

                    }
                })
        )

    }

}