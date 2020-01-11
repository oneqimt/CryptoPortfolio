package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    var prefHelper = PreferencesHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


}
