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
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.utils.loadUrl

// Adapter para RecyclerView que muestra una lista de recetas.
class RecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    // Infla el layout item_recipe_card y crea un RecipeViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view, onRecipeClick)
    }

    // Llama a bind() del ViewHolder para mostrar la receta correspondiente
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Contiene las vistas del item
    class RecipeViewHolder(
        itemView: View,
        private val onRecipeClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val imageView: ImageView = itemView.findViewById(R.id.recipe_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.recipe_name)
        private val timeTextView: TextView = itemView.findViewById(R.id.recipe_time)
        private val difficultyTextView: TextView = itemView.findViewById(R.id.recipe_difficulty)
        private val ratingTextView: TextView = itemView.findViewById(R.id.recipe_rating)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favorite_button)
        
        fun bind(recipe: Recipe) {
            nameTextView.text = recipe.name
            timeTextView.text = recipe.preparationTime
            difficultyTextView.text = recipe.difficulty
            ratingTextView.text = recipe.rating.toString()
            
            imageView.loadUrl(recipe.imageUrl)
            
            // Actualizar icono de favorito
            val favoriteIcon = if (recipe.isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
            favoriteButton.setImageResource(favoriteIcon)
            
            itemView.setOnClickListener {
                onRecipeClick(recipe)
            }
            
            favoriteButton.setOnClickListener {
                // TODO: Implementar toggle de favorito
            }
        }
    }

    // Permite que ListAdapter detecte cambios en la lista de recetas
    class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}
