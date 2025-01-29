package com.example.cookingguideapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class FavoritesFragment : Fragment() {

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritesRecyclerView = view.findViewById<RecyclerView>(R.id.favoritesRecyclerView)

        favoritesAdapter = FavoritesAdapter(mutableListOf()) { recipe ->
            openRecipeDetail(recipe)
        }
        favoritesRecyclerView.adapter = favoritesAdapter
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)

        fetchFavoriteRecipes()
    }

    private fun openRecipeDetail(recipe: Recipe) {
        val detailFragment = RecipeDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("recipe", recipe)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchFavoriteRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val favoritesCollection = firestore.collection("users").document(userId)
                .collection("favorite_recipes")

            favoritesCollection.get().addOnSuccessListener { querySnapshot ->
                val favoriteRecipes = querySnapshot.toObjects(Recipe::class.java)
                favoritesAdapter.updateRecipes(favoriteRecipes)
            }.addOnFailureListener { exception ->

            }
        } else {

        }
    }




}
