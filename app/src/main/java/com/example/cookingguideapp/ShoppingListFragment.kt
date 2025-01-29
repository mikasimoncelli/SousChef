package com.example.cookingguideapp

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingListFragment : Fragment() {
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var ingredientInput: EditText
    private lateinit var quantityInput: EditText
    private lateinit var addButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.shoppingListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ShoppingListAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // Load the shopping list for the current user
        loadShoppingList()

        // Initialize EditTexts for ingredient, quantity,
        ingredientInput = view.findViewById(R.id.ingredientInput)
        quantityInput = view.findViewById(R.id.quantityInput)

        // Initialize addButton and set click listener
        addButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val ingredientName = ingredientInput.text.toString()
            val quantityText = quantityInput.text.toString()



            if (ingredientName.isNotEmpty() && quantityText.isNotEmpty()) {
                addIngredientToList(ingredientName, quantityText)
                ingredientInput.text.clear()
                quantityInput.text.clear()
            }
        }

        // Initialize removeButton and set click listener
        val removeButton = view.findViewById<Button>(R.id.removeSelectedButton)
        removeButton.setOnClickListener {
            adapter.removeSelectedItems()
        }
    }

    private fun addIngredientToList(ingredientName: String, quantityText: String) {
        val newIngredient = Ingredient(ingredientName, quantityText)
        adapter.addIngredient(newIngredient)
        hideKeyboard()
        // Prepare data for Firestore
        val ingredientMap = hashMapOf(
            "name" to newIngredient.name,
            "quantity" to newIngredient.quantity,
        )

        // Access Firestore instance and add a new document to the user's ingredients sub-collection
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val userIngredientRef = db.collection("users").document(userId)
                .collection("shoppingList")

            userIngredientRef.add(ingredientMap)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        } else {
            Log.w(TAG, "User not logged in, can't add ingredient")
        }
    }



    private fun loadShoppingList() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val shoppingListRef = db.collection("users").document(userId).collection("shoppingList")

            shoppingListRef.get()
                .addOnSuccessListener { documents ->
                    val shoppingList = mutableListOf<Ingredient>()
                    for (document in documents) {
                        val ingredient = document.toObject(Ingredient::class.java).apply {
                            documentId = document.id
                        }
                        shoppingList.add(ingredient)
                    }
                    adapter.setIngredients(shoppingList) // Update the adapter
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } else {
            Log.w(TAG, "User not logged in, can't load shopping list")
        }
    }


    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentFocusedView = activity?.currentFocus
        currentFocusedView?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
