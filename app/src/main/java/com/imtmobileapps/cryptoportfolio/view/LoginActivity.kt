package com.imtmobileapps.cryptoportfolio.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
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
        
        usernameTextInputEditText.setText("")
        passwordTextInputEditText.setText("")
        usernameTextInputEditText.requestFocus()
        
        loginBtn.setOnClickListener {
            validateLogin()
            it.isEnabled = false
        }
        usernameTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    usernameLayout.error = "Please input a valid username."
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        passwordTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    passwordLayout.error = "Please input a valid password"
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        observeModel()
    }
    
    fun doLogin() {
        
        viewModel.loginUser(usernameText, passwordText)
    }
    
    fun validateLogin() {
        
        usernameText = usernameTextInputEditText.text.toString()
        passwordText = passwordTextInputEditText.text.toString()
        
        
        if (usernameText.isNotEmpty() and passwordText.isNotEmpty()) {
            loading.visibility = View.VISIBLE
            loginBtn.isEnabled = true
            doLogin()
        } else {
            Toast.makeText(
                getApplication(),
                "Please enter Username and Password",
                Toast.LENGTH_SHORT
            ).show()
            
        }
        
    }
    
    fun observeModel() {
        
        viewModel.user.observe(this, Observer { user ->
            user?.let {
                println("$TAG USER in NOT NULL")
                loading.visibility = View.GONE
                loginBtn.isEnabled = true
            }
            
        })
        
        viewModel.loginError.observe(this, Observer { isError ->
            isError?.let {
                if (isError) {
                    val imm: InputMethodManager = this
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    loading.visibility = View.GONE
                    loginBtn.isEnabled = true
                    
                    usernameTextInputEditText.setText("")
                    passwordTextInputEditText.setText("")
                    usernameTextInputEditText.clearFocus()
                    passwordTextInputEditText.clearFocus()
                    
                    val toastDurationInMilliSeconds = 10000L
                    val t = Toast.makeText(
                        getApplication(),
                        "There was a server error with those login values. Please try again.",
                        Toast.LENGTH_LONG
                    )
                    t.setGravity(Gravity.TOP, 0, 300)
                    val toastCountDown: CountDownTimer
                    toastCountDown = object :
                        CountDownTimer(
                            toastDurationInMilliSeconds,
                            1000
                        ) {
                        override fun onTick(millisUntilFinished: Long) {
                            t.show()
                        }
                        
                        override fun onFinish() {
                            t.cancel()
                        }
                    }
                    
                    t.show();
                    toastCountDown.start();
                    
                }
                
            }
        })
    }
    
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
    
}
