package com.example.recetasapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.ui.home.adapters.RecipeAdapter
import com.example.recetasapp.utils.PreferencesHelper

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupRecyclerView()
        setupObservers()
    }

    private fun initViewModel() {
        val preferencesHelper = PreferencesHelper(requireContext())
        val factory = FavoritesViewModelFactory(preferencesHelper)
        viewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
    }

    private fun initViews(view: View) {
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            // Si quieres navegar:
            // val action = FavoritesFragmentDirections.actionFavoritesToRecipeDetail(recipe.id)
            // findNavController().navigate(action)
        }

        recipesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
        }
    }

    private fun setupObservers() {
        viewModel.favoriteRecipes.observe(viewLifecycleOwner) { recipes ->
            // recipes es List<Recipe>
            recipeAdapter.submitList(recipes)
            if (recipes.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recipesRecyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recipesRecyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
