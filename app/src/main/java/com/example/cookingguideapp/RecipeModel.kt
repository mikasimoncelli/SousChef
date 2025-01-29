package com.example.cookingguideapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecipeSearchResponse(
    @SerializedName("from") val from: Int,
    @SerializedName("to") val to: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("hits") val hits: List<Hit>
)

data class Hit(
    @SerializedName("recipe") val recipe: Recipe,
    @SerializedName("_links") val links: Links
)

data class Links(
    @SerializedName("self") val self: SelfLink


)

data class SelfLink(
    @SerializedName("href") val href: String
)

data class Recipe(
    @SerializedName("uri") val uri: String = "",
    @SerializedName("label") val label: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("source") val source: String = "",
    @SerializedName("url") val url: String = "",
    @SerializedName("yield") val yield: Int = 0,
    @SerializedName("ingredientLines") val ingredientLines: List<String> = listOf(),
    @SerializedName("method") val method: String? = null,
    @SerializedName("_links") val links: Links? = null
) : Serializable
