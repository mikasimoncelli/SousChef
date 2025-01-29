package com.example.cookingguideapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RecipeDetailFragment : Fragment() {

    private lateinit var favoriteButton: Button
    private lateinit var recipe: Recipe
    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe = arguments?.getSerializable("recipe") as? Recipe ?: return
        val recipeImageView = view.findViewById<ImageView>(R.id.recipeImageView)
        val recipeTitleTextView = view.findViewById<TextView>(R.id.recipeTitleTextView)
        val ingredientsContentTextView = view.findViewById<TextView>(R.id.ingredientsContentTextView)
        val uriContentTextView = view.findViewById<TextView>(R.id.uriContentTextView)
        favoriteButton = view.findViewById(R.id.favoritesButton)

        recipeTitleTextView.text = recipe.label
        ingredientsContentTextView.text = recipe.ingredientLines.joinToString("\n")
        uriContentTextView.text = recipe.url
        Log.d("RecipesAdapter", "Loading image URL: ${recipe.image}")

        Picasso.get().load(recipe.image).into(recipeImageView)

        uriContentTextView.setOnClickListener {
            openUrlInBrowser(recipe.url)
        }

        favoriteButton.setOnClickListener {
            updateRecipeFavoriteStatus()
        }

        checkFavoriteStatus()

        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val addToShoppingListButton = view.findViewById<Button>(R.id.addToShoppingListButton)
        addToShoppingListButton.setOnClickListener {
            addToShoppingList()
        }
    }

    private fun openUrlInBrowser(originalUrl: String) {
        var url = originalUrl
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun checkFavoriteStatus() {
        user?.let { user ->
            val sanitizedUri = sanitizeUri(recipe.uri)
            firestore.collection("users")
                .document(user.uid)
                .collection("favorite_recipes")
                .document(sanitizedUri)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        updateFavoriteButton(true)
                    } else {
                        updateFavoriteButton(false)
                    }
                }
        }
    }

    private fun updateRecipeFavoriteStatus() {
        user?.let { user ->
            val sanitizedUri = sanitizeUri(recipe.uri)
            val recipeRef = firestore.collection("users")
                .document(user.uid)
                .collection("favorite_recipes")
                .document(sanitizedUri)

            recipeRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    removeFromFavorites()  // Call without passing recipeRef
                } else {
                    addToFavorites()  // Call without passing recipeRef
                }
            }
        } ?: Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
    }


    private fun addToFavorites() {
        user?.uid?.let { userId ->
            val sanitizedUri = sanitizeUri(recipe.uri)
            val recipeRef = firestore.collection("users")
                .document(userId)
                .collection("favorite_recipes")
                .document(sanitizedUri)  // Use the sanitized URI as the document ID

            recipeRef.set(recipe)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Recipe added to favorites!", Toast.LENGTH_SHORT).show()
                    updateFavoriteButton(true)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to add recipe to favorites", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun removeFromFavorites() {
        user?.uid?.let { userId ->
            val sanitizedUri = sanitizeUri(recipe.uri)
            val recipeRef = firestore.collection("users")
                .document(userId)
                .collection("favorite_recipes")
                .document(sanitizedUri)  // Use the sanitized URI as the document ID

            recipeRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Recipe removed from favorites!", Toast.LENGTH_SHORT).show()
                    updateFavoriteButton(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to remove recipe from favorites", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            favoriteButton.text = "Remove"
        } else {
            favoriteButton.text = "Favorites"
        }
    }

    private fun addToShoppingList() {
        user?.let { user ->

            val shoppingListRef = firestore.collection("users").document(user.uid).collection("shoppingList")

            recipe.ingredientLines.forEach { ingredientLine ->
                // Regex pattern to match numbers followed by optional specific words like 'cup' or 'teaspoon'
                val regex = Regex("^[0-9\\s/]+(cup|g|teaspoons|tablespoons|tablespoon|tsps|tbsp|teaspoon|oz|grams|ml)?")
                val matchResult = regex.find(ingredientLine)
                val quantity = matchResult?.value?.trim() ?: ""
                val name = ingredientLine.removePrefix(quantity).trim()

                val ingredientData = hashMapOf("name" to name, "quantity" to quantity)
                shoppingListRef.add(ingredientData).addOnSuccessListener {
                    Log.d(TAG, "Ingredient added to shopping list")
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding ingredient to shopping list", e)
                }
            }
            Toast.makeText(context, "Ingredients added to shopping list", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
    }



    fun sanitizeUri(uri: String): String {
        return uri.replace("/", "_")
            .replace(".", "_")

    }
}
