package com.example.ratatouille

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar  // ← ADICIONADO
import androidx.lifecycle.lifecycleScope
import com.example.ratatouille.Tradutor.traduzir
import com.example.ratatouille.api.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeActivity : AppCompatActivity() {

    private lateinit var tvRecipeName: TextView
    private lateinit var tvIngredientsList: TextView
    private lateinit var tvStepsList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_recipe)

        // CORRIGIDO: Toolbar configurada
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        tvRecipeName = findViewById(R.id.tv_recipe_name)
        tvIngredientsList = findViewById(R.id.tv_ingredients_list)
        tvStepsList = findViewById(R.id.tv_steps_list)

        // nav bar debaixo
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_favorites
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_favorites -> true
                else -> false
            }
        }

        // receber Id e Nome da receita
        val recipeId = intent.getIntExtra("recipe_id", 0)
        val recipeName = intent.getStringExtra("recipe_name") ?: "Recipe"
        tvRecipeName.text = traduzir(recipeName)

        if (recipeId != 0) {
            loadRecipeInstructions(recipeId)
        } else {
            tvStepsList.text = "No recipe ID found."
        }
    }

    // CORRIGIDO: função FORA do onCreate
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        return true
    }

    fun loadRecipeInstructions(recipeId: Int) {
        val apiKey = "1aea402617064353a12a4bbe9e8e64f5"
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val instructions = ApiClient.service.getAnalyzedRecipeInstructions(recipeId, apiKey)
                val ingredientNames = instructions
                    .flatMap { it.steps }
                    .flatMap { it.ingredients ?: emptyList() }
                    .mapNotNull { it.name }
                    .distinct()
                val stepStrings = instructions
                    .flatMap { it.steps }
                    .sortedBy { it.number }
                    .map { "${it.number}. ${it.step}" }

                withContext(Dispatchers.Main) {
                    tvIngredientsList.text = if (ingredientNames.isNotEmpty())
                        traduzir(ingredientNames.joinToString("\n") { "- $it" })
                    else "No ingredients found."
                    tvStepsList.text = if (stepStrings.isNotEmpty())
                        traduzir(stepStrings.joinToString("\n\n"))
                    else "No steps found."
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvStepsList.text = "Error loading recipe: ${e.message}"
                }
            }
        }
    }
}