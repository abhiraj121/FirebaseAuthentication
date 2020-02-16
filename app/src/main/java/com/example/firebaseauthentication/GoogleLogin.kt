package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_login.*

class GoogleLogin : AppCompatActivity(), View.OnClickListener {

    private val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 9001
    private lateinit var auth: FirebaseAuth
    private lateinit var signOut: Button
    private lateinit var GoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)

        val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signOut = findViewById<View>(R.id.signOutBtn) as Button
        GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn.setOnClickListener {
                view: View? -> signIn()
        }
    }
    private fun signIn () {
        val signInIntent: Intent = GoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }
    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount ?= completedTask.getResult(ApiException::class.java)
            updateUI (account!!)
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateUI (account: GoogleSignInAccount) {
        val dispTxt = findViewById<View>(R.id.status) as TextView
        dispTxt.text = account.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
            GoogleSignInClient.signOut().addOnCompleteListener {
                task: Task<Void> -> if (task.isSuccessful) {
            dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
            signOut.isClickable = false
        }
        }
        }
    }

    override fun onClick(v: View?) {
    }
}