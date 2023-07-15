package com.example.swipeapp.models.post

import com.google.gson.annotations.SerializedName

data class PostProduct(
    @SerializedName("message"         ) var message        : String?         = null,
    @SerializedName("product_details" ) var productDetails : ProductDetails? = ProductDetails(),
    @SerializedName("product_id"      ) var productId      : Int?            = null,
    @SerializedName("success"         ) var success        : Boolean?        = null
)

data class ProductDetails (

    @SerializedName("image"        ) var image       : String? = null,
    @SerializedName("price"        ) var price       : Int?    = null,
    @SerializedName("product_name" ) var productName : String? = null,
    @SerializedName("product_type" ) var productType : String? = null,
    @SerializedName("tax"          ) var tax         : Int?    = null

)
