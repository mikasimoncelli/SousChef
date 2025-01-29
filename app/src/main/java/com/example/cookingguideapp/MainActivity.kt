package com.example.cookingguideapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.navigation)
        bottomNavigationView.visibility = View.GONE


        if (savedInstanceState == null) {

            navigateToRecipes()
        }


        setupBottomNavigation()


        if (FirebaseAuth.getInstance().currentUser == null) {

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_recipes -> {
                    navigateToRecipes()
                    true
                }
                R.id.navigation_favorites -> {
                    navigateToFavorites()
                    true
                }
                R.id.navigation_shopping_list -> {
                    navigateToShoppingList()
                    true
                }
                R.id.navigation_account -> {
                    navigateToAccount()
                    true
                }
                else -> false
            }
        }
    }

    fun navigateToRecipes() {
        bottomNavigationView.visibility = View.VISIBLE
        val recipesFragment = RecipesFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, recipesFragment)
            .commit()
    }

    private fun navigateToFavorites() {
        bottomNavigationView.visibility = View.VISIBLE
        val favoritesFragment = FavoritesFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, favoritesFragment)
            .commit()
    }

    private fun navigateToShoppingList() {
        bottomNavigationView.visibility = View.VISIBLE
        val shoppingListFragment = ShoppingListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, shoppingListFragment)
            .commit()
    }

    fun navigateToAccount() {
        bottomNavigationView.visibility = View.VISIBLE
        val accountFragment = AccountFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, accountFragment)
            .commit()
    }

    fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
    }


    fun showWelcomeFragment(savedInstanceState: Bundle?) {
        if (findViewById<FrameLayout>(R.id.fragment_container) != null && savedInstanceState == null) {
            val welcomeFragment = WelcomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, welcomeFragment)
                .commit()
        }
    }
}
