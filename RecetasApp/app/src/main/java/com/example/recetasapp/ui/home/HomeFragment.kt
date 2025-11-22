package com.example.recetasapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.remote.api.RetrofitClient
import com.example.recetasapp.data.repository.RecipeRepository
import com.example.recetasapp.ui.home.adapters.CategoryAdapter
import com.example.recetasapp.ui.home.adapters.RecipeAdapter

// Fragment principal donde se muestran recetas y categorías.
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var loadingOverlay: View
    private lateinit var searchCard: View

    // Infla el layout fragment_home.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel() // Inicializa el view model
        initViews(view) // Inicializa las vistas
        setupRecyclerViews() // Configura RecyclerView
        setupListeners() // Configura los listeners
        setupObservers() // Configura los Observers
    }

    private fun initViewModel() {
        val repository = RecipeRepository(RetrofitClient.api) // Crea el RecipeRepository con la API de Retrofit
        val factory = HomeViewModelFactory(repository) //Inicializa HomeViewModel usando su Factory.
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    // Vincula los elementos de UI (RecyclerView, loadingOverlay, searchCard) con las variables del fragmento.
    private fun initViews(view: View) {
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        searchCard = view.findViewById(R.id.search_card)

        // TODO: OCULTAR la barra verde superior (AppBarLayout)
        //val appBarLayout = view.findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.app_bar_layout)
        //appBarLayout?.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        // Recetas con RecyclerView (Horizontal)
        recipeAdapter = RecipeAdapter { recipe ->
            //Navegar la receta
            val action = HomeFragmentDirections.actionHomeToRecipeDetail(recipe.id)
            findNavController().navigate(action)
        }

        recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }

        // Categories con RecyclerView (Grid 2 columnas)
        categoryAdapter = CategoryAdapter { category ->
            viewModel.loadRecipesByCategory(category.name)
        }

        categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        searchCard.setOnClickListener {
            // Ir a Search Fragment(Búqueda)
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun setupObservers() {
        viewModel.recipes.observe(viewLifecycleOwner) { recipes -> //actualiza lista de recetas
            recipeAdapter.submitList(recipes)
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories -> //actualiza lista de categorías
            categoryAdapter.submitList(categories)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading -> // muestra/oculta overlay de carga
            loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error -> // muestra toast si hay error
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}