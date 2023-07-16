package com.example.swipeapp.api

import com.example.swipeapp.models.get.SwipeAllProductResponse
import com.example.swipeapp.models.post.PostProduct
import com.example.swipeapp.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Swipe {
    /*
    getAllProduct()

HTTP Method: GET
Endpoint: "/get"
Description: This endpoint retrieves all products.
Returns: A response containing a list of SwipeAllProductResponse objects.
Synchronization: This function is a suspend function, indicating that it can be called from a coroutine and will suspend execution until the response is received.
     */
    @GET("get")
    suspend fun getAllProduct(): Response<List<SwipeAllProductResponse>>
    /*
    addProduct(productName: String, productType: String, price: String, tax: String, image: MultipartBody.Part?)

HTTP Method: POST
Endpoint: "/add"
Description: This endpoint adds a new product to the system.
Parameters:
productName (String): The name of the product.
productType (String): The type of the product.
price (String): The price of the product.
tax (String): The tax amount for the product.
image (MultipartBody.Part?): Optional parameter for attaching an image file to the request. This parameter allows uploading images as multipart form data.
Returns: A response containing a PostProduct object.
     */

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name")  productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Response<PostProduct>

    /*

     */


}