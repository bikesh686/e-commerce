package com.example.kiranapasa.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kiranapasa.R
import com.example.kiranapasa.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding

    private lateinit var preferences: SharedPreferences

    private lateinit var totalCost :String

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
           preferences     = this.getSharedPreferences("user", MODE_PRIVATE)
               totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()

        binding.proceed.setOnClickListener{
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userPin.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userStreet.text.toString()
            )

        }
    }

    private fun validateData(number: String, name: String, postcode: String, city: String, state: String, street: String) {

    if(number.isEmpty() || state.isEmpty() || name.isEmpty() ){
        Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
    }
        else{
            storeData(postcode,city,state,street)
    }


    }

    private fun storeData(postcode: String, city: String, state: String, street: String) {
        val map = hashMapOf<String, Any>()
        map["street"] = street
        map["state"] = state
        map["city"] = city
        map["postcode"] = postcode

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)


                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)


            }.addOnFailureListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userStreet.setText(it.getString("street"))
                binding.userCity.setText(it.getString("city"))
                binding.userState.setText(it.getString("state"))
                binding.userPin.setText(it.getString("postcode"))

            }

    }
}