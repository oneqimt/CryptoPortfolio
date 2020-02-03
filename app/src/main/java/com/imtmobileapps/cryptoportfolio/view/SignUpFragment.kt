package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        registerBtn.setOnClickListener {
            println("$TAG ${CoinApp.TEST_APP} register clicked")
        }
        
        already_have_account.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        Navigation.findNavController(v!!).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                }
                
                return v?.onTouchEvent(event) ?: true
            }
        })
    }
    
    companion object {
        private val TAG = SignUpFragment::class.java.simpleName
    }
    
    
}
