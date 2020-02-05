package com.imtmobileapps.cryptoportfolio.view


import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.SignUp
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.util.addRequiredToHint
import com.imtmobileapps.cryptoportfolio.util.createSignUp
import com.imtmobileapps.cryptoportfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class LoginFragment : Fragment() {
    
    private lateinit var viewModel: LoginViewModel
    
    var usernameText = ""
    var passwordText = ""
    
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        
        usernameTxt.setText("")
        passwordTxt.setText("")
        usernameTxt.requestFocus()
    
        val parentActivity : LoginActivity = activity as LoginActivity
        usernameLayout.hint = addRequiredToHint(parentActivity, usernameLayout.hint.toString())
        passwordLayout.hint = addRequiredToHint(parentActivity, passwordLayout.hint.toString())
        
        loginBtn.setOnClickListener {
            
            validateLogin()
            
        }
        
        usernameTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    usernameLayout.error = activity?.getString(R.string.enter_username)
                } else {
                    usernameLayout.error = null
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        passwordTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    passwordLayout.error = activity?.getString(R.string.enter_password)
                } else {
                    passwordLayout.error = null
                }
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
        })
        
        createAccountBtn.setOnClickListener {
            findNavController(it).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
        
        forgot_password_text.setOnTouchListener  { v: View, m: MotionEvent ->
            
            when(m.action){
                MotionEvent.ACTION_DOWN ->
                    // ADD ForgotPasswordFragment call for email
                    findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
                
            }
            true
            
            
        }
        
        observeModel()
    }
    
    fun doLogin() {
        
        viewModel.loginUser(usernameText, passwordText)
    }
    
    
    fun validateLogin() {
        
        usernameText = usernameTxt.text.toString()
        passwordText = passwordTxt.text.toString()
        
        
        if (usernameText.isNotEmpty() and passwordText.isNotEmpty()) {
            loading.visibility = View.VISIBLE
            doLogin()
        } else {
            
            if (usernameText.isEmpty()) {
                usernameLayout.error = activity?.getString(R.string.enter_username)
            }
            if (passwordText.isEmpty()) {
                passwordLayout.error = activity?.getString(R.string.enter_password)
            }
            
        }
        
    }
    
    fun observeModel() {
        
        viewModel.user.observe(this, Observer { user ->
            user?.let {
                println("$TAG USER in NOT NULL")
                loading.visibility = View.GONE
            }
            
        })
        
        viewModel.loginError.observe(this, Observer { isError ->
            isError?.let {
                if (isError) {
                    val imm: InputMethodManager =
                        this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
                    loading.visibility = View.GONE
                    
                    usernameTxt.setText("")
                    passwordTxt.setText("")
                    usernameTxt.clearFocus()
                    passwordTxt.clearFocus()
                    
                    val toastDurationInMilliSeconds = 10000L
                    val t = Toast.makeText(
                        activity,
                        activity?.getString(R.string.login_server_error),
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
                    
                    t.show()
                    toastCountDown.start()
                    
                }
                
            }
        })
    }
    
    
    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}
