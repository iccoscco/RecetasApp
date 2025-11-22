package com.example.recetasapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.remote.api.RetrofitClient
import com.example.recetasapp.data.repository.RecipeRepository
import com.example.recetasapp.ui.home.adapters.RecipeAdapter

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var searchEditText: EditText
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
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
        val repository = RecipeRepository(RetrofitClient.api)
        val factory = SearchViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    private fun initViews(view: View) {
        searchEditText = view.findViewById(R.id.search_edit_text)
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            val action = SearchFragmentDirections.actionSearchToRecipeDetail(recipe.id)
            findNavController().navigate(action)
        }

        recipesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
        }
    }

    private fun setupListeners() {
        searchEditText.addTextChangedListener { text ->
            val query = text.toString().trim()
            if (query.length > 2) {
                viewModel.searchRecipes(query)
            }
        }
    }

    private fun setupObservers() {
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.submitList(recipes)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}