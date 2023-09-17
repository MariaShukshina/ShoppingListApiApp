package com.example.shoppinglistapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppinglistapiapp.databinding.ActivityShoppingListBinding

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}