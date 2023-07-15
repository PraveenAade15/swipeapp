package com.example.swipeapp.models

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_type") val productType: String,
    val price: Double,
    val tax: Double
)

