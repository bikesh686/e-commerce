package com.example.kiranapasa.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.room.Query
import com.bumptech.glide.Glide
import com.example.kiranapasa.R
import com.example.kiranapasa.adapter.CategoryAdapter
import com.example.kiranapasa.adapter.ProductAdapter
import com.example.kiranapasa.databinding.FragmentHomeBinding
import com.example.kiranapasa.model.AddProductModel
import com.example.kiranapasa.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
//    private lateinit var searchView: SearchView
//    private lateinit var dataList: ArrayList<DataClass>
//    private lateinit var searchList: ArrayList<DataClass>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)

        if (preference.getBoolean("isCart",false))
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
    //SEARCH
//        val productRecycler = binding.productRecycler
//        searchView = binding.search
//
//        dataList = arrayListOf<DataClass>()
//        searchList = arrayListOf<DataClass>()



        //calling getCategories function
        getCategories()

        getProducts()

        getSliderImage()
        //SEARCH
//        searchView.clearFocus()
//        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                return true
//            }


////
//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun onQueryTextChange(newText: String?): Boolean {
//                searchList.clear()
//                val searchText = newText!!.toLowerCase(Locale.getDefault())
//                if(searchText.isNotEmpty()){
//                    dataList.forEach(){
//                        if(it.productName.toLowerCase(Locale.getDefault()).contains(searchText)){
//                            searchList.add(it)
//                        }
//                    }
//                    productRecycler.adapter!!.notifyDataSetChanged()
//                }else{
//                    searchList.clear()
//                    searchList.addAll(dataList)
//                    productRecycler.adapter!!.notifyDataSetChanged()
//                }
//                return false
//            }
//
//        })


        return binding.root

    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
            }

    }

    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("category")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(), list)
            }
    }

    private fun getProducts() {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
//                searchList.addAll(dataList)
//                binding.productRecycler.adapter = ProductAdapter(requireContext(),searchList)
               binding.productRecycler.adapter = ProductAdapter(requireContext(), list)
            }


    }
}