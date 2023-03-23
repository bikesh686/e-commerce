package com.example.kiranapasa.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.kiranapasa.MainActivity
import com.example.kiranapasa.R
import com.example.kiranapasa.roomdb.AppDatabase
import com.example.kiranapasa.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(),PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_lIHHxrxnB2Zijo")

        //passing the total price
        val price = intent.getStringExtra("totalCost")



            try {
                val options = JSONObject()
                options.put("name","Kirana Pa Sa")
                options.put("description","Grocery App")
                //You can omit the image option to fetch the image from the dashboard
                options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
                options.put("theme.color", "#673AB7")
                options.put("currency","GBP")

                options.put("amount",(price!!.toInt()*100))//pass amount in currency subunits
                options.put("prefill.email", "bikeshshrestha686@gmail.com")
                options.put("prefill.contact", +447868616698)
                checkout.open(this, options)

            }catch (e: Exception){
                Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }


    override fun onPaymentSuccess(p0: String?){
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()

        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){
            fetchData(currentId)

        }
    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId))
                }
                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)
            }

    }

    private fun saveData(name: String?, price: String?, productId: String) {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data =  hashMapOf<String, Any>()
        data["name"] = name !!
        data["price"] = price!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.add(data).addOnSuccessListener {
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }.addOnFailureListener {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
    }
}