package com.example.ratatouille

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.example.ratatouille.api.ApiClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ajuste o layout conforme necessário

        val etIngredients: TextInputEditText = findViewById(R.id.edit_ingredients)
        val btnGenerate: Button = findViewById(R.id.btn_search)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
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
            val ingredients = etIngredients.text.toString().trim() // Ex: "maçã,farinha,açúcar"
            if (ingredients.isNotEmpty()) {
                generateRecipes(ingredients)
            } else {
                // Mostre um Toast: "Digite ingredientes!"
            }
        }
    }

    private fun generateRecipes(ingredients: String) {
        val apiKey = "1aea402617064353a12a4bbe9e8e64f5" // Coloque sua chave aqui (para testes)

        GlobalScope.launch(Dispatchers.IO) { // Chamada em background
            val call = ApiClient.service.findRecipesByIngredients(ingredients, 10, apiKey)
            try {
                val response = call.execute() // Executa a chamada
                if (response.isSuccessful) {
                    val recipes = response.body() // Lista de RecipeResponse
                    launch(Dispatchers.Main) { // Volta pra UI
                        // Aqui, atualize a UI: exiba as receitas em uma RecyclerView
                        Log.d("Recipes", recipes.toString()) // Para testes, veja no Logcat
                        // Exemplo: recipes[0].title -> "Apple Pie"
                        // Mostre em TextView ou lista
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