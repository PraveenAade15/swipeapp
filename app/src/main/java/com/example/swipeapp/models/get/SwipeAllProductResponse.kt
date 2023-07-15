package com.example.swipeapp.models.get

import com.google.gson.annotations.SerializedName

data class SwipeAllProductResponse(@SerializedName("image"        ) var image       : String? = null,
                                   @SerializedName("price"        ) var price       : Double? = null,
                                   @SerializedName("product_name" ) var productName : String? = null,
                                   @SerializedName("product_type" ) var productType : String? = null,
                                   @SerializedName("tax"          ) var tax         : Float?    = null)