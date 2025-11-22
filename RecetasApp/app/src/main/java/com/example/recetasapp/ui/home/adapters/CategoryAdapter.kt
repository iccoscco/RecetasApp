package com.example.recetasapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Category
import com.example.recetasapp.utils.loadUrl

// Adapter para RecyclerView que muestra una lista de categorías de recetas.
class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    // Infla el layout item_category y crea un CategoryViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view, onCategoryClick)
    }

    // Llama a bind() del ViewHolder para mostrar la categoría correspondiente
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Contiene las vistas del item (ImageView y TextView)
    class CategoryViewHolder(
        itemView: View,
        private val onCategoryClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val imageView: ImageView = itemView.findViewById(R.id.category_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.category_name)
        
        fun bind(category: Category) {
            nameTextView.text = category.name
            imageView.loadUrl(category.imageUrl)
            
            itemView.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }
    
    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
