package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.imtmobileapps.cryptoportfolio.model.CoinDatabase
import com.imtmobileapps.cryptoportfolio.model.CryptoApiService
import com.imtmobileapps.cryptoportfolio.model.Person
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

    fun loginUser(username: String, password: String) {

        disposable.add(
            cryptoService.login(username, password).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Person>() {
                    override fun onSuccess(person: Person) {

                        prefHelper.savePersonId(person.personId!!)
                        loginError.value = false
                        user.value = person
                        println("$TAG TESTAPP PERSON is ${person}")
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

    @Suppress("SENSELESS_COMPARISON")
    fun cacheUser(person: Person) {
        launch {
            val dao = CoinDatabase(getApplication())
            val cachedPerson = CoinDatabase(getApplication()).personDao().getPerson(person.personId!!)
            if (cachedPerson == null){
                CoinDatabase(getApplication()).personDao().deletePerson()
                val result: Long = dao.personDao().insertPerson(person)
                println("$TAG TESTAPP PERSON CACHED result is: ${result.toString()}")
                person.personuuid = result.toInt()
                println("$TAG TESTAPP PERSON in database IS:  $person")
            }else{
                println("$TAG TESTAPP USER ALREADY EXISTS!!!")
            }

        }

    }

    fun goToMainActivity() {

        val intent = Intent(getApplication(), MainActivity::class.java)
        val person : Person? = user.value
        intent.putExtra("user", person)
        
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