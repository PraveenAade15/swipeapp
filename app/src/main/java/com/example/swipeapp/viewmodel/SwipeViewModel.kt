package com.example.swipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeapp.models.SwipeAllProductResponse
import com.example.swipeapp.models.post.PostProduct
import com.example.swipeapp.repository.SwipeRepository
import com.example.swipeapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(private val swipeRepository: SwipeRepository) : ViewModel() {

    val swipeLiveData get() = swipeRepository.swipeLiveData
    val statusLiveData get() = swipeRepository.statusLiveData

    fun getAllProduct() {
        viewModelScope.launch {
            swipeRepository.getAllProduct()
        }
    }
    val selectCropYear = MutableLiveData(arrayOf("Select Category", "Service", "Books", "Food", "Product", "Electronics"))
//    fun addProduct(product_name:String,product_type:String,tax:Double,price:Double){
//        viewModelScope.launch {
//            swipeRepository.addProduct(product_name,product_type,tax,price)
//        }
//    }
private val _addProductResponse = MutableLiveData<Response<PostProduct>>()
    val addProductResponse: LiveData<Response<PostProduct>> = _addProductResponse

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            val response = swipeRepository.addProduct(productName, productType, price, tax, image)
            _addProductResponse.value = response
        }
    }

//    private val _addProductResult = MutableLiveData<NetworkResult<PostProduct?>>()
//    val addProductResult: LiveData<NetworkResult<PostProduct?>> = _addProductResult
//
//    fun addProduct(
//        productName: String,
//        productType: String,
//        price: Double,
//        tax: Double,
//        images: List<File>?
//    ) {
//        viewModelScope.launch {
//            _addProductResult.value = swipeRepository.addProduct(productName, productType, price, tax, images)
//        }
//    }
}