package com.example.shoppinglistapiapp.retrofit

data class ShoppingListById(
    val item_list: List<Item>,
    val success: Boolean
)