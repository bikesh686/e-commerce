package com.example.kiranapasa.model

data class AddProductModel(
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId: String? = "",
    val productMrp: String? = "",
    val productSp: String? = "",
    val stock: String? = "", //stock_quantity


    val productImages: ArrayList<String> = ArrayList()



)
