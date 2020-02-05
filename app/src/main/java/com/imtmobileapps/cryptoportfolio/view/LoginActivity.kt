package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {
    
    private lateinit var viewModel: LoginViewModel
    private lateinit var navControllerLogin: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        navControllerLogin = Navigation.findNavController(this, R.id.host_fragment_login_signup)
        
        navControllerLogin.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    supportActionBar?.setTitle(R.string.action_sign_in)
                }
                R.id.signUpFragment -> {
                    supportActionBar?.setTitle(R.string.register)
                }
                R.id.forgotPasswordFragment ->{
                    supportActionBar?.setTitle(R.string.forgot_password)
                }
            }
        }
        
    }
    
    
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
    
}
