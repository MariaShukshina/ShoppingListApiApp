package com.example.shoppinglistapiapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapiapp.databinding.ShoppingListLayoutBinding
import com.example.shoppinglistapiapp.retrofit.Shop

class ShoppingListItemsAdapter: RecyclerView.Adapter<ShoppingListItemsAdapter
    .ShoppingListItemsViewHolder>() {

    private var shoppingListList = arrayListOf<Shop>()
    var onDeleteItemClicked: (id: Int) -> Unit = {}

    fun setList(list: List<Shop>){
        shoppingListList = list.toCollection(arrayListOf())
        notifyDataSetChanged()
    }

    fun addShoppingList(shoppingList: Shop){
        shoppingListList.add(shoppingList)
        notifyItemInserted(shoppingListList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemsViewHolder {
        val binding = ShoppingListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ShoppingListItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListItemsViewHolder, position: Int) {
        val list = shoppingListList[position]
        holder.listNameTv.text = list.name

        holder.deleteIcon.setOnClickListener {
            onDeleteItemClicked(list.id)
            shoppingListList.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return shoppingListList.size
    }

    class ShoppingListItemsViewHolder(binding: ShoppingListLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        val listNameTv = binding.listNameTv
        val deleteIcon = binding.deleteIcon
    }
}