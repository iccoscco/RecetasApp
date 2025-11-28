package com.example.recetasapp.ui.weeklyplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recetasapp.databinding.FragmentWeeklyPlanBinding
import com.example.recetasapp.data.model.WeeklyPlan
import com.example.recetasapp.data.repository.RecipeRepository
import com.example.recetasapp.data.remote.api.MealDbApi
import com.example.recetasapp.ui.home.HomeViewModel
import com.example.recetasapp.ui.home.HomeViewModelFactory
import com.example.recetasapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeeklyPlanFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyPlanBinding
    private lateinit var viewModel: WeeklyPlanViewModel
    private lateinit var recipesViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyPlanBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[WeeklyPlanViewModel::class.java]

        // Instanciación correcta de la API MealDbApi usando Retrofit
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealDbApi::class.java)

        val recipeRepository = RecipeRepository(api)
        val factory = HomeViewModelFactory(recipeRepository)
        recipesViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        val adapter = WeeklyPlanAdapter(emptyList()) { plan ->
            viewModel.removeWeeklyPlan(plan)
        }

        binding.rvWeeklyPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeeklyPlans.adapter = adapter

        viewModel.plans.observe(viewLifecycleOwner) { plans ->
            adapter.updateData(plans)
        }

        recipesViewModel.recipes.observe(viewLifecycleOwner) { allRecipes ->
            binding.fabAddPlan.isEnabled = allRecipes.isNotEmpty()
            binding.fabAddPlan.setOnClickListener {
                WeeklyPlanDialog(
                    requireContext(),
                    allRecipes,
                    allRecipes,
                    allRecipes
                ) { newPlan ->
                    viewModel.addWeeklyPlan(newPlan)
                }.show()
            }
        }

        return binding.root
    }
}