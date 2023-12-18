package com.mshukshina.shoppinglistapiapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mshukshina.shoppinglistapiapp.databinding.ShoppingListLayoutBinding
import com.mshukshina.shoppinglistapiapp.models.ItemModel
import com.mshukshina.shoppinglistapiapp.retrofit.Item
import com.mshukshina.shoppinglistapiapp.retrofit.Shop

class ShoppingListAdapter: RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private var shoppingListList = arrayListOf<Shop>()

    var loadItems: (id: Int, callback: (ArrayList<Item>?) -> Unit) -> Unit = { _, _ -> }

    var addItemHandler : (item: ItemModel) -> Unit = {}

    var onDeleteItemClicked: (id: Int) -> Unit = {}

    var crossOutItemHandler: (shopId: Int) -> Unit = {}


    fun setList(list: List<Shop>) {
        shoppingListList = list.toCollection(arrayListOf())
        notifyDataSetChanged()
    }

    fun addShoppingList(shoppingList: Shop){
        shoppingListList.add(shoppingList)
        notifyItemInserted(shoppingListList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ShoppingListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val list = shoppingListList[position]
        holder.listNameTv.text = list.name
        val shoppingListItemsAdapter = ShoppingListItemsAdapter()

        loadItems(list.id) { items ->
            shoppingListItemsAdapter.setList(items ?: arrayListOf())
        }

        holder.deleteIcon.setOnClickListener {
            onDeleteItemClicked(list.id)
            shoppingListList.removeAt(position)
            notifyItemRemoved(position)
        }

        shoppingListItemsAdapter.crossOutItemHandler = {
            crossOutItemHandler(it)
        }


        holder.shoppingItemsRv.adapter = shoppingListItemsAdapter
        holder.shoppingItemsRv.layoutManager = LinearLayoutManager(holder.itemView.context,
            LinearLayoutManager.VERTICAL, false)


        holder.addShoppingItemBtn.setOnClickListener {
            if(holder.shoppingListNameEt.text.isNotEmpty() && holder.shoppingListQuantityEt.text.isNotEmpty()) {
                val item = Item("",
                    list.id, holder.shoppingListNameEt.text.toString() + " "
                            + holder.shoppingListQuantityEt.text.toString())
                shoppingListItemsAdapter.addItem(item)

                addItemHandler(
                    ItemModel(list.id, holder.shoppingListNameEt.text.toString(),
                    holder.shoppingListQuantityEt.text.toString().toInt())
                )

                holder.shoppingListNameEt.text.clear()
                holder.shoppingListQuantityEt.text.clear()
            }
        }

    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int {
        return shoppingListList.size
    }

    class ShoppingListViewHolder(binding: ShoppingListLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        val listNameTv = binding.listNameTv
        val deleteIcon = binding.deleteIcon
        val addShoppingItemBtn = binding.addShoppingItemBtn
        val shoppingListNameEt = binding.shoppingListNameEt
        val shoppingListQuantityEt = binding.shoppingListQuantityEt
        val shoppingItemsRv = binding.shoppingItemsRv
    }
}