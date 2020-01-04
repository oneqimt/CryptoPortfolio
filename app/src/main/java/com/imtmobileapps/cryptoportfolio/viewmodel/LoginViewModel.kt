package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.Person
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseViewModel(application){

    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    private val disposable = CompositeDisposable()
    var user  = MutableLiveData<Person>()
    val loginError = MutableLiveData<Boolean>()

    fun loginUser(username: String, password: String){

        disposable.add(
            cryptoService.login(username, password).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Person>() {
                    override fun onSuccess(person: Person) {
                        Toast.makeText(
                            getApplication(),
                            "Person retrieved",
                            Toast.LENGTH_SHORT
                        ).show()

                        prefHelper.savePersonId(person.personId)
                        loginError.value = false
                        cacheUser(person)

                        goToMainActivity()

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loginError.value = true
                        println(e.localizedMessage)

                    }
                })
        )

    }

    fun cacheUser(user: Person){
        this.user.value = user
    }

    fun goToMainActivity(){

        val intent = Intent(getApplication(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
        startActivity(getApplication(), intent, null)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}