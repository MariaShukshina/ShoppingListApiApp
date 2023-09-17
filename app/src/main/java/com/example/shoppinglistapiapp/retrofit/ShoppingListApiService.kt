package com.example.shoppinglistapiapp.retrofit

import com.example.shoppinglistapiapp.models.ItemToAdd
import com.example.shoppinglistapiapp.models.ShoppingList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingListApiService {

    @GET("CreateTestKey")
    fun getTestKey(): Call<String>

    @GET("Authentication")
    fun authenticate(@Query("key") key: String): Call<Authentication>

    @POST("CreateShoppingList")
    fun createShoppingList(@Body shoppingList: ShoppingList): Call<ShoppingListCreated>

    @DELETE("RemoveShoppingList")
    fun removeShoppingList(@Query("list_id") listId: Int): Call<ShoppingListRemoved>

    @GET("GetAllMyShopLists")
    fun getAllMyShopLists(@Query("key") key: String): Call<ShoppingListsList>

    @GET("GetShoppingList")
    fun getShoppingListById(@Query("list_id") listId: Int): Call<ShoppingListById>

    @POST("AddToShoppingList")
    fun addToShoppingList(@Body itemToAdd: ItemToAdd): Call<ItemAdded>

    @DELETE("CrossItOff")
    fun crossOffItem(@Query("id") id: Int, @Query("value") value: String)
            : Call<ItemCrossedOff>

}