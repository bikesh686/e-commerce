package com.example.kiranapasa.fragment


import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.kiranapasa.R
import com.example.kiranapasa.activity.AddressActivity
import com.example.kiranapasa.activity.CategoryActivity
import com.example.kiranapasa.activity.LoginActivity
import com.example.kiranapasa.adapter.CartAdapter
import com.example.kiranapasa.adapter.ProductAdapter
import com.example.kiranapasa.databinding.FragmentCartBinding
import com.example.kiranapasa.databinding.LayoutCartItemBinding
import com.example.kiranapasa.model.AddProductModel
import com.example.kiranapasa.roomdb.AppDatabase
import com.example.kiranapasa.roomdb.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class CartFragment : Fragment() {
    private lateinit var binding : FragmentCartBinding
    private lateinit var list: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentCartBinding.inflate(layoutInflater)




        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)

        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

       val dao = AppDatabase.getInstance(requireContext()).productDao()
        list = ArrayList()

        dao.getAllProducts().observe(requireActivity()){

            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)
            list.clear()
            for(data in it){
                list.add(data.productId)

            }

            totalCost(it)
            

        }

        return binding.root
    }

    //giving error


    private fun totalCost(data: List<ProductModel>?) {




        var total = 0
        for (item in data!!) {
            total += item.productSp!!.toInt()
        }

        //TOTAL

        binding.textView12.text = "Total item in cart is ${data.size}"
        binding.textView13.text = "Total Cost :  £ $total"


        binding.checkout.setOnClickListener {

            if (FirebaseAuth.getInstance().currentUser == null) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } else {

                val intent = Intent(context, AddressActivity::class.java)
                val b = Bundle()
                b.putStringArrayList("productIds", list)
                b.putString("totalCost", total.toString())
                intent.putExtras(b)



                startActivity(intent)

            }
        }





    }




}