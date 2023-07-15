package com.example.swipeapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swipeapp.api.Swipe
import com.example.swipeapp.models.SwipeAllProductResponse
import com.example.swipeapp.models.post.PostProduct
import com.example.swipeapp.utils.NetworkResult
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SwipeRepository @Inject constructor(private val swipeAPI: Swipe){
     private val _swipeLiveData = MutableLiveData<NetworkResult<List<SwipeAllProductResponse>>>()
    val swipeLiveData get() = _swipeLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    //post
    private val _userResponseLiveData = MutableLiveData<NetworkResult<SwipeAllProductResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<SwipeAllProductResponse>>
        get() = _userResponseLiveData

    suspend fun getAllProduct() {
        _swipeLiveData.postValue(NetworkResult.Loading())
        val response = swipeAPI.getAllProduct()
        if (response.isSuccessful && response.body() != null) {
            _swipeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _swipeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _swipeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        image: MultipartBody.Part?
    ): Response<PostProduct> {
        return swipeAPI.addProduct(productName, productType, price, tax, image)
    }
//    suspend fun addProduct(productName: String,
//                           productType: String,
//                           price: String,
//                           tax: String,
//                           images: List<File>) {
//        _userResponseLiveData.postValue(NetworkResult.Loading())
//        val response = swipeAPI.addProduct(productName,productType,price,tax,images)
//        handleResponse(response)
//    }
//    private fun handleResponse(response: Response<SwipeAllProductResponse>) {
//        if (response.isSuccessful && response.body() != null) {
//            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
//        }
//        else if(response.errorBody()!=null){
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
//        }
//        else{
//            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
//        }
//    }


//    suspend fun addProduct(
//        productName: String,
//        productType: String,
//        price: Double,
//        tax: Double,
//        images: List<File>?
//    ): NetworkResult<PostProduct?> {
//        return try {
//            val parts = mutableListOf<MultipartBody.Part>()
//            if (images != null) {
//                for (imageFile in images) {
//                    val requestImage = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
//                    val imagePart = MultipartBody.Part.createFormData("files[]", imageFile.name, requestImage)
//                    parts.add(imagePart)
//                }
//            }
//
//            val response = swipeAPI.addProduct(
//                RequestBody.create("text/plain".toMediaTypeOrNull(), productName),
//                RequestBody.create("text/plain".toMediaTypeOrNull(), productType),
//                RequestBody.create("text/plain", price),
//                RequestBody.create("text/plain".toMediaTypeOrNull(), tax),
//                parts
//            )
//
//            if (response.isSuccessful) {
//               NetworkResult.Success(response.body())
//            } else {
//                NetworkResult.Error("Error: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            NetworkResult.Error("Error: ${e.message}")
//        }
//    }


}