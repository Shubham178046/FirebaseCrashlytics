package com.example.firebasecrashlytics.Notification

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

open class RetrofitService {
    companion object
    {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.BODY
        }).build()

        fun getService() : NotificationApi
        {
            var retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient)  .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()

            return retrofit.create(NotificationApi::class.java)
        }

    }
}