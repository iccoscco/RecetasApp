package com.example.recetasapp.ui.weeklyplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.data.model.WeeklyPlan
import com.example.recetasapp.databinding.ItemWeeklyPlanBinding

class WeeklyPlanAdapter(
    private var plans: List<WeeklyPlan>,
    private val onDelete: (WeeklyPlan) -> Unit
) : RecyclerView.Adapter<WeeklyPlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding: ItemWeeklyPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: WeeklyPlan) {
            binding.tvPlanWeek.text = "Semana: ${plan.weekStartDate.toFechaLegible()}"
            binding.tvPlanMeals.text = plan.meals.entries.joinToString("\n") { (day, meals) ->
                """$day:
  Desayuno: ${meals.breakfast?.name ?: "-"}
  Almuerzo: ${meals.lunch?.name ?: "-"}
  Cena: ${meals.dinner?.name ?: "-"}"""
            }
            binding.btnDelete.setOnClickListener { onDelete(plan) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemWeeklyPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount() = plans.size

    fun updateData(newPlans: List<WeeklyPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}

// Extensión para mostrar la semana como fecha legible:
fun Long.toFechaLegible(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(this))
}