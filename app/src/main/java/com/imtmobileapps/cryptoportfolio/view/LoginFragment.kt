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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.LoginViewModel
import androidx.lifecycle.Observer
import com.imtmobileapps.cryptoportfolio.model.SignUp
import com.imtmobileapps.cryptoportfolio.util.createSignUp
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    
    private lateinit var viewModel: LoginViewModel
    
    var usernameText = ""
    var passwordText = ""
    
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    
        usernameTxt.setText("")
        passwordTxt.setText("")
        usernameTxt.requestFocus()
        
        loginBtn.setOnClickListener {
    
            validateLogin()
            it.isEnabled = false
            
        }
    
        usernameTxt.addTextChangedListener(object : TextWatcher {
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
    
        passwordTxt.addTextChangedListener(object : TextWatcher {
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
        
        createAccountBtn.setOnClickListener {
            findNavController(it).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
        
        forgot_password_text.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        findNavController(v!!).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                }
                
                return v?.onTouchEvent(event) ?: true
            }
        })
        
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
            loginBtn.isEnabled = true
            doLogin()
        } else {
            Toast.makeText(
                activity,
                "Please enter Username and Password",
                Toast.LENGTH_SHORT
            ).show()
            
        }
        
    }
    
    fun testSignUp() {
        val signUp: SignUp = createSignUp()
        
        viewModel.signUpUser(signUp)
    }
    
    
    fun observeModel() {
       
       viewModel.user.observe(this, Observer { user ->
           user?.let {
               println("$TAG USER in NOT NULL")
               loading.visibility = View.GONE
               loginBtn.isEnabled = true
           }
           
       })
       
       viewModel.signUpPerson.observe(this, Observer { signup ->
           signup?.let {
               println("$TAG ${CoinApp.TEST_APP} in Observe and sign up is : $it")
           }
       })
       
       viewModel.loginError.observe(this, Observer { isError ->
           isError?.let {
               if (isError) {
                   val imm: InputMethodManager = this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                   imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
                   loading.visibility = View.GONE
                   loginBtn.isEnabled = true
                   
                   usernameTxt.setText("")
                   passwordTxt.setText("")
                   usernameTxt.clearFocus()
                   passwordTxt.clearFocus()
                   
                   val toastDurationInMilliSeconds = 10000L
                   val t = Toast.makeText(
                       activity,
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
