package com.example.swipeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Constants {
    const val TAG = "SWIPEAPPTAG"
    const val BASE_URL = "https://app.getswipe.in/api/public/"
//    fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val network = connectivityManager.activeNetwork ?: return false
//        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
//
//        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }


}