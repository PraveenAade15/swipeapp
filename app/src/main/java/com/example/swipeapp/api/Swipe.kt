package com.example.swipeapp.api

import android.widget.TextView
import com.example.swipeapp.models.SwipeAllProductResponse
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

    @GET("get")
    suspend fun getAllProduct(): Response<List<SwipeAllProductResponse>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: String,
        @Part("product_type") productType: String,
        @Part("price") price: String,
        @Part("tax") tax: String,
        @Part image: MultipartBody.Part?
    ): Response<PostProduct>

//https://app.getswipe.in/api/public/add?product_name=Testing app&Product_type=product&price=1694.91525424&tax=18.0
//    @POST("api/public/add")
//    suspend fun addProduct(
//        @Query("product_name") productName: String,
//        @Query("product_type") productType: String,
//        @Query("price") price: Double,
//        @Query("tax") tax: Double
//    ): Response<SwipeAllProductResponse>


//    @FormUrlEncoded
//    @POST("api/public/add")
//    suspend fun postAllProduct(
//        @HeaderMap headerMap: Map<String, String>,
//        @Field("product_name") Product_Name: TextView,
//        @Field("product_type") Product_Type: TextView,
//        @Field("price") Price: TextView,
//        @Field("tax") Tax : TextView?,
//    ): Response<SwipeAllProductResponse>

}