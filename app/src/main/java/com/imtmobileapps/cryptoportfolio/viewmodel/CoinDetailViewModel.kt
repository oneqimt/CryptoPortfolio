package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CoinDetailViewModel(application: Application) : BaseViewModel(application) {
    
    private val disposable = CompositeDisposable()
    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    // 1 minutes
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L
    
    val cryptoLiveData = MutableLiveData<CryptoValue>()
    var totals = MutableLiveData<TotalValues>()
    var articles = MutableLiveData<List<Article>>()
    val totalsLoadError = MutableLiveData<Boolean>()
    val totalsLoading = MutableLiveData<Boolean>()
    var newsLoading = MutableLiveData<Boolean>()
    var newsLoadError = MutableLiveData<Boolean>()
    
    fun setCrypto(crypto: CryptoValue?) {
        
        cryptoLiveData.value = crypto
        
    }
    
    fun refresh(personId: Int) {
        
        checkCacheDuration()
        val updateTime: Long? = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchTotalsFromDatabase(personId)
        } else {
            fetchTotalsFromRemote(personId)
        }
    }
    
    fun refreshNews() {
        articles.value = CoinApp.newsList
    }
    
    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        println("$TAG TESTAPP Cached Duration is : ${cachePreference}")
        
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 1 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        
    }
    
    
    fun storeTotalsLocally(totalValues: TotalValues) {
        
        launch {
            val dao = CoinDatabase(getApplication())
            CoinDatabase(getApplication()).totalValuesDao().deleteAllTotalValues()
            val result: Long = dao.totalValuesDao().insertTotalValues(totalValues)
            println("$TAG TESTAPP TOTALS result is: ${result.toString()}")
            totalValues.uuid = result.toInt()
            
            totalsRetrieved(totalValues)
        }
        
        prefHelper.saveUpdateTime(System.nanoTime())
        
        
    }
    
    @Suppress("SENSELESS_COMPARISON")
    fun fetchTotalsFromDatabase(personId: Int) {
        
        totalsLoading.value = true
        launch {
            // on first launch of app this can be null
            val totalValuesFromDatabase =
                CoinDatabase(getApplication()).totalValuesDao().getTotalValues(personId)
            if (totalValuesFromDatabase != null) {
                totalsRetrieved(totalValuesFromDatabase)
                println("$TAG TESTAPP Totals retrieved from database")
            } else {
                println("$TAG TESTAPP totalValuesFromDatabase == NULL so get from remote")
                fetchTotalsFromRemote(personId)
            }
        }
        
    }
    
    private fun totalsRetrieved(totalValues: TotalValues) {
        totals.value = totalValues
        totalsLoadError.value = false
        totalsLoading.value = false
        
    }
    
    private fun fetchTotalsFromRemote(personId: Int) {
        totalsLoading.value = true
        disposable.add(
            cryptoService.getTotals(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TotalValues>() {
                    override fun onSuccess(totalValues: TotalValues) {
                        println("$TAG TESTAPP fetchTotalsFromRemote TOTAL VALUES are: ${totalValues.toString()}")
                        storeTotalsLocally(totalValues)
                    }
                    
                    override fun onError(e: Throwable) {
                        totalsLoadError.value = true
                        totalsLoading.value = false
                        e.printStackTrace()
                        
                    }
                })
        )
        
    }
    
    fun getCoinNews(coinName: String) {
        newsLoading.value = true
        newsLoadError.value = false
        CoinApp.fromWeb = false
        disposable.add(
            cryptoService.getCoinNews(coinName).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Article>>() {
                    override fun onSuccess(t: List<Article>) {
                        println("$TAG TESTAPP in getCoinNews SUCCESS ${t.size}")
                        if (t.isNotEmpty()) {
                            newsLoading.value = false
                            newsLoadError.value = false
                            articles.value = t
                        } else {
                            newsLoadError.value = true
                            articles.value = null
                        }
                        
                    }
                    
                    override fun onError(e: Throwable) {
                        println("$TAG TESTAPP in getCoinNews Error : ${e.localizedMessage}")
                        newsLoadError.value = true
                        articles.value = null
                        
                    }
                })
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = CoinDetailViewModel::class.java.simpleName
    }
    
    
}