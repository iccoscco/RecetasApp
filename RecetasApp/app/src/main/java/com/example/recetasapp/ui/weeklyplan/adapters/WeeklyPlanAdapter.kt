package com.example.recetasapp.ui.weeklyplan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Recipe

class WeeklyPlanAdapter(
    private val onMealClick: (Recipe) -> Unit
) : ListAdapter<Recipe, WeeklyPlanAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weekly_meal, parent, false)
        return ViewHolder(view, onMealClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onMealClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val dayName: TextView = itemView.findViewById(R.id.day_name)
        private val breakfastMeal: TextView = itemView.findViewById(R.id.breakfast_meal)
        private val lunchMeal: TextView = itemView.findViewById(R.id.lunch_meal)
        private val dinnerMeal: TextView = itemView.findViewById(R.id.dinner_meal)

        fun bind(recipe: Recipe) {
            dayName.text = "Lunes" // TODO: Implementar día real
            breakfastMeal.text = recipe.name
            lunchMeal.text = "Sin planificar"
            dinnerMeal.text = "Sin planificar"

            itemView.setOnClickListener {
                onMealClick(recipe)
            }
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