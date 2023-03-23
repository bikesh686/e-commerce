package com.example.kiranapasa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kiranapasa.MainActivity
import com.example.kiranapasa.databinding.ActivityForgotBinding
import com.example.kiranapasa.databinding.ActivityLoginBinding
import com.example.kiranapasa.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ForgotActivity : AppCompatActivity() {

    private lateinit var builder : AlertDialog
    private lateinit var binding: ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityForgotBinding.inflate(layoutInflater)

        setContentView(binding.root)




        //login button
        binding.button3.setOnClickListener{
            if(binding.userNumber.text!!.isEmpty())
                Toast.makeText(this, "Please Provide All Details", Toast.LENGTH_SHORT).show()
            else
                sendOtp(binding.userNumber.text.toString())


        }

    }


    private fun sendOtp(number: String) {
        builder = AlertDialog.Builder(this).setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+44$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId:String,
            token:PhoneAuthProvider.ForceResendingToken){
            builder.dismiss()
            val intent = Intent(this@ForgotActivity, OTPActivity::class.java)
            intent.putExtra("verificationId",verificationId)
            intent.putExtra("number", binding.userNumber.text.toString())
            startActivity(intent)
        }

    }
}









