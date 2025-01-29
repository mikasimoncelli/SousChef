package com.example.cookingguideapp

data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    var isSelected: Boolean = false,
    var documentId: String = ""

)