package com.example.cookingguideapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment



class RecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recipes)


//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.recipe_fragment_container, RecipesFragment())
                .commit()
        }
    }
}
