package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.CoinApp.TEST_APP
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class AddFormViewModel(application: Application) : BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private val disposable = CompositeDisposable()
    var coins = MutableLiveData<List<CryptoValue>>()
    
    fun getPersonHoldingsFromDatabase(): List<CryptoValue> {
        
        var dbholdings = arrayListOf<CryptoValue>()
        
        launch {
            dbholdings =
                CoinDatabase(getApplication()).coinDao().getPersonCoins() as ArrayList<CryptoValue>
            println("${TAG} ${TEST_APP} holdings is database length ${dbholdings.size}")
            coins.value = dbholdings
            
        }
        
        return dbholdings
        
    }
    
    fun addHolding(coinHolding: CoinHolding) {
        disposable.add(
            cryptoService.addHolding(coinHolding).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<Holdings>() {
                override fun onSuccess(t: Holdings) {
                    
                    println("$TAG ${TEST_APP}, In addHolding success and holding is: $t")
                    
                }
                
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    println("$TAG ${TEST_APP}, In addHolding ERROR and error is: ${e.localizedMessage}")
                }
            }
            ))
    }
    
    fun updateHolding(coinHolding: CoinHolding){
        disposable.add(
            cryptoService.updateHolding(coinHolding).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()
            
            ).subscribeWith(object : DisposableSingleObserver<Holdings>(){
                override fun onSuccess(t: Holdings) {
                    println("$TAG ${TEST_APP}, In updateHolding success and holding is: $t")
                }
    
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    println("$TAG ${TEST_APP}, In updateHolding ERROR and error is: ${e.localizedMessage}")
                }
    
            })
        )
    }
    
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = AddFormViewModel::class.java.simpleName
    }
}