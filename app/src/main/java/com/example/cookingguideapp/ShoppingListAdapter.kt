package com.example.cookingguideapp

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ShoppingListAdapter(private val ingredients: MutableList<Ingredient>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientName: TextView = view.findViewById(R.id.ingredientName)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)

        val checkBox: CheckBox = view.findViewById(R.id.ingredientCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val ingredient = ingredients[position]

        // Replace the contents of the view with that element
        holder.ingredientName.text = ingredient.name
        holder.checkBox.isChecked = ingredient.isSelected
        holder.quantityTextView.text = ingredient.quantity // Set the quantityTextView

        // Handle CheckBox click
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            ingredient.isSelected = isChecked  // Update the ingredient's selected state
        }
    }

    override fun getItemCount() = ingredients.size

    // Method to add an ingredient to the adapter
    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        notifyItemInserted(ingredients.size - 1)
    }

    // Method to remove selected ingredients


    // Method to update the entire list of ingredients
    fun setIngredients(newIngredients: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(newIngredients)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    // Method to remove selected ingredients
    fun removeSelectedItems() {
        val selectedIngredients = ingredients.filter { it.isSelected }
        selectedIngredients.forEach { ingredient ->
            if (ingredient.documentId.isNotEmpty()) {
                // Access Firestore instance and remove the document
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val db = Firebase.firestore
                    db.collection("users").document(userId)
                        .collection("shoppingList").document(ingredient.documentId)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document with ID: ${ingredient.documentId} successfully deleted")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting document", e)
                        }
                }
            }
        }

        // Remove selected ingredients from the local list and update the RecyclerView
        ingredients.removeAll { it.isSelected }
        notifyDataSetChanged()
    }

}
