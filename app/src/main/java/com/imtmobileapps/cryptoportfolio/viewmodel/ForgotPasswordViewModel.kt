package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.ReturnDTO
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.view.LoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private val disposable = CompositeDisposable()
    var returnDTO = MutableLiveData<ReturnDTO>()
    val resetPasswordError = MutableLiveData<Boolean>()
    
    fun resetPassword(email: String) {
        
        disposable.add(
            cryptoService.resetPassword(email).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReturnDTO>() {
                    override fun onSuccess(t: ReturnDTO) {
                        
                        returnDTO.value = t
                        
                        println("$TAG ${CoinApp.TEST_APP} in resetPassword success and t is: $t")
                        
                        resetPasswordError.value = false
                        
                        goToLoginActivity()
                        
                    }
                    
                    override fun onError(e: Throwable) {
                        resetPasswordError.value = true
                    }
                })
        
        )
        
    }
    
    fun goToLoginActivity() {
        
        val intent = Intent(getApplication(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(getApplication(), intent, null)
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    
    companion object {
        private val TAG = ForgotPasswordViewModel::class.java.simpleName
    }
}