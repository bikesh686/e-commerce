package com.example.kiranapasa.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kiranapasa.activity.ProductDetailsActivity
import com.example.kiranapasa.databinding.ItemCategoryProductLayoutBinding
import com.example.kiranapasa.databinding.LayoutProductItemBinding
import com.example.kiranapasa.model.AddProductModel

class CategoryProductAdapter(private val context : Context, private val list: ArrayList<AddProductModel>):RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {
    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
    Glide.with(context).load(list[position].productImages).into(holder.binding.imageView3)
        //
        holder.binding.textView5.text = list[position].productName
        holder.binding.textView6.text = list[position].productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java )
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}