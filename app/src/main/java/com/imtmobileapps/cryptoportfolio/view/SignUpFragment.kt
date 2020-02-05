package com.imtmobileapps.cryptoportfolio.view


import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.Auth
import com.imtmobileapps.cryptoportfolio.model.SignUp
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.addRequiredToHint
import com.imtmobileapps.cryptoportfolio.util.createEmptySignUp
import com.imtmobileapps.cryptoportfolio.util.createSignUp
import com.imtmobileapps.cryptoportfolio.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {
    
    private lateinit var signUpViewModel: SignUpViewModel
    
    var emailString = ""
    var usernameString = ""
    var passwordString = ""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        
        signupEmailText.setText("")
        signupUsernameText.setText("")
        signupPasswordText.setText("")
        signupEmailText.requestFocus()
        
        val parentActivity : LoginActivity = activity as LoginActivity
        signupEmailLayout.hint = addRequiredToHint(parentActivity, signupEmailLayout.hint.toString())
        signupUsernameLayout.hint = addRequiredToHint(parentActivity, signupUsernameLayout.hint.toString())
        signupPasswordLayout.hint = addRequiredToHint(parentActivity, signupPasswordLayout.hint.toString())
        
        signupEmailText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    signupEmailLayout.error = activity?.getString(R.string.enter_email)
                } else {
                    signupEmailLayout.error = null
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        signupUsernameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    signupUsernameLayout.error = activity?.getString(R.string.enter_username)
                } else {
                    signupUsernameLayout.error = null
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        signupPasswordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    signupPasswordLayout.error = activity?.getString(R.string.enter_password)
                } else {
                    signupPasswordLayout.error = null
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        registerBtn.setOnClickListener {
            println("$TAG ${CoinApp.TEST_APP} register clicked")
            validateSignUp()
        }
        
        already_have_account.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        Navigation.findNavController(v!!).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                    
                    MotionEvent.ACTION_UP ->
                        v?.performClick()
                }
                
                return v?.onTouchEvent(event) ?: true
            }
        })
        
        observeModel()
    }
    
    private fun doSignUp(email: String, username: String, password: String) {
        val signUp: SignUp = createEmptySignUp()
        
        val person = signUp.person
        val auth = signUp.auth
        
        person.email = email
        auth.username = username
        auth.password = password
        
        val gson = Gson()
        val test = gson.toJson(signUp)
        println("$TAG ${CoinApp.TEST_APP} SIGN UP is: $test")
        
        signUpViewModel.signUpUser(signUp)
    }
    
    private fun validateSignUp() {
        
        val emailValid: Boolean
        val usernameValid: Boolean
        val passwordValid: Boolean
        
        emailString = signupEmailText.text.toString()
        usernameString = signupUsernameText.text.toString()
        passwordString = signupPasswordText.text.toString()
        
        if (emailString.isNotEmpty()) {
            val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
            if (isValidEmail) {
                emailValid = true
            } else {
                signupEmailLayout.error = activity?.getString(R.string.enter_email)
                emailValid = false
            }
        } else {
            signupEmailLayout.error = activity?.getString(R.string.enter_email)
            emailValid = false
        }
        
        if (usernameString.isNotEmpty()) {
            usernameValid = true
        } else {
            usernameValid = false
            signupUsernameLayout.error = activity?.getString(R.string.enter_username)
            
        }
        
        if (passwordString.isNotEmpty()) {
            passwordValid = true
        } else {
            passwordValid = false
            signupPasswordLayout.error = activity?.getString(R.string.enter_password)
        }
        
        var oktosend = false
        
        if (emailValid and usernameValid and passwordValid){
            oktosend = true
        }
        
        if (oktosend) {
            println("$TAG ${CoinApp.TEST_APP} TIme to call the server to signup")
            
            doSignUp(emailString, usernameString, passwordString)
        }
        
    }
    
    
    private fun observeModel() {
        
        signUpViewModel.signUpPerson.observe(this, Observer { signup ->
            signup?.let {
                println("$TAG ${CoinApp.TEST_APP} in Observe and sign up is : $it")
            }
        })
        
    }
    
    companion object {
        private val TAG = SignUpFragment::class.java.simpleName
    }
    
    
}
