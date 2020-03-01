package com.imtmobileapps.cryptoportfolio.util

import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned

class DecimalInputFilter() : InputFilter {
    
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        
        val len = end - start
        if (len == 0){
            source?.let {
                return source
            }
        }
        
         val dlen = dest?.length
    
       // val range = 0..dstart
        // find the position of the decimal
        for (i in 0 until dstart){
            if (dest != null) {
                if (dest.contains(".")){
                    
                    val amount = (dlen?.minus((i + 1)) ?: 0 ) + len
                    if (amount > 2){
                        return ""
                    }else{
                        return SpannableStringBuilder(source, start, end)
                    }
        
                }
            }
        }
        
        for (i in start until end){
            source.let {
                if (it != null) {
                    if (it.contains(".")){
    
                        if (dlen != null) {
                            val one = dlen - dend
                            val two = end - (i +1)
                            val three = one + two
                            if (three > 2){
                                return ""
                            }
                        }
        
                    }
                }
            }
           
        }
        
        val sb = SpannableStringBuilder(source, start, end)
        
        return sb
    }
    
}

