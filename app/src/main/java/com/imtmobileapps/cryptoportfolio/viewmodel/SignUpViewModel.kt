package com.imtmobileapps.cryptoportfolio.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
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

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    
    private val cryptoService = CryptoApiService()
    private var prefHelper = PreferencesHelper(getApplication())
    private val disposable = CompositeDisposable()
    var signUpPerson = MutableLiveData<SignUp>()
    val signUpError = MutableLiveData<Boolean>()
    
    fun signUpUser(signUp: SignUp) {
        disposable.add(
            cryptoService.signUpUser(signUp).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SignUp>() {
                    override fun onSuccess(t: SignUp) {
                        println("${TAG} ${CoinApp.TEST_APP} SIGN SUCCESS and signup is: $signUp")
                        signUpPerson.value = t
                        signUpError.value = false
                        prefHelper.savePersonId(t.person.personId!!)
                        cacheUser(t.person)
                        goToMainActivity()
                    }
                    
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        println("$TAG ${CoinApp.TEST_APP} ERROR sign up is: ${e.localizedMessage}")
                        signUpError.value = true
                    }
                })
        )
    }
    
    fun goToMainActivity() {
        
        val intent = Intent(getApplication(), MainActivity::class.java)
        val person : Person? = signUpPerson.value?.person
        val auth : Auth? = signUpPerson.value?.auth
        intent.putExtra("user", person)
        intent.putExtra("auth", auth)
        
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(getApplication(), intent, null)
    }
    
    @Suppress("SENSELESS_COMPARISON")
    fun cacheUser(person: Person) {
        launch {
            val dao = CoinDatabase(getApplication())
            val cachedPerson = CoinDatabase(getApplication()).personDao().getPerson(person.personId!!)
            if (cachedPerson == null){
                CoinDatabase(getApplication()).personDao().deletePerson()
                val result: Long = dao.personDao().insertPerson(person)
                println("${TAG} ${CoinApp.TEST_APP} PERSON CACHED result is: ${result.toString()}")
                person.personuuid = result.toInt()
                println("${TAG} ${CoinApp.TEST_APP} PERSON in database IS:  $person")
            }else{
                println("${TAG} ${CoinApp.TEST_APP} USER ALREADY EXISTS!!!")
            }
            
        }
        
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    
    companion object {
        private val TAG = SignUpViewModel::class.java.simpleName
    }
    
    
}