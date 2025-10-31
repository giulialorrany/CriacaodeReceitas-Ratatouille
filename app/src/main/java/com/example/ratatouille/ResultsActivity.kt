package com.example.ratatouille

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import com.example.ratatouille.model.Ingredient
import com.example.ratatouille.model.RecipeResponse

class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerRecipes: RecyclerView
    private val recipes = mutableListOf<Recipe>()

    data class Recipe(
        val name: String,
        val id: Int = -1,
        val usedIngredients: List<Ingredient> = emptyList(),
        var missedIngredients: List<Ingredient> = emptyList(),
        var isFavorite: Boolean = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        recyclerRecipes = findViewById(R.id.recycler_recipes)
        recyclerRecipes.layoutManager = LinearLayoutManager(this)

        // Pegar as receitas do intent
        val recipeResponses = intent.getSerializableExtra("recipes") as? ArrayList<RecipeResponse>

        if (!recipeResponses.isNullOrEmpty()) {
            recipes.addAll(recipeResponses.map {Recipe(
                it.title,
                it.id,
                it.usedIngredients,
                it.missedIngredients
            )})
        } else {
            val ingredients = intent.getStringExtra("ingredients") ?: ""
            if (ingredients.isNotEmpty()) {
                recipes.add(Recipe("No online results for: $ingredients"))
            }
        }

        recyclerRecipes.adapter = RecipeAdapter(recipes) { recipe ->
            // ação ao clicar em uma receita
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipeName", recipe.name)
            startActivity(intent)
        }


        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_search // Reutilizando nav_search para Results
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
                            startActivity(Intent(this, MainActivity::class.java))
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
    }

    fun toggleFavorite(view: View) {
        val imageView = view as ImageView
        val position = recyclerRecipes.getChildAdapterPosition(view.parent.parent as View)
        if (position != RecyclerView.NO_POSITION) {
            val recipe = recipes[position]
            recipe.isFavorite = !recipe.isFavorite
            imageView.setImageResource(if (recipe.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
        }
    }

    inner class RecipeAdapter(
        private val recipes: List<Recipe>,
        private val onClick: (Recipe) -> Unit
    ) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.results_example, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]

            // Título traduzido
            holder.tvRecipeName.text = Tradutor.traduzirTitulo(recipe.name)

            // Ingredientes (ajuste conforme os campos do seu modelo Recipe)
            val allIngredients = recipe.usedIngredients + recipe.missedIngredients
            holder.tvIngredientsList.text = allIngredients.joinToString(", ") { it.name }

            // Passos ou instruções (se existirem)
            // holder.tvStepsList.text = recipe.steps ?: "Passos não disponíveis"

            // Ícone de favorito (placeholder por enquanto)
            holder.ivFavorite.setImageResource(R.drawable.ic_heart_outline)

            // Clique no item
            holder.itemView.setOnClickListener {
                getRecipeInformation(recipe.id, recipe.name)
            }
        }

        override fun getItemCount(): Int = recipes.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvRecipeName: TextView = itemView.findViewById(R.id.tv_recipe_name)
            val tvIngredientsList: TextView = itemView.findViewById(R.id.tv_ingredients_list)
            val ivFavorite: ImageView = itemView.findViewById(R.id.iv_favorite)
        }
    }
    private fun getRecipeInformation(recipeId: Int, recipeName: String) {
        val intent = Intent(this@ResultsActivity, RecipeActivity::class.java)
        intent.putExtra("recipe_id", recipeId)
        intent.putExtra("recipe_name", recipeName)
        startActivity(intent)
    }
}