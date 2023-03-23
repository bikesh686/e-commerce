package com.example.kiranapasa.activity


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kiranapasa.MainActivity
import com.example.kiranapasa.R
import com.example.kiranapasa.databinding.ActivityProductDetailsBinding
import com.example.kiranapasa.roomdb.AppDatabase
import com.example.kiranapasa.roomdb.ProductDao
import com.example.kiranapasa.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity: AppCompatActivity(){
    private lateinit var binding : ActivityProductDetailsBinding

   override fun onCreate(savedInstanceState: Bundle?){
       super.onCreate(savedInstanceState)
       binding = ActivityProductDetailsBinding.inflate(layoutInflater)
       setContentView(binding.root)

       getProductDetails(intent.getStringExtra("id"))
       

    }

    private fun getProductDetails(prodId: String?) {

        Firebase.firestore.collection("products")
            .document(prodId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")
                val stock = it.getString("stock")



                binding.textView8.text = name
                binding.textView7.text = "£$productSp"
                binding.textView.text = productDesc
                binding.textView9.text = stock

                val slideList = ArrayList<SlideModel>()

                for(data in list){
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }

                cartAction(prodId, name, productSp, it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener{
                    Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()

            }

    }

    private fun cartAction(prodId: String, name: String?, productSp: String?, coverImg: String?) {
        val productDao = AppDatabase.getInstance(this).productDao()

        if(productDao.isExit(prodId)!= null){
            binding.textView10.text = "Go to Cart"

        }else{
            binding.textView10.text = "Add to Cart"
        }

        binding.textView10.setOnClickListener{
            if(productDao.isExit(prodId) != null){
                openCart()


            }else{
                addToCart(productDao,prodId,name, productSp,coverImg)

            }
        }

    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)

        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()



    }

    private fun addToCart(
                            productDao: ProductDao,
                          prodId: String,
                          name: String?,
                          productSp: String?,
                          coverImg: String?) {

        val data = ProductModel(prodId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text = "Go to Cart "
        }

    }

}