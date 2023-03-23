package com.example.kiranapasa.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.kiranapasa.R
import com.example.kiranapasa.adapter.CategoryAdapter
import com.example.kiranapasa.adapter.CategoryProductAdapter
import com.example.kiranapasa.adapter.ProductAdapter
import com.example.kiranapasa.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getProducts(intent.getStringExtra("cate"))
    }

    private fun getProducts(category: String?) {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.adapter = CategoryProductAdapter(this,list)
            }


    }


    }
