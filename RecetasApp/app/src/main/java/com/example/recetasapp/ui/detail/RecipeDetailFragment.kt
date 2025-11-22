package com.example.recetasapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.remote.api.RetrofitClient
import com.example.recetasapp.data.repository.RecipeRepository
import com.example.recetasapp.ui.cookingmode.CookingModeActivity
import com.example.recetasapp.ui.detail.adapters.IngredientAdapter
import com.example.recetasapp.utils.loadUrl
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class RecipeDetailFragment : Fragment() {

    private val args: RecipeDetailFragmentArgs by navArgs()
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var ingredientAdapter: IngredientAdapter

    private lateinit var recipeImage: ImageView
    private lateinit var recipeName: TextView
    private lateinit var recipeTime: TextView
    private lateinit var recipeDifficulty: TextView
    private lateinit var recipeCategory: TextView
    private lateinit var recipeArea: TextView
    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var instructionsText: TextView
    private lateinit var caloriesText: TextView
    private lateinit var proteinsText: TextView
    private lateinit var carbsText: TextView
    private lateinit var cookingModeFab: ExtendedFloatingActionButton
    private lateinit var favoriteButton: ImageView
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupRecyclerView()
        setupListeners()
        setupObservers()

        // Cargar receta
        viewModel.loadRecipe(args.recipeId)
    }

    private fun initViewModel() {
        val repository = RecipeRepository(RetrofitClient.api)
        val factory = RecipeDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[RecipeDetailViewModel::class.java]
    }

    private fun initViews(view: View) {
        recipeImage = view.findViewById(R.id.recipe_image)
        recipeName = view.findViewById(R.id.recipe_name)
        recipeTime = view.findViewById(R.id.recipe_time)
        recipeDifficulty = view.findViewById(R.id.recipe_difficulty)
        recipeCategory = view.findViewById(R.id.recipe_category)
        recipeArea = view.findViewById(R.id.recipe_area)
        ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view)
        instructionsText = view.findViewById(R.id.instructions_text)
        caloriesText = view.findViewById(R.id.calories_text)
        proteinsText = view.findViewById(R.id.proteins_text)
        carbsText = view.findViewById(R.id.carbs_text)
        cookingModeFab = view.findViewById(R.id.cooking_mode_fab)
        favoriteButton = view.findViewById(R.id.favorite_button)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter()
        ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ingredientAdapter
        }
    }

    private fun setupListeners() {
        cookingModeFab.setOnClickListener {
            // Navegar a Cooking Mode Activity
            val intent = Intent(requireContext(), CookingModeActivity::class.java)
            intent.putExtra("recipeId", args.recipeId)
            startActivity(intent)
        }

        favoriteButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupObservers() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                recipeName.text = it.name
                recipeTime.text = it.preparationTime
                recipeDifficulty.text = it.difficulty
                recipeCategory.text = it.category
                recipeArea.text = it.area
                instructionsText.text = it.instructions
                caloriesText.text = "${it.calories ?: 450} kcal"
                proteinsText.text = it.proteins ?: "30g"
                carbsText.text = it.carbohydrates ?: "40g"

                recipeImage.loadUrl(it.imageUrl)
                ingredientAdapter.submitList(it.ingredients)

                updateFavoriteIcon(it.isFavorite)
            }
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

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        favoriteButton.setImageResource(icon)
    }
}