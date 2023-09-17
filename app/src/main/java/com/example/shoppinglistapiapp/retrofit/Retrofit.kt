package com.example.shoppinglistapiapp.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    const val BASE_URL = "https://cyberprot.ru/shopping/v1/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitService: ShoppingListApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ShoppingListApiService::class.java)
    }
}