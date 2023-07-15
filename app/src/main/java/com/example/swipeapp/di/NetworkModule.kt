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


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesTopHeadlinesApi(retrofit: Retrofit): Swipe {
        return retrofit.create(Swipe::class.java)
    }


}
//
//@InstallIn(SingletonComponent::class)
//@Module
//class NetworkModule {
//
//    @Singleton
//    @Provides
//    fun providesRetrofit(): Retrofit.Builder {
//        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//    }
//
//
//
//    @Singleton
//    @Provides
//    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): Swipe {
//        return retrofitBuilder.build().create(Swipe::class.java)
//    }
//
//
////    @Provides
////    fun provideBaseUrl(): String = "https://app.getswipe.in/"
////
////    @Provides
////    fun provideRetrofit(baseUrl: String): Retrofit {
////        return Retrofit.Builder()
////            .baseUrl(baseUrl)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////    }
////
////    @Provides
////    fun provideApiService(retrofit: Retrofit): Swipe {
////        return retrofit.create(Swipe::class.java)
////    }
//
////    @Singleton
////    @Provides
////    fun providesRetrofit(): Retrofit.Builder {
////        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////    }
////
////    @Singleton
////    @Provides
////    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): Swipe {
////        return retrofitBuilder.build().create(Swipe::class.java)
////    }
//
//
//
//
//}