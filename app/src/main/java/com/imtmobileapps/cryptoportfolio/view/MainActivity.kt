package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.Auth
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.IOnBackPressed
import com.imtmobileapps.cryptoportfolio.model.Person
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var navController: NavController
    private lateinit var viewModel: CoinDetailViewModel
    var selectedCoin: CryptoValue? = null
    private var person: Person? = null
    private var auth : Auth? = null
    private var personFullName = ""
    private var usernameAuth = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        
        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        
        person = intent.getParcelableExtra("user")
        auth = intent.getParcelableExtra("auth")
        
        // set default
        println("$TAG ${CoinApp.TEST_APP} user from parcelable is $person")
        println("$TAG ${CoinApp.TEST_APP} auth from parcelable is $auth")
        
        if (!person?.firstName.isNullOrEmpty() and !person?.lastName.isNullOrEmpty()){
            personFullName = "${person?.firstName} ${person?.lastName}"
            supportActionBar?.setTitle(personFullName)
        }else{
            // use username instead
            if (!auth?.username.isNullOrEmpty()){
                usernameAuth = "${auth?.username}"
                supportActionBar?.setTitle(usernameAuth)
            }
        }
        
        
        /*bottomNavigationView.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when(it.itemId){
                R.id.bottom_menu_home ->{
                    var t = Toast.makeText(this, "Home", Toast.LENGTH_SHORT)
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    t.show()
                    true
    
                }
                
                R.id.bottom_menu_manage ->{
                    var t = Toast.makeText(this, "Manage", Toast.LENGTH_SHORT)
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    t.show()
                    true
                }
                
                R.id.bottom_menu_news ->{
                    var t = Toast.makeText(this, "News", Toast.LENGTH_SHORT)
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    t.show()
                    true
                }
                
                else -> false
                
            }
        }*/
        
        observeViewModel()
        
        
    }
    
    // NOTE : could just set this action bar title in the fragments (see WebFragment).
    // Then we would not have to observe viewmodel here.
    private fun observeViewModel() {
        
        
        viewModel.cryptoLiveData.observe(this, Observer { crypto ->
            
            println("$TAG ${CoinApp.TEST_APP} in observer and crypto is $crypto")
            selectedCoin = crypto
            val personFullName = "${person?.firstName} ${person?.lastName}"
            
            
            crypto?.let {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.coinListFragment -> {
                            supportActionBar?.setTitle(personFullName)
                        }
                        R.id.coinDetailFragment -> {
                            supportActionBar?.setTitle("${crypto.coin.coinName} Details")
                        }
                        R.id.settingsFragment -> {
                        
                        }
                        R.id.webFragment ->{
                        
                        }
                    }
                }
            }
            
        })
        
    }
    
    
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
    
    override fun onBackPressed() {
        println("$TAG ${CoinApp.TEST_APP} and onBackPressed() ")
        
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.fragment) as? NavHostFragment
        
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) //as? IOnBackPressed
        
        if (currentFragment is WebFragment) {
            println("$TAG ${CoinApp.TEST_APP} and currentFragment is WebFragment ")
            currentFragment.onBackPressed().takeIf {
                !it
            }?.let {
                println("$TAG ${CoinApp.TEST_APP} and should be system backpressed! and IT is: $it")
                super.onBackPressed()
                
            }
        } else {
            super.onBackPressed()
        }
        
        
    }
    
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    
    
}
