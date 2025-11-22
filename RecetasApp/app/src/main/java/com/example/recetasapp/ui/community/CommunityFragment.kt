package com.example.recetasapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.remote.api.RetrofitClient
import com.example.recetasapp.data.repository.RecipeRepository
import com.example.recetasapp.ui.home.adapters.RecipeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommunityFragment : Fragment() {

    private lateinit var viewModel: CommunityViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var uploadFab: FloatingActionButton
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
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
        val factory = CommunityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CommunityViewModel::class.java]
    }

    private fun initViews(view: View) {
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        uploadFab = view.findViewById(R.id.upload_fab)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            // Navegar a detalle de receta
            val action = CommunityFragmentDirections.actionCommunityToRecipeDetail(recipe.id)
            findNavController().navigate(action)
        }

        recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    private fun setupListeners() {
        uploadFab.setOnClickListener {
            // Navegar a subir receta
            findNavController().navigate(R.id.uploadRecipeFragment)
        }
    }

    private fun setupObservers() {
        viewModel.communityRecipes.observe(viewLifecycleOwner) { recipes ->
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