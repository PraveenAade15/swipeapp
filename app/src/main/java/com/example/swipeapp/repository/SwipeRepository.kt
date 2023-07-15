package com.example.swipeapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swipeapp.api.Swipe
import com.example.swipeapp.models.get.SwipeAllProductResponse
import com.example.swipeapp.models.post.PostProduct
import com.example.swipeapp.utils.NetworkResult
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class SwipeRepository @Inject constructor(private val swipeAPI: Swipe){
    // Live data for swipe responses
    private val _swipeLiveData = MutableLiveData<NetworkResult<List<SwipeAllProductResponse>>>()
    val swipeLiveData get() = _swipeLiveData

    // Live data for status responses
    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    // Live data for user responses
    private val _userResponseLiveData = MutableLiveData<NetworkResult<SwipeAllProductResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<SwipeAllProductResponse>>
        get() = _userResponseLiveData
    /**
     * Makes a network request to fetch all products.
     */
    suspend fun getAllProduct() {
        // Set the swipeLiveData to loading state
        _swipeLiveData.postValue(NetworkResult.Loading())

        // Perform the network request
        val response = swipeAPI.getAllProduct()

        // Handle the network response
        if (response.isSuccessful && response.body() != null) {
            // Set the swipeLiveData to success state with the response data
            _swipeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            // Extract the error message from the error body and set swipeLiveData to error state
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _swipeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            // Set swipeLiveData to error state when something unexpected occurs
            _swipeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    /**
     * Adds a product by making a network request.
     *
     * @param productName The name of the product to be added.
     * @param productType The type of the product.
     * @param price The price of the product.
     * @param tax The tax for the product.
     * @param image An optional image of the product.
     * @return The response containing the added product.
     */
    suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        image: MultipartBody.Part?
    ): Response<PostProduct> {
        // Make a network request to add the product using the provided parameters
        return swipeAPI.addProduct(productName, productType, price, tax, image)
    }


}