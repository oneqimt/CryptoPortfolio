package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import io.cryptocontrol.cryptonewsapi.models.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CoinDetailViewModel(application: Application) : BaseViewModel(application){

    private val disposable = CompositeDisposable()
    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    // 1 minutes
    private var refreshTime = 1 * 60 * 1000 * 1000 * 1000L

    val cryptoLiveData = MutableLiveData<CryptoValue>()
    var totals = MutableLiveData<TotalValues>()
    var news = MutableLiveData<List<News>>()

    var articles = MutableLiveData<List<Article>>()
    val totalsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun setCrypto(crypto: CryptoValue?){

        cryptoLiveData.value = crypto

    }

    fun setNewsList(newsList: List<News>){
        news.value = newsList
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

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        println("Cached Duration is : ${cachePreference}")

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 1 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

    }


    fun storeTotalsLocally(totalValues: TotalValues){

        launch {
            val dao = CoinDatabase(getApplication())
            CoinDatabase(getApplication()).totalValuesDao().deleteAllTotalValues()
            val result: Long = dao.totalValuesDao().insertTotalValues(totalValues)
            println("TOTALS result is: ${result.toString()}")
            totalValues.uuid = result.toInt()

            totalsRetrieved(totalValues)
        }

       prefHelper.saveUpdateTime(System.nanoTime())


    }

    @Suppress("SENSELESS_COMPARISON")
    fun fetchTotalsFromDatabase(personId: Int){

        loading.value = true
        launch {
            // on first launch of app this can be null
           val totalValuesFromDatabase = CoinDatabase(getApplication()).totalValuesDao().getTotalValues(personId)
            if (totalValuesFromDatabase != null){
                totalsRetrieved(totalValuesFromDatabase)
                Toast.makeText(getApplication(), "Totals retrieved from database", Toast.LENGTH_SHORT)
                    .show()
            }else{
                println("totalValuesFromDatabase == NULL!!!")
                fetchTotalsFromRemote(personId)
            }


        }

    }

    fun totalsRetrieved(totalValues: TotalValues){
        totals.value = totalValues
        totalsLoadError.value = false
        loading.value = false
    }

    fun fetchTotalsFromRemote(personId : Int){
        loading.value = true
        disposable.add(
            cryptoService.getTotals(personId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TotalValues>() {
                    override fun onSuccess(totalValues: TotalValues) {

                        println("fetchTotalsFromRemote TOTAL VALUES are: ${totalValues.toString()}")
                        Toast.makeText(getApplication(), "Totals retrieved from REMOTE", Toast.LENGTH_SHORT)
                            .show()
                        storeTotalsLocally(totalValues)

                    }

                    override fun onError(e: Throwable) {
                        totalsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }
                })
        )

    }

    fun getCoinNews(coinName : String){
        disposable.add(
            cryptoService.getCoinNews(coinName).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Article>>(){
                    override fun onSuccess(t: List<Article>) {
                        articles.value = t
                    }

                    override fun onError(e: Throwable) {
                        e.localizedMessage
                        e.printStackTrace();
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}