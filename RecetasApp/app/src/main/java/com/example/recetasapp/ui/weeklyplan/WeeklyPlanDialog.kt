package com.example.recetasapp.ui.weeklyplan

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.recetasapp.data.model.DayMeals
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.data.model.WeeklyPlan
import com.example.recetasapp.databinding.DialogWeeklyPlanBinding
import com.example.recetasapp.databinding.ItemPlanDayBinding
import com.example.recetasapp.R

class WeeklyPlanDialog(
    context: Context,
    private val breakfasts: List<Recipe>,
    private val lunches: List<Recipe>,
    private val dinners: List<Recipe>,
    private val onSave: (WeeklyPlan) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogWeeklyPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogWeeklyPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Solo los nombres de recetas en cada categoría
        val breakfastNames = breakfasts.map { it.name }
        val lunchNames     = lunches.map { it.name }
        val dinnerNames    = dinners.map { it.name }

        val breakfastAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, breakfastNames)
        breakfastAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val lunchAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, lunchNames)
        lunchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val dinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, dinnerNames)
        dinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Obtén binding de cada día
        val mondayBinding    = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeMonday))
        val tuesdayBinding   = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeTuesday))
        val wednesdayBinding = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeWednesday))
        val thursdayBinding  = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeThursday))
        val fridayBinding    = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeFriday))
        val saturdayBinding  = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeSaturday))
        val sundayBinding    = ItemPlanDayBinding.bind(binding.root.findViewById(R.id.includeSunday))

        val daysBindings = listOf(
            Pair("Lunes", mondayBinding),
            Pair("Martes", tuesdayBinding),
            Pair("Miércoles", wednesdayBinding),
            Pair("Jueves", thursdayBinding),
            Pair("Viernes", fridayBinding),
            Pair("Sábado", saturdayBinding),
            Pair("Domingo", sundayBinding)
        )

        // Asigna nombre y adapters a cada día
        daysBindings.forEach { (dayName, dayBinding) ->
            dayBinding.tvDayTitle.text = dayName
            dayBinding.spinnerBreakfast.adapter = breakfastAdapter
            dayBinding.spinnerLunch.adapter = lunchAdapter
            dayBinding.spinnerDinner.adapter = dinnerAdapter
        }

        binding.btnSavePlan.setOnClickListener {
            val meals = daysBindings.associate { (dayName, dayBinding) ->
                dayName to DayMeals(
                    dayName = dayName,
                    breakfast = breakfasts.getOrNull(dayBinding.spinnerBreakfast.selectedItemPosition),
                    lunch     = lunches.getOrNull(dayBinding.spinnerLunch.selectedItemPosition),
                    dinner    = dinners.getOrNull(dayBinding.spinnerDinner.selectedItemPosition)
                )
            }
            val plan = WeeklyPlan(
                weekStartDate = System.currentTimeMillis(),
                meals = meals
            )
            onSave(plan)
            dismiss()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }
}