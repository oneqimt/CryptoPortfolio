package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.Person
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var viewModel: CoinDetailViewModel
    var selectedCoin: CryptoValue? = null
    private var person: Person? = null
    private var personFullName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)

        person = getIntent().getParcelableExtra("user")
        // set default
        println("$TAG user from parcelable is $person")
        personFullName = "${person?.firstName} ${person?.lastName}"
        supportActionBar?.setTitle(personFullName)

        observeViewModel()


    }

    fun observeViewModel() {


        viewModel.cryptoLiveData.observe(this, Observer { crypto ->

            println("$TAG in observer and crypto is $crypto")
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
                    }
                }
            }

        })

    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


}
