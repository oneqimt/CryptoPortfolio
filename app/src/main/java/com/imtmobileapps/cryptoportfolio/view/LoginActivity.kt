package com.imtmobileapps.cryptoportfolio.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imtmobileapps.cryptoportfolio.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var valid = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onPause() {
        super.onPause()

        checkAuth(true)
        valid = false
    }



    fun checkAuth(valid : Boolean){

        if (valid){
            // got to MainActivity
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            //remain here
        }
    }
}
