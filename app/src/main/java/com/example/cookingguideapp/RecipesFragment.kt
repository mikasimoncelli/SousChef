package com.example.cookingguideapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.mutableSetOf
import android.widget.FrameLayout
import android.widget.LinearLayout

class RecipesFragment : Fragment() {
    private val selectedAllergies = mutableSetOf<String>()


    private val selectedDiets = mutableSetOf<String>()

    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipesRecyclerView = view.findViewById<RecyclerView>(R.id.recipesRecyclerView)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val searchButton = view.findViewById<Button>(R.id.searchButton)
        val allergyGridLayout = view.findViewById<GridLayout>(R.id.allergyGridLayout)
        val dietGridLayout = view.findViewById<GridLayout>(R.id.dietGridLayout)

        val selectDietButton = view.findViewById<Button>(R.id.selectDiet)
        selectDietButton.setOnClickListener {
            toggleDiet()


        }
//
//        val selectCaloriesButton = view.findViewById<Button>(R.id.selectCalories)
//
//        val selectNutritionButton = view.findViewById<Button>(R.id.selectNutrition)



        allergyGridLayout?.visibility = View.INVISIBLE
        dietGridLayout?.visibility = View.INVISIBLE

        val selectAllergiesButton = view.findViewById<Button>(R.id.selectAllergies)
        selectAllergiesButton.setOnClickListener {
            toggleViewAllergies()
        }


        val backButton: Button = view.findViewById(R.id.backButton)
        backButton?.visibility = View.INVISIBLE // Hide Back button

        backButton.setOnClickListener {
            toggleViewBackButton()
        }



        val checkBoxCeleryFree = view.findViewById<CheckBox>(R.id.celery_free)
        val checkBoxCrustaceanFree = view.findViewById<CheckBox>(R.id.crustacean_free)
        val checkBoxDairyFree = view.findViewById<CheckBox>(R.id.dairy_free)
        val checkBoxEggFree = view.findViewById<CheckBox>(R.id.egg_free)
        val checkBoxFishFree = view.findViewById<CheckBox>(R.id.fish_free)
        val checkBoxGlutenFree = view.findViewById<CheckBox>(R.id.gluten_free)
        val checkBoxLupineFree = view.findViewById<CheckBox>(R.id.lupine_free)
        val checkBoxMustardFree = view.findViewById<CheckBox>(R.id.mustard_free)
        val checkBoxPeanutFree = view.findViewById<CheckBox>(R.id.peanut_free)
        val checkBoxSesameFree = view.findViewById<CheckBox>(R.id.sesame_free)
        val checkBoxShellfishFree = view.findViewById<CheckBox>(R.id.shellfish_free)
        val checkBoxSoyFree = view.findViewById<CheckBox>(R.id.soy_free)
        val checkBoxTreeNutFree = view.findViewById<CheckBox>(R.id.tree_nut_free)
        val checkBoxWheatFree = view.findViewById<CheckBox>(R.id.wheat_free)



        val checkBoxAlcoholFree = view.findViewById<CheckBox>(R.id.alcohol_free)
        val checkBoxHighFiber = view.findViewById<CheckBox>(R.id.high_fiber)
        val checkBoxHighProtein = view.findViewById<CheckBox>(R.id.high_protein)
        val checkBoxKeto = view.findViewById<CheckBox>(R.id.keto)
        val checkBoxKidneyFriendly = view.findViewById<CheckBox>(R.id.kidney_friendly)
        val checkBoxKosher = view.findViewById<CheckBox>(R.id.kosher)
        val checkBoxLowCarb = view.findViewById<CheckBox>(R.id.low_carb)
        val checkBoxLowFat = view.findViewById<CheckBox>(R.id.low_fat)
        val checkBoxLowSodium = view.findViewById<CheckBox>(R.id.low_sodium)
        val checkBoxNoOilAdded = view.findViewById<CheckBox>(R.id.no_oil_added)
        val checkBoxNoSugar = view.findViewById<CheckBox>(R.id.no_sugar)
        val checkBoxPaleo = view.findViewById<CheckBox>(R.id.paleo)
        val checkBoxPescatarian = view.findViewById<CheckBox>(R.id.pescatarian)
        val checkBoxPorkFree = view.findViewById<CheckBox>(R.id.pork_free)
        val checkBoxRedMeatFree = view.findViewById<CheckBox>(R.id.red_meat_free)
        val checkBoxSugarConscious = view.findViewById<CheckBox>(R.id.sugar_conscious)
        val checkBoxVegan = view.findViewById<CheckBox>(R.id.vegan)
        val checkBoxVegetarian = view.findViewById<CheckBox>(R.id.vegetarian)



        checkBoxCeleryFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("celery-free")
            } else {
                selectedAllergies.remove("celery-free")
            }
        }

        checkBoxCrustaceanFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("crustacean-free")
            } else {
                selectedAllergies.remove("crustacean-free")
            }
        }

        checkBoxDairyFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("dairy-free")
            } else {
                selectedAllergies.remove("dairy-free")
            }
        }

        checkBoxEggFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("egg-free")
            } else {
                selectedAllergies.remove("egg-free")
            }
        }

        checkBoxFishFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("fish-free")
            } else {
                selectedAllergies.remove("fish-free")
            }
        }

        checkBoxGlutenFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("gluten-free")
            } else {
                selectedAllergies.remove("gluten-free")
            }
        }

        checkBoxLupineFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("lupine-free")
            } else {
                selectedAllergies.remove("lupine-free")
            }
        }

        checkBoxMustardFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("mustard-free")
            } else {
                selectedAllergies.remove("mustard-free")
            }
        }

        checkBoxPeanutFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("peanut-free")
            } else {
                selectedAllergies.remove("peanut-free")
            }
        }

        checkBoxSesameFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("sesame-free")
            } else {
                selectedAllergies.remove("sesame-free")
            }
        }

        checkBoxShellfishFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("shellfish-free")
            } else {
                selectedAllergies.remove("shellfish-free")
            }
        }

        checkBoxSoyFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("soy-free")
            } else {
                selectedAllergies.remove("soy-free")
            }
        }

        checkBoxTreeNutFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("tree-nut-free")
            } else {
                selectedAllergies.remove("tree-nut-free")
            }
        }

        checkBoxWheatFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAllergies.add("wheat-free")
            } else {
                selectedAllergies.remove("wheat-free")
            }
        }



        checkBoxAlcoholFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("alcohol-free")
            } else {
                selectedDiets.remove("alcohol-free")
            }
        }



        checkBoxHighFiber.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("high-fiber")
            } else {
                selectedDiets.remove("high-fiber")
            }
        }

        checkBoxHighProtein.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("high-protein")
            } else {
                selectedDiets.remove("high-protein")
            }
        }

        checkBoxKeto.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("keto")
            } else {
                selectedDiets.remove("keto")
            }
        }

        checkBoxKidneyFriendly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("kidney-friendly")
            } else {
                selectedDiets.remove("kidney-friendly")
            }
        }

        checkBoxKosher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("kosher")
            } else {
                selectedDiets.remove("kosher")
            }
        }

        checkBoxLowCarb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("low-carb")
            } else {
                selectedDiets.remove("low-carb")
            }
        }

        checkBoxLowFat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("low-fat")
            } else {
                selectedDiets.remove("low-fat")
            }
        }


        checkBoxLowSodium.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("low-sodium")
            } else {
                selectedDiets.remove("low-sodium")
            }
        }

        checkBoxNoOilAdded.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("no-oil-added")
            } else {
                selectedDiets.remove("no-oil-added")
            }
        }

        checkBoxNoSugar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("no-sugar")
            } else {
                selectedDiets.remove("no-sugar")
            }
        }

        checkBoxPaleo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("paleo")
            } else {
                selectedDiets.remove("paleo")
            }
        }

        checkBoxPescatarian.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("pescatarian")
            } else {
                selectedDiets.remove("pescatarian")
            }
        }

        checkBoxPorkFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("pork-free")
            } else {
                selectedDiets.remove("pork-free")
            }
        }

        checkBoxRedMeatFree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("red-meat-free")
            } else {
                selectedDiets.remove("red-meat-free")
            }
        }

        checkBoxSugarConscious.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("sugar-conscious")
            } else {
                selectedDiets.remove("sugar-conscious")
            }
        }

        checkBoxVegan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("vegan")
            } else {
                selectedDiets.remove("vegan")
            }
        }

        checkBoxVegetarian.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedDiets.add("vegetarian")
            } else {
                selectedDiets.remove("vegetarian")
            }
        }






        // Initialize RecyclerView and Adapter with the click listener
        recipesAdapter = RecipesAdapter(mutableListOf()) { recipe ->
            openRecipeDetail(recipe)
        }
        recipesRecyclerView.adapter = recipesAdapter
        recipesRecyclerView.layoutManager = LinearLayoutManager(context)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(context, "Please enter a search query.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toggleDiet() {
        val allergyButton = view?.findViewById<Button>(R.id.selectAllergies)
//        val selectCaloriesButton = view?.findViewById<Button>(R.id.selectCalories)
//        val selectNutritionButton = view?.findViewById<Button>(R.id.selectNutrition)
        val selectDietButton = view?.findViewById<Button>(R.id.selectDiet)
        val dietGridLayout = view?.findViewById<GridLayout>(R.id.dietGridLayout)

        // Toggle visibility based on the current state of one of the buttons
        if (allergyButton?.visibility == View.VISIBLE) {
            // Hide other buttons
            allergyButton.visibility = View.INVISIBLE
//            selectCaloriesButton?.visibility = View.INVISIBLE
//            selectNutritionButton?.visibility = View.INVISIBLE

            dietGridLayout?.visibility=View.VISIBLE




        } else {
            // Show other buttons
            allergyButton?.visibility = View.VISIBLE
//            selectCaloriesButton?.visibility = View.VISIBLE
//            selectNutritionButton?.visibility = View.VISIBLE
            dietGridLayout?.visibility=View.INVISIBLE

        }
    }



    fun toggleViewAllergies() {
        val selectDietButton = view?.findViewById<Button>(R.id.selectDiet)

//        val selectCaloriesButton = view?.findViewById<Button>(R.id.selectCalories)
//
//        val selectNutritionButton = view?.findViewById<Button>(R.id.selectNutrition)

        val recipesRecyclerView = view?.findViewById<RecyclerView>(R.id.recipesRecyclerView)
        val backButton = view?.findViewById<Button>(R.id.backButton)
        val allergyGridLayout = view?.findViewById<GridLayout>(R.id.allergyGridLayout)
        val allergyButton = view?.findViewById<Button>(R.id.selectAllergies)

        if (allergyGridLayout != null) {
            if (allergyGridLayout.visibility == View.INVISIBLE) {
                // Hide Allergy GridLayout, show RecyclerView and Back button
                allergyGridLayout.visibility = View.VISIBLE
                selectDietButton?.visibility = View.INVISIBLE
//                selectCaloriesButton?.visibility = View.INVISIBLE
//                selectNutritionButton?.visibility = View.INVISIBLE


            } else {
                // Show Allergy GridLayout, hide RecyclerView and Back button
                allergyGridLayout.visibility = View.INVISIBLE
                selectDietButton?.visibility = View.VISIBLE
//                selectCaloriesButton?.visibility = View.VISIBLE
//                selectNutritionButton?.visibility = View.VISIBLE

            }

        }
    }

    fun toggleViewBackButton() {



            // Create a new instance of RecipesFragment
            val newRecipesFragment = RecipesFragment()

            // Replace the current fragment with the new instance
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newRecipesFragment)
                .commit()

//        val selectDietButton = view?.findViewById<Button>(R.id.selectDiet)
////
////        val selectCaloriesButton = view?.findViewById<Button>(R.id.selectCalories)
////
////        val selectNutritionButton = view?.findViewById<Button>(R.id.selectNutrition)
//
//        val recipesRecyclerView = view?.findViewById<RecyclerView>(R.id.recipesRecyclerView)
//        val backButton = view?.findViewById<Button>(R.id.backButton)
//        val allergyGridLayout = view?.findViewById<GridLayout>(R.id.allergyGridLayout)
//        val allergyButton = view?.findViewById<Button>(R.id.selectAllergies)
//
//        // Toggle visibility of allergy grid layout and allergy button
//        allergyButton?.visibility = View.VISIBLE
//        selectDietButton?.visibility = View.VISIBLE
////        selectCaloriesButton?.visibility = View.VISIBLE
////        selectNutritionButton?.visibility = View.VISIBLE
//
//        // Toggle visibility of back button and recycler view
//        backButton?.visibility = View.INVISIBLE
//        recipesRecyclerView?.visibility = View.INVISIBLE
    }

    fun toggleViewSearch() {
//        val selectCaloriesButton = view?.findViewById<Button>(R.id.selectCalories)
//        val selectNutritionButton = view?.findViewById<Button>(R.id.selectNutrition)
        val selectDietButton = view?.findViewById<Button>(R.id.selectDiet)
        val dietGridLayout = view?.findViewById<GridLayout>(R.id.dietGridLayout)
        val recipesRecyclerView = view?.findViewById<RecyclerView>(R.id.recipesRecyclerView)
        val backButton = view?.findViewById<Button>(R.id.backButton)
        val allergyGridLayout = view?.findViewById<GridLayout>(R.id.allergyGridLayout)
        val allergyButton = view?.findViewById<Button>(R.id.selectAllergies)

        // Toggle visibility of allergy grid layout and allergy button
        allergyGridLayout?.visibility = View.INVISIBLE
        allergyButton?.visibility = View.INVISIBLE
        dietGridLayout?.visibility=View.INVISIBLE
        selectDietButton?.visibility=View.INVISIBLE
//        selectNutritionButton?.visibility=View.INVISIBLE
//        selectCaloriesButton?.visibility=View.INVISIBLE
        // Toggle visibility of back button and recycler view
        backButton?.visibility = View.VISIBLE
        recipesRecyclerView?.visibility = View.VISIBLE
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

    private fun putSerializable(s: String, recipe: Recipe) {

    }

    private fun getEdamamService(): EdamamService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(EdamamService::class.java)
    }



    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentFocusedView = activity?.currentFocus
        currentFocusedView?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun performSearch(query: String) {
        Log.d("RecipesFragment", "Performing search with query: $query")
        val allergyButton = view?.findViewById<Button>(R.id.selectAllergies)
        hideKeyboard()
        val service = getEdamamService()
        val call: Call<RecipeSearchResponse>

        when {
            selectedDiets.isNotEmpty() -> {
                val dietFilters = selectedDiets.joinToString(",")
                call = service.searchRecipesWithDietLabels(
                    appId = "951c3ce7",
                    appKey = "8ca648fddfd63916ee6f0d182e125d92",
                    query = query,
                    dietLabels = dietFilters
                )
                Log.d("API Call", "Diet Labels: $dietFilters")
            }
            selectedAllergies.isNotEmpty() -> {
                val allergyFilters = selectedAllergies.joinToString(",")
                call = service.searchRecipesWithHealthLabels(
                    appId = "951c3ce7",
                    appKey = "8ca648fddfd63916ee6f0d182e125d92",
                    query = query,
                    healthLabels = allergyFilters
                )
                Log.d("API Call", "Health Labels: $allergyFilters")
            }
            else -> {
                call = service.searchRecipesWithoutHealthLabels(
                    appId = "951c3ce7",
                    appKey = "8ca648fddfd63916ee6f0d182e125d92",
                    query = query
                )
            }
        }
        selectedDiets.clear()
        selectedAllergies.clear()
        centerRecyclerView()
        allergyButton?.visibility = View.INVISIBLE



        call.enqueue(object : Callback<RecipeSearchResponse> {
            override fun onResponse(
                call: Call<RecipeSearchResponse>,
                response: Response<RecipeSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.hits?.map { it.recipe } ?: emptyList()
                    displayRecipes(recipes) // Call displayRecipes with the list of recipes
//                    setAllergyInvisible()
                    toggleViewSearch()

                } else {
                    Toast.makeText(context, "Failed to retrieve recipes: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RecipeSearchResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun centerRecyclerView() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recipesRecyclerView)
        val layoutParams = recyclerView?.layoutParams as? LinearLayout.LayoutParams
        layoutParams?.gravity = Gravity.CENTER
        recyclerView?.layoutParams = layoutParams
    }



    // Example of displayRecipes method
    private fun displayRecipes(recipes: List<Recipe>) {
        recipesAdapter.updateRecipes(recipes)



    }


    fun setAllergyInvisible(){
        val checkBoxCeleryFree = view?.findViewById<CheckBox>(R.id.celery_free)
        val checkBoxCrustaceanFree = view?.findViewById<CheckBox>(R.id.crustacean_free)
        val checkBoxDairyFree = view?.findViewById<CheckBox>(R.id.dairy_free)
        val checkBoxEggFree = view?.findViewById<CheckBox>(R.id.egg_free)
        val checkBoxFishFree = view?.findViewById<CheckBox>(R.id.fish_free)
        val checkBoxGlutenFree = view?.findViewById<CheckBox>(R.id.gluten_free)
        val checkBoxLupineFree = view?.findViewById<CheckBox>(R.id.lupine_free)
        val checkBoxMustardFree = view?.findViewById<CheckBox>(R.id.mustard_free)
        val checkBoxPeanutFree = view?.findViewById<CheckBox>(R.id.peanut_free)
        val checkBoxSesameFree = view?.findViewById<CheckBox>(R.id.sesame_free)
        val checkBoxShellfishFree = view?.findViewById<CheckBox>(R.id.shellfish_free)
        val checkBoxSoyFree = view?.findViewById<CheckBox>(R.id.soy_free)
        val checkBoxTreeNutFree = view?.findViewById<CheckBox>(R.id.tree_nut_free)
        val checkBoxWheatFree = view?.findViewById<CheckBox>(R.id.wheat_free)
        if (checkBoxCeleryFree != null) {
            checkBoxCeleryFree.visibility = View.INVISIBLE
        }
        if (checkBoxCrustaceanFree != null) {
            checkBoxCrustaceanFree.visibility = View.INVISIBLE
        }
        if (checkBoxDairyFree != null) {
            checkBoxDairyFree.visibility = View.INVISIBLE
        }
        if (checkBoxEggFree != null) {
            checkBoxEggFree.visibility = View.INVISIBLE
        }
        if (checkBoxFishFree != null) {
            checkBoxFishFree.visibility = View.INVISIBLE
        }
        if (checkBoxGlutenFree != null) {
            checkBoxGlutenFree.visibility = View.INVISIBLE
        }
        if (checkBoxLupineFree != null) {
            checkBoxLupineFree.visibility = View.INVISIBLE
        }
        if (checkBoxMustardFree != null) {
            checkBoxMustardFree.visibility = View.INVISIBLE
        }
        if (checkBoxPeanutFree != null) {
            checkBoxPeanutFree.visibility = View.INVISIBLE
        }
        if (checkBoxSesameFree != null) {
            checkBoxSesameFree.visibility = View.INVISIBLE
        }
        if (checkBoxShellfishFree != null) {
            checkBoxShellfishFree.visibility = View.INVISIBLE
        }
        if (checkBoxSoyFree != null) {
            checkBoxSoyFree.visibility = View.INVISIBLE
        }
        if (checkBoxTreeNutFree != null) {
            checkBoxTreeNutFree.visibility = View.INVISIBLE
        }
        if (checkBoxWheatFree != null) {
            checkBoxWheatFree.visibility = View.INVISIBLE
        }
    }

        fun displaySearchResults(newRecipes: List<Recipe>) {
        recipesAdapter.updateRecipes(newRecipes)
    }
}