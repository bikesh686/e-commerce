package com.example.kiranapasa.adapter

import  android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiranapasa.activity.ProductDetailsActivity

import com.example.kiranapasa.databinding.LayoutCartItemBinding
import com.example.kiranapasa.roomdb.AppDatabase
import com.example.kiranapasa.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context, val list : List<ProductModel>):
RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)

        holder.binding.textView12.text = list[position].productName
        holder.binding.textView13.text = list[position].productSp


                //to go to Product Details from Cart
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("id", list[position].productId)
                    context.startActivity(intent)


                }

                val dao = AppDatabase.getInstance(context).productDao()
                //minus quantity
                var cartQuantity = 1
                var stock = holder.binding.textView19.text
                var stocks = Integer.parseInt(stock as String)


                holder.binding.textView11.text = cartQuantity.toString()
        
                //INCE
                holder.binding.imageView7.setOnClickListener {

                        if(stocks !=0){
                            cartQuantity += 1
                            stocks -= 1
                            holder.binding.textView11.text = cartQuantity.toString()
                            holder.binding.textView19.text = stocks.toString()
                        }




                }
        
        //DEC
        holder.binding.imageView8.setOnClickListener {
            if(cartQuantity>0){
                    cartQuantity -= 1
                    stocks += 1
                    holder.binding.textView19.text = stocks.toString()
                    holder.binding.textView11.text = cartQuantity.toString()

                }
//            else{
//                Toast.makeText(this, "0 reached", Toast.LENGTH_SHORT).show()
//            }
            

        }
                holder.binding.imageView6.setOnClickListener {

                    GlobalScope.launch(Dispatchers.IO) {
                        dao.deleteProduct(
                            ProductModel(
                                list[position].productId,
                                list[position].productName,
                                list[position].productImage,
                                list[position].productSp,
                                list[position].stock,


                                )
                        )

                    }


                }

    }
    override fun getItemCount(): Int {
        return list.size
    }
}






