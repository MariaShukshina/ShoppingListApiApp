package com.example.shoppinglistapiapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapiapp.databinding.ShoppingListItemLayoutBinding
import com.example.shoppinglistapiapp.retrofit.Item


class ShoppingListItemsAdapter: RecyclerView.Adapter<ShoppingListItemsAdapter
.ShoppingListItemsViewHolder>() {

    private var shoppingItems = arrayListOf<Item>()


    fun setList(list: ArrayList<Item>){
        shoppingItems = list
        notifyDataSetChanged()
    }

    fun addItem(item: Item){
        shoppingItems.add(item)
        notifyItemInserted(shoppingItems.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemsViewHolder {
        val binding = ShoppingListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ShoppingListItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListItemsViewHolder, position: Int) {
        val list = shoppingItems[position]
        holder.itemNameTv.text = list.name
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    class ShoppingListItemsViewHolder(binding: ShoppingListItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
            val itemNameTv = binding.tvToDoTitle
    }
}