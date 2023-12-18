package com.mshukshina.shoppinglistapiapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mshukshina.shoppinglistapiapp.databinding.SingleShoppingListItemBinding
import com.mshukshina.shoppinglistapiapp.retrofit.Item

class SingleShoppingListItemsAdapter : RecyclerView.Adapter<SingleShoppingListItemsAdapter.SingleShoppingListItemsViewHolder>() {

    private var itemsList = ArrayList<Item>()
    fun setList(list: ArrayList<Item>) {
        itemsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleShoppingListItemsViewHolder {
        val binding = SingleShoppingListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SingleShoppingListItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleShoppingListItemsViewHolder, position: Int) {
        val list = itemsList[position]
        holder.itemName.text = list.name

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class SingleShoppingListItemsViewHolder(binding: SingleShoppingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val itemName = binding.itemNameTv
    }
}