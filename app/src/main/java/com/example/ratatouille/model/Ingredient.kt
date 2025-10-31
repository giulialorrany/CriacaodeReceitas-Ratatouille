package com.example.ratatouille.model

import java.io.Serializable

data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String
) : Serializable