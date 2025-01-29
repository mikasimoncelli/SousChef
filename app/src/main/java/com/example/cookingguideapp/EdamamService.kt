package com.example.cookingguideapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamService {
    @GET("search")
    fun searchRecipesWithHealthLabels(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("health") healthLabels: String
    ): Call<RecipeSearchResponse>


    @GET("search")
    fun searchRecipesWithDietLabels(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("diet") dietLabels: String
    ): Call<RecipeSearchResponse>

    @GET("search")
    fun searchRecipesWithoutHealthLabels(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String
    ): Call<RecipeSearchResponse>






        @GET("search")
        fun searchRecipes(
            @Query("app_id") appId: String,
            @Query("app_key") appKey: String,
            @Query("q") query: String
        ): Call<RecipeSearchResponse>
    }

