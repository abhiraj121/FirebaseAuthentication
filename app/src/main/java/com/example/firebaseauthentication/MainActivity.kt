package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import bolts.Task.delay
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val TAG = "creationOfObject"
    val auth by lazy { FirebaseAuth.getInstance() }
    lateinit var storedVerificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun submit(view: View) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    root.showSnackbar("${e.localizedMessage}")
                } else if (e is FirebaseTooManyRequestsException) {
                    root.showSnackbar("${e.localizedMessage}")
                }

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                var resendToken = token

            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91"+email.text.toString(),
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    //for email verification
    private fun signUpWithEase() {
        signUpWithEase()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener {
                root.showSnackbar("Account Created Successfully")
            }.addOnCanceledListener{

            }.addOnFailureListener{
                root.showSnackbar("Error ${it.localizedMessage}")
            }
    }

    //for phone verification
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                Log.d("TAG", "signInWithCredential:success")
                root.showSnackbar("Account Created Succesfully")
                startActivity(Intent(this@MainActivity,AccountDashboard ::class.java))

            }
            .addOnFailureListener {
                root.showSnackbar("Error : ${it.localizedMessage}")
            }
    }

    private fun signUpWithEmail() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener {
                root.showSnackbar("Account Created Succesfully")
            }.addOnCanceledListener {

            }.addOnFailureListener {
                root.showSnackbar("Error : ${it.localizedMessage}")
            }
    }

    fun verify(view: View) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, password.text.toString())
        signInWithPhoneAuthCredential(credential)
    }
}


fun View.showSnackbar(msg:String){
    val snack = Snackbar.make(this, msg,Snackbar.LENGTH_SHORT)
    snack.animationMode = Snackbar.ANIMATION_MODE_SLIDE
    snack.show()
}
