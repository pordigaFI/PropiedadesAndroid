package com.porfirio.androidprojectpdg.data.remote.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.porfirio.androidprojectpdg.util.Constants

class RetrofitHelper {
    fun getRetrofit(): Retrofit {

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())  //vamos a usar Gson para convertir la respuesta
            .build()
    }
}