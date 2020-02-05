package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    private val disposable = CompositeDisposable()
    var user = MutableLiveData<Person>()
    val loginError = MutableLiveData<Boolean>()
    var auth : Auth? = null
    
    
    fun loginUser(username: String, password: String) {

        disposable.add(
            cryptoService.login(username, password).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SignUp>() {
                    override fun onSuccess(signUp: SignUp) {

                        prefHelper.savePersonId(signUp.person.personId!!)
                        auth = signUp.auth
                        loginError.value = false
                        user.value = signUp.person
                        println("$TAG ${CoinApp.TEST_APP} PERSON is ${signUp.person}")
                        cacheUser(signUp.person)
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
    
    
    @Suppress("SENSELESS_COMPARISON")
    fun cacheUser(person: Person) {
        launch {
            val dao = CoinDatabase(getApplication())
            val cachedPerson = CoinDatabase(getApplication()).personDao().getPerson(person.personId!!)
            if (cachedPerson == null){
                CoinDatabase(getApplication()).personDao().deletePerson()
                val result: Long = dao.personDao().insertPerson(person)
                println("$TAG ${CoinApp.TEST_APP} PERSON CACHED result is: ${result.toString()}")
                person.personuuid = result.toInt()
                println("$TAG ${CoinApp.TEST_APP} PERSON in database IS:  $person")
            }else{
                println("$TAG ${CoinApp.TEST_APP} USER ALREADY EXISTS!!!")
            }

        }

    }

    fun goToMainActivity() {

        val intent = Intent(getApplication(), MainActivity::class.java)
        val person : Person? = user.value
        intent.putExtra("user", person)
        if (auth != null){
            intent.putExtra("auth", auth)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(getApplication(), intent, null)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

}