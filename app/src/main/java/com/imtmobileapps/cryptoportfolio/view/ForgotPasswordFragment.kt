package com.imtmobileapps.cryptoportfolio.view


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

import com.imtmobileapps.cryptoportfolio.R
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    
        forgotPasswordEmailText.addTextChangedListener(object : TextWatcher {
            
            override fun afterTextChanged(s: Editable?) {
                println("S is $s")
               //forgotPasswordEmailText.setBackgroundColor(Color.TRANSPARENT)
            }
    
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
            }
    
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            
            }
    
        })
        
        
    }
    
    
}
