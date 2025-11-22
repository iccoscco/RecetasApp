package com.example.recetasapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.data.model.Recipe
import com.example.recetasapp.ui.home.adapters.RecipeAdapter
import com.example.recetasapp.utils.PreferencesHelper
import com.example.recetasapp.utils.showToast

class CollectionFragment : Fragment() {

    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var collectionTitle: TextView
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var emptyView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())

        initViews(view)
        setupRecyclerView()
        loadCollection()
    }

    private fun initViews(view: View) {
        collectionTitle = view.findViewById(R.id.title)
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            //Navegar a detalle de receta
            //val action = CollectionFragmentDirections.actionFavoritesToRecipeDetail(recipe.id)
            //findNavController().navigate(action)
        }

        recipesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
        }
    }

    private fun loadCollection() {
        val favorites = preferencesHelper.getFavorites()

        if (favorites.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recipesRecyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recipesRecyclerView.visibility = View.VISIBLE
            recipeAdapter.submitList(favorites.filterNotNull() as List<Recipe?>?)
        }
    }
}