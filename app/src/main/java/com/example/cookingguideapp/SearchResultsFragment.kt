package com.example.cookingguideapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchResultsFragment : Fragment() {

    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.search_results, container, false)

        // Get the search results passed to this fragment
        val searchResults = arguments?.getSerializable("searchResults") as? List<Recipe>

        // Initialize the RecyclerView and the adapter
        val recipesRecyclerView = view.findViewById<RecyclerView>(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(context)

        recipesAdapter = RecipesAdapter(searchResults.orEmpty().toMutableList()) { recipe ->

        }

        recipesRecyclerView.adapter = recipesAdapter

        return view
    }

    // Method to update the list of recipes in the adapter
    fun updateSearchResults(newRecipes: List<Recipe>) {
        recipesAdapter.updateRecipes(newRecipes)
    }
}
