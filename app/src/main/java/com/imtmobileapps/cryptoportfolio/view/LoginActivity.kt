package com.imtmobileapps.cryptoportfolio.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    var usernameText = ""
    var passwordText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        loginBtn.setOnClickListener {
            validateLogin()
        }

        observeModel()
    }

    fun doLogin(){

        viewModel.loginUser(usernameText, passwordText)
    }

    fun validateLogin(){

        usernameText = username.text.toString()
        passwordText = password.text.toString()

        if (usernameText.isNotEmpty() and passwordText.isNotEmpty()){
            doLogin()
        }else{
            Toast.makeText(
                getApplication(),
                "Please enter Username and Password",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    fun observeModel(){

        viewModel.user.observe(this, Observer { user ->
            user?.let {
                //if(it.personId != 0){ }
            }

        })

        viewModel.loginError.observe(this, Observer { isError ->
            isError?.let {

                if (isError){
                    username.setText("")
                    password.setText("")

                    username.requestFocus()
                    // notify user
                    Toast.makeText(
                        getApplication(),
                        "Username and/or Password not valid. Try again.",
                        Toast.LENGTH_SHORT).show()

                }

            }
        })
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

}
