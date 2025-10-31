package com.example.ratatouille.model

import java.io.Serializable

data class RecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val likes: Int,
    // Adicione mais campos se quiser, mas esses são básicos
    val usedIngredients: List<Ingredient>,
    val missedIngredients: List<Ingredient>
) : Serializable