package com.example.swipeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeapp.models.post.PostProduct
import com.example.swipeapp.repository.SwipeRepository
import com.example.swipeapp.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


/**
 * ViewModel class responsible for managing data and network requests related to the Swipe feature.
 */
@HiltViewModel
class SwipeViewModel @Inject constructor(private val swipeRepository: SwipeRepository) : ViewModel() {
    // Expose swipeLiveData from swipeRepository
    val swipeLiveData get() = swipeRepository.swipeLiveData

    // Expose statusLiveData from swipeRepository
    val statusLiveData get() = swipeRepository.statusLiveData

    // LiveData for add product response
    private val _addProductResponse = MutableLiveData<Response<PostProduct>>()
    val addProductResponse: LiveData<Response<PostProduct>> = _addProductResponse

    /**
     * Fetches all products.
     */
    fun getAllProduct() {
        viewModelScope.launch {
            swipeRepository.getAllProduct()
        }
    }

    /**
     * Adds a product.
     *
     * @param productName The name of the product to be added.
     * @param productType The type of the product.
     * @param price The price of the product.
     * @param tax The tax for the product.
     * @param image An optional image of the product.
     */
    fun addProduct(
        productName: RequestBody,
        productType: RequestBody,
        price: RequestBody,
        tax: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            Log.d(TAG, "addProductGgbsxs: $image")
            val response = swipeRepository.addProduct(productName, productType, price, tax, image)
            _addProductResponse.value = response
        }
    }
}
