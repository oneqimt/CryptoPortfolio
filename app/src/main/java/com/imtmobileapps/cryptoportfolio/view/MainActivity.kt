package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.Auth
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.Person
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var navController: NavController
    private lateinit var viewModel: CoinDetailViewModel
    var selectedCoin: CryptoValue? = null
    private var person: Person? = null
    private var auth: Auth? = null
    private var personFullName = ""
    private var usernameAuth = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        navController = Navigation.findNavController(this, R.id.fragment)
        
        // Set up the tool bar
        setSupportActionBar(app_bar)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.coinListFragment -> {
                    supportActionBar?.setTitle(personFullName)
                }
                R.id.coinDetailFragment -> {
                    // Note: if selected coin is null, title is set in CoinDetailFragment
                    supportActionBar?.setTitle("${selectedCoin?.coin?.coinName} Details")
                }
                
                R.id.settingsFragment -> {
                    supportActionBar?.setTitle(R.string.settings)
                
                }
                R.id.webFragment -> {
                
                }
            }
            
        }
        
        viewModel = ViewModelProvider(this).get(CoinDetailViewModel::class.java)
        
        person = intent.getParcelableExtra("user")
        auth = intent.getParcelableExtra("auth")
        
        // set default
        println("$TAG ${CoinApp.TEST_APP} user from parcelable is $person")
        println("$TAG ${CoinApp.TEST_APP} auth from parcelable is $auth")
        
        
        if (!person?.firstName.isNullOrEmpty() and !person?.lastName.isNullOrEmpty()) {
            personFullName = "${person?.firstName} ${person?.lastName}"
            supportActionBar?.setTitle(personFullName)
        } else {
            // use username instead
            if (!auth?.username.isNullOrEmpty()) {
                usernameAuth = "${auth?.username}"
                supportActionBar?.setTitle(usernameAuth)
            }
        }
        
        
        observeViewModel()
        
    }
    
    private fun observeViewModel() {
        
        
        viewModel.cryptoLiveData.observe(this, Observer { crypto ->
            
            println("$TAG ${CoinApp.TEST_APP} in observer and crypto is $crypto")
            selectedCoin = crypto
            personFullName = "${person?.firstName} ${person?.lastName}"
            
        })
        
    }
    
    
    override fun onSupportNavigateUp(): Boolean {
        
        return navController.navigateUp()
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
        } else if (currentFragment is CoinListFragment) {
            println("$TAG ${CoinApp.TEST_APP} DONT LET THEM GO BACK TO LOGIN ")
        } else {
            super.onBackPressed()
        }
        
        
    }
    
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    
    
}
