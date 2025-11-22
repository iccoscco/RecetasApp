package com.example.recetasapp.ui.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Ingredient

class ShoppingItemAdapter(
    private val onItemChecked: (Ingredient, Boolean) -> Unit,
    private val onItemDeleted: (Ingredient) -> Unit
) : ListAdapter<Ingredient, ShoppingItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_item, parent, false)
        return ViewHolder(view, onItemChecked, onItemDeleted)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onItemChecked: (Ingredient, Boolean) -> Unit,
        private val onItemDeleted: (Ingredient) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val checkbox: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val nameText: TextView = itemView.findViewById(R.id.item_name)
        private val quantityText: TextView = itemView.findViewById(R.id.item_quantity)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(item: Ingredient) {
            nameText.text = item.name
            quantityText.text = item.quantity
            checkbox.isChecked = item.isChecked

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(item, isChecked)
            }

            deleteButton.setOnClickListener {
                onItemDeleted(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }
    }
}