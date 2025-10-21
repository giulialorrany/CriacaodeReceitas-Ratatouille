package com.example.ratatouille.api

import com.example.ratatouille.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call // Ou use suspend para coroutines

interface SpoonacularService {
    @GET("recipes/findByIngredients")
    fun findRecipesByIngredients(
        @Query("ingredients") ingredients: String, // Ex: "apples,flour,sugar"
        @Query("number") number: Int = 10, // Quantas receitas retornar
        @Query("apiKey") apiKey: String
    ): Call<List<RecipeResponse>> // Retorna uma lista de receitas
}