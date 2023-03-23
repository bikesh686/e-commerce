package com.example.kiranapasa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kiranapasa.databinding.ActivityRegisterBinding
import com.example.kiranapasa.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)
    //open Login from Register activity
        binding.textView14.setOnClickListener{

            openLogin()
        }

        //for login button press
        binding.button3.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()


            validateUser()
        }

    }

    private fun validateUser() {
        if(binding.userName.text!!.isEmpty() || binding.userNumber.text!!.isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else
            storeData()

    }

    private fun storeData() {
        //to show messages while storing
        val builder = AlertDialog.Builder(this).setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val preferences= this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString("number",binding.userNumber.text.toString())
        editor.putString("name", binding.userName.text.toString())
        //
        editor.putString("email", binding.email.text.toString())
        editor.putString("password",binding.password.text.toString())
        editor.apply()

        //to store data into firebase

        val data = UserModel(userName =  binding.userName.text.toString(), userPhoneNumber = binding.userNumber.text.toString(), email = binding.email.text.toString(), pass = binding.password.text.toString())
        Firebase.firestore.collection("users").document(binding.userNumber.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this,"User Registered.",Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()

            }
            .addOnFailureListener{
                builder.dismiss()

                Toast.makeText(this,"Something went wrong.",Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}