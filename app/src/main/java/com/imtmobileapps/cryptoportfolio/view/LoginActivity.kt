package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var valid = true
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loginBtn.setOnClickListener {
            doLogin()
        }

        observeModel()
    }

    fun doLogin(){
        val usernameText = username.text.toString()
        val passwordText = password.text.toString()
        viewModel.loginUser(usernameText, passwordText)

    }

    fun validateLogin() : Boolean{

        val valid = true

        return valid

    }

    fun observeModel(){

        viewModel.user.observe(this, Observer { user ->
            user?.let {
                if(it.personId != 0){
                    this.finish()
                }
            }

        })

        viewModel.loginError.observe(this, Observer { isError ->
            isError?.let {
                username.setText("")
                password.setText("")
                // notify user
            }
        })
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

}
