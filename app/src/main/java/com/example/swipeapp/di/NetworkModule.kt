package com.example.swipeapp.di

import com.example.swipeapp.api.Swipe
import com.example.swipeapp.utils.Constants
import com.example.swipeapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Dagger Hilt module that provides dependencies for network-related components.
 * created by Praveen Aade
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of the OkHttpClient with the interceptor.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            // Modify the request if needed
            val modifiedRequest = originalRequest.newBuilder()
                // Add headers or modify existing ones
                .addHeader("Authorization", "Bearer token123")
                .build()

            // Proceed with the modified request
            val response = chain.proceed(modifiedRequest)

            // Perform any modifications to the response here

            response
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit with the OkHttpClient.
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides an instance of the Swipe API service.
     * @param retrofit The Retrofit instance.
     */
    @Provides
    fun provideSwipeApi(retrofit: Retrofit): Swipe {
        return retrofit.create(Swipe::class.java)
    }
}

