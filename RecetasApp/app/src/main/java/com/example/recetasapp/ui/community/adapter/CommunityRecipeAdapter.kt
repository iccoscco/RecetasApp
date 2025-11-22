package com.example.recetasapp.ui.community.adapter

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

class CommunityRecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit,
    private val onLikeClick: (Recipe) -> Unit,
    private val onCommentClick: (Recipe) -> Unit
) : ListAdapter<Recipe, CommunityRecipeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community_post, parent, false)
        return ViewHolder(view, onRecipeClick, onLikeClick, onCommentClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onRecipeClick: (Recipe) -> Unit,
        private val onLikeClick: (Recipe) -> Unit,
        private val onCommentClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
        private val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        private val recipeDescription: TextView = itemView.findViewById(R.id.recipe_description)
        private val likeButton: ImageView = itemView.findViewById(R.id.like_button)
        private val commentButton: ImageView = itemView.findViewById(R.id.comment_button)
        private val likesCount: TextView = itemView.findViewById(R.id.likes_count)

        fun bind(recipe: Recipe) {
            userName.text = recipe.author ?: "Usuario"
            recipeName.text = recipe.name
            recipeDescription.text = recipe.instructions.take(100) + "..."
            likesCount.text = "0" // Implementar likes reales

            recipeImage.loadUrl(recipe.imageUrl)

            itemView.setOnClickListener { onRecipeClick(recipe) }
            likeButton.setOnClickListener { onLikeClick(recipe) }
            commentButton.setOnClickListener { onCommentClick(recipe) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}