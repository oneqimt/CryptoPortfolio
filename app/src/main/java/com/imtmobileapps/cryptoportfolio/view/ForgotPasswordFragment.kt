package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : Fragment() {
    
    private lateinit var forgotViewModel: ForgotPasswordViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        forgotViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        forgotPasswordEmailText.addTextChangedListener(object : TextWatcher {
            
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    forgotPasswordEmailLayout.error = activity?.getString(R.string.enter_email)
                } else {
                    forgotPasswordEmailLayout.error = null
                    //forgotPasswordEmailLayout.boxBackgroundColor = activity?.resources.getColor(R.color.light_grey, null)
                    //forgotPasswordEmailText.clearFocus()
                    //forgotPasswordEmailText.invalidate()
                }
            }
    
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
    
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
    
        })
        
        sendBtn.isEnabled = true
        sendBtn.setOnClickListener {
            validateEmail()
        }
        
        observeModel()
        
        
    }
    
    private fun doSend(email: String){
        
        println("$TAG ${CoinApp.TEST_APP} time to send to server email is $email")
    
        forgotPasswordLoadingProgress.visibility = View.VISIBLE
        sendBtn.isEnabled = false
        
        forgotViewModel.resetPassword(email)
    
    }
    
    private fun validateEmail() {
    
        val emailValid: Boolean
    
        val emailString = forgotPasswordEmailText.text.toString()
        if (emailString.isNotEmpty()) {
            val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
            if (isValidEmail) {
                emailValid = true
            } else {
                forgotPasswordEmailLayout.error = activity?.getString(R.string.enter_email)
                emailValid = false
            }
        } else {
            forgotPasswordEmailLayout.error = activity?.getString(R.string.enter_email)
            emailValid = false
        }
        
        if (emailValid){
            doSend(emailString)
        }
        
        
    }
    
    fun observeModel() {
        
        forgotViewModel.returnDTO.observe(this, Observer {returnDTO ->
            returnDTO?.let {
                println("$TAG ${CoinApp.TEST_APP} in observe and returnDTO is: $returnDTO")
                forgotPasswordLoadingProgress.visibility = View.GONE
                sendBtn.isEnabled = true
            }
        })
        
        forgotViewModel.resetPasswordError.observe(this, Observer {
            forgotPasswordLoadingProgress.visibility = View.GONE
            sendBtn.isEnabled = true
            
        })
    
    }
    
    companion object {
        private val TAG = ForgotPasswordFragment::class.java.simpleName
    }
    
    
}
