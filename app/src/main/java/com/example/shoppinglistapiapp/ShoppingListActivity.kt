package com.example.shoppinglistapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglistapiapp.adapters.ShoppingListItemsAdapter
import com.example.shoppinglistapiapp.databinding.ActivityShoppingListBinding
import com.example.shoppinglistapiapp.retrofit.Retrofit
import com.example.shoppinglistapiapp.retrofit.Shop
import com.example.shoppinglistapiapp.retrofit.ShoppingListById
import com.example.shoppinglistapiapp.retrofit.ShoppingListCreated
import com.example.shoppinglistapiapp.retrofit.ShoppingListRemoved
import com.example.shoppinglistapiapp.retrofit.ShoppingListsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        var listId: Int?

        val adapter = ShoppingListItemsAdapter()

        setupRecyclerView(adapter)

        binding.getAllButton.setOnClickListener {
            if(key != null) {
                Retrofit.retrofitService.getAllMyShopLists(key).enqueue(object: Callback<ShoppingListsList> {
                    override fun onResponse(
                        call: Call<ShoppingListsList>,
                        response: Response<ShoppingListsList>
                    ) {
                        if(response.isSuccessful) {
                            val shoppingLists = response.body()?.shop_list
                            if(shoppingLists != null) {
                                adapter.setList(shoppingLists)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ShoppingListsList>, t: Throwable) {
                        Toast.makeText(this@ShoppingListActivity,
                            "Failed to get all lists", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.searchButton.setOnClickListener {
            if (binding.shoppingActivitySearchView.query.isNotEmpty()) {
                Retrofit.retrofitService.getShoppingListById(binding.shoppingActivitySearchView.query
                    .toString().toInt()).enqueue(object: Callback<ShoppingListById> {
                    override fun onResponse(call: Call<ShoppingListById>, response: Response<ShoppingListById>) {
                        if(response.isSuccessful) {
                            val shoppingList = response.body()
                            if(shoppingList != null) {
                                //
                            }
                        }
                    }

                    override fun onFailure(call: Call<ShoppingListById>, t: Throwable) {
                        Toast.makeText(this@ShoppingListActivity,
                            "Failed to get list", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.addShoppingListBtn.setOnClickListener {
            if (binding.shoppingListNameEt.text.isNotEmpty() && !key.isNullOrEmpty()) {
                Retrofit.retrofitService.createShoppingList(
                    key, binding.shoppingListNameEt.text.toString()
                ).enqueue(object : Callback<ShoppingListCreated> {
                    override fun onResponse(
                        call: Call<ShoppingListCreated>,
                        response: Response<ShoppingListCreated>
                    ) {
                        if(response.isSuccessful){
                            listId = response.body()?.list_id
                            Log.i("ShoppingListActivity", "List created with id: $listId")
                            if(listId != null) {
                                Toast.makeText(this@ShoppingListActivity,
                                    "List created", Toast.LENGTH_SHORT).show()
                                adapter.addShoppingList(Shop("", listId!!,
                                    binding.shoppingListNameEt.text.toString()))
                                binding.shoppingListNameEt.text.clear()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ShoppingListCreated>, t: Throwable) {
                        Toast.makeText(this@ShoppingListActivity,
                            "Failed to create a list", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a name for the list", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(adapter: ShoppingListItemsAdapter) {
        binding.shoppingListsRv.adapter = adapter
        binding.shoppingListsRv.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter.onDeleteItemClicked = {
            Retrofit.retrofitService.removeShoppingList(it).enqueue(object : Callback<ShoppingListRemoved> {
                override fun onResponse(
                    call: Call<ShoppingListRemoved>,
                    response: Response<ShoppingListRemoved>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(this@ShoppingListActivity,
                            "List deleted", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ShoppingListRemoved>, t: Throwable) {
                    Toast.makeText(this@ShoppingListActivity,
                        "Failed to delete list", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}