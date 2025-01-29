package com.example.cookingguideapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecipesAdapter(
    private val recipes: MutableList<Recipe>,
    private val clickListener: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(view: View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.recipeImageView)
        val textView: TextView = view.findViewById(R.id.recipeTextView)

        init {
            view.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
    }

    // onCreateViewHolder method
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view) { position ->
            // Ensure position is valid
            if (position != RecyclerView.NO_POSITION) {
                val recipe = recipes[position]
                clickListener(recipe)
            }
        }
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    // onBindViewHolder method
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.textView.text = recipe.label

        Picasso.get()
            .load(recipe.image)
            .into(holder.imageView)
    }

    // getItemCount method
    override fun getItemCount() = recipes.size




}
