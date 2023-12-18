package com.mshukshina.shoppinglistapiapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshukshina.shoppinglistapiapp.presentation.adapters.ShoppingListAdapter
import com.mshukshina.shoppinglistapiapp.presentation.adapters.SingleShoppingListItemsAdapter
import com.mshukshina.shoppinglistapiapp.databinding.ActivityShoppingListBinding
import com.mshukshina.shoppinglistapiapp.retrofit.ItemAdded
import com.mshukshina.shoppinglistapiapp.retrofit.ItemCrossedOff
import com.mshukshina.shoppinglistapiapp.retrofit.Retrofit
import com.mshukshina.shoppinglistapiapp.retrofit.Shop
import com.mshukshina.shoppinglistapiapp.retrofit.ShoppingListById
import com.mshukshina.shoppinglistapiapp.retrofit.ShoppingListCreated
import com.mshukshina.shoppinglistapiapp.retrofit.ShoppingListRemoved
import com.mshukshina.shoppinglistapiapp.retrofit.ShoppingListsList
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

        val adapter = ShoppingListAdapter()

        setupRecyclerView(adapter)

        binding.getAllButton.setOnClickListener {
            binding.shoppingListsRv.visibility = View.VISIBLE
            binding.singleShoppingListRv.visibility = View.GONE
            if(key != null) {
                Retrofit.retrofitService.getAllMyShopLists(key).enqueue(object:
                    Callback<ShoppingListsList> {
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
                    override fun onResponse(call: Call<ShoppingListById>,
                                            response: Response<ShoppingListById>) {
                        if(response.isSuccessful) {
                            val shoppingList = response.body()
                            if(shoppingList != null) {
                                binding.shoppingListsRv.visibility = View.GONE
                                binding.singleShoppingListRv.visibility = View.VISIBLE

                                val singleListAdapter = SingleShoppingListItemsAdapter()
                                binding.singleShoppingListRv.adapter = singleListAdapter
                                binding.singleShoppingListRv.layoutManager = LinearLayoutManager(
                                    this@ShoppingListActivity,
                                    LinearLayoutManager.VERTICAL, false)
                                singleListAdapter.setList(shoppingList.item_list)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ShoppingListById>, t: Throwable) {
                        Toast.makeText(this@ShoppingListActivity,
                            "Failed to get list", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a list id", Toast.LENGTH_SHORT).show()
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
                                adapter.addShoppingList(
                                    Shop("", listId!!,
                                    binding.shoppingListNameEt.text.toString())
                                )
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
                Toast.makeText(this, "Please enter a name for the list",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(adapter: ShoppingListAdapter) {
        binding.shoppingListsRv.adapter = adapter
        binding.shoppingListsRv.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        adapter.onDeleteItemClicked = {
            Retrofit.retrofitService.removeShoppingList(it).enqueue(object :
                Callback<ShoppingListRemoved> {
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
        adapter.loadItems = { id, callback ->
            Retrofit.retrofitService.getShoppingListById(id).enqueue(object :
                Callback<ShoppingListById> {
                override fun onResponse(
                    call: Call<ShoppingListById>,
                    response: Response<ShoppingListById>
                ) {
                    if (response.isSuccessful) {
                        val shoppingList = response.body()
                        if (shoppingList != null) {
                            val items = shoppingList.item_list
                            callback(items)
                        }
                    }
                }

                override fun onFailure(call: Call<ShoppingListById>, t: Throwable) {
                    Toast.makeText(this@ShoppingListActivity, "Failed to get list", Toast.LENGTH_SHORT).show()
                    callback(null) // Notify the callback with null data in case of failure
                }
            })
        }
        adapter.addItemHandler = {
            Retrofit.retrofitService.addToShoppingList(it.id, it.name + " " + it.n,
                it.n).enqueue(object : Callback<ItemAdded> {
                override fun onResponse(
                    call: Call<ItemAdded>,
                    response: Response<ItemAdded>
                ) {
                    Log.i("ShoppingListActivity", "${response.body()?.item_id}")
                }

                override fun onFailure(call: Call<ItemAdded>, t: Throwable) {
                    Toast.makeText(this@ShoppingListActivity,
                        "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            })
        }
        adapter.crossOutItemHandler = {
            Retrofit.retrofitService.crossOffItem(it).enqueue(object : Callback<ItemCrossedOff> {
                override fun onResponse(
                    call: Call<ItemCrossedOff>,
                    response: Response<ItemCrossedOff>
                ) {
                    Log.i("ShoppingListActivity", "${response.body()?.success}")
                }

                override fun onFailure(call: Call<ItemCrossedOff>, t: Throwable) {
                    Toast.makeText(this@ShoppingListActivity,
                        "Failed to cross out item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}