package com.mshukshina.shoppinglistapiapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingListApiService {

    @GET("CreateTestKey")
    fun getTestKey(): Call<String>

    @GET("Authentication")
    fun authenticate(@Query("key") key: String): Call<Authentication>

    @POST("CreateShoppingList")
    fun createShoppingList(@Query("key") key: String, @Query("name") name: String):
            Call<ShoppingListCreated>

    @POST("RemoveShoppingList")
    fun removeShoppingList(@Query("list_id") listId: Int): Call<ShoppingListRemoved>

    @GET("GetAllMyShopLists")
    fun getAllMyShopLists(@Query("key") key: String): Call<ShoppingListsList>

    @GET("GetShoppingList")
    fun getShoppingListById(@Query("list_id") listId: Int): Call<ShoppingListById>

    @POST("AddToShoppingList")
    fun addToShoppingList(@Query("id") id: Int, @Query("value") name: String, @Query("n") n: Int)
            : Call<ItemAdded>

    @POST("CrossItOff")
    fun crossOffItem(@Query("id") id: Int): Call<ItemCrossedOff>

}