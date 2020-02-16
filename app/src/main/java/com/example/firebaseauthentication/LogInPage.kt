package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_log_in_page.*

class LogInPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

        googleLogIn.setOnClickListener {
            startActivity(Intent(this, GoogleLogin ::class.java))
        }
        phoneLogIn.setOnClickListener {
            startActivity(Intent(this, MainActivity ::class.java))
        }
        facebookLogIn.setOnClickListener {
            startActivity(Intent(this, FacebookLogin ::class.java))
        }
    }
}
