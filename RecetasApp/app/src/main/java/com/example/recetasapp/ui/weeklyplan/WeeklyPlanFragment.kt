package com.example.recetasapp.ui.weeklyplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.ui.weeklyplan.adapters.WeeklyPlanAdapter
import com.example.recetasapp.utils.showToast

class WeeklyPlanFragment : Fragment() {

    private lateinit var viewModel: WeeklyPlanViewModel
    private lateinit var adapter: WeeklyPlanAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var generateShoppingListButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weekly_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun initViewModel() {
        val factory = WeeklyPlanViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[WeeklyPlanViewModel::class.java]
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.weekly_recycler_view)
        generateShoppingListButton = view.findViewById(R.id.generate_shopping_list_button)
    }

    private fun setupRecyclerView() {
        adapter = WeeklyPlanAdapter { meal ->
            requireContext().showToast("Meal: ${meal.name}")
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WeeklyPlanFragment.adapter
        }
    }

    private fun setupListeners() {
        generateShoppingListButton.setOnClickListener {
            viewModel.generateShoppingList()
        }
    }

    private fun setupObservers() {
        viewModel.weeklyMeals.observe(viewLifecycleOwner) { meals ->
            adapter.submitList(meals)
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                requireContext().showToast(it)
            }
        }
    }
}