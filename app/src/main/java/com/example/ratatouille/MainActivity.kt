package com.example.ratatouille

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ratatouille.Tradutor.traduzir
import com.example.ratatouille.api.ApiClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ajuste o layout conforme necessário

        val etIngredients: TextInputEditText = findViewById(R.id.edit_ingredients)
        val btnGenerate: Button = findViewById(R.id.btn_search)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_search
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> true
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                else -> {
                    // Navegação manual para outras atividades
                    when (item.itemId) {
                        R.id.nav_search -> {
                            startActivity(Intent(this, ResultsActivity::class.java))
                            true
                        }
                        R.id.nav_favorites -> {
                            startActivity(Intent(this, RecipeDetailActivity::class.java))
                            true
                        }
                    }
                    false
                }
            }
        }

        btnGenerate.setOnClickListener {
            val ingredients = traduzir(etIngredients.text.toString().trim()
                .replace("\\s*,\\s*".toRegex(), ","),
                true)
            // Ex: "maçã ,  farinha , batata doce, açúcar" -> "apple,flour,sweet potato,sugar"
            if (ingredients.isNotEmpty()) {
                generateRecipes(ingredients)
            } else {
                Toast.makeText(this, "Digite ingredientes!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun generateRecipes(ingredients: String) {
        val apiKey = "1aea402617064353a12a4bbe9e8e64f5"

        lifecycleScope.launch(Dispatchers.IO) {
            val call = ApiClient.service.findRecipesByIngredients(ingredients, 10, apiKey)
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val recipes = response.body()

                    if (!recipes.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@MainActivity, ResultsActivity::class.java)
                            intent.putExtra("recipes", ArrayList(recipes))
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.e("Error", "Falha: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exceção: ${e.message}")
            }
        }
    }
}