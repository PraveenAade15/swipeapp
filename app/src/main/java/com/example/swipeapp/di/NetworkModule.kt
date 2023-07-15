package com.example.swipeapp.di

import com.example.swipeapp.api.Swipe
import com.example.swipeapp.utils.Constants
import com.example.swipeapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Dagger Hilt module that provides dependencies for network-related components.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of Retrofit.
     */
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides an instance of the Swipe API service.
     * @param retrofit The Retrofit instance.
     */
    @Provides
    fun providesSwipeApi(retrofit: Retrofit): Swipe {
        return retrofit.create(Swipe::class.java)
    }
}
