package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.SignUp
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private val disposable = CompositeDisposable()
    var signUpPerson = MutableLiveData<SignUp>()
    val signUpError = MutableLiveData<Boolean>()
    
    fun signUpUser(signUp: SignUp){
        disposable.add(
            cryptoService.signUpUser(signUp).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<SignUp>(){
                    override fun onSuccess(t: SignUp) {
                        println("${TAG} ${CoinApp.TEST_APP} SIGN SUCCESS and signup is: $signUp")
                        signUpPerson.value = t
                        signUpError.value = false
                    }
                    
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        println(e.localizedMessage)
                        signUpError.value = true
                    }
                }
                )
        )
    }
    
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = SignUpViewModel::class.java.simpleName
    }
    
    
    
}