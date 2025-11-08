package com.example.ratatouille

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.ratatouille.Tradutor.traduzir
import com.example.ratatouille.api.ApiClient
import com.example.ratatouille.model.AppDatabase
import com.example.ratatouille.model.Favorite
import com.example.ratatouille.model.Ingredient
import com.example.ratatouille.model.RecipeResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerRecipes: RecyclerView
    private val recipes = mutableListOf<Recipe>()
    private lateinit var db: AppDatabase

    data class Recipe(
        val name: String,
        val id: Int = -1,
        val usedIngredients: List<Ingredient> = emptyList(),
        var missedIngredients: List<Ingredient> = emptyList(),
        var isFavorite: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // CORRIGIDO: usando androidx.appcompat.widget.Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        // Botando a navbar de baixo pra funcionar
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

        recyclerRecipes = findViewById(R.id.recycler_recipes)
        recyclerRecipes.layoutManager = LinearLayoutManager(this)

        // ---------- Verifica se está no DB dos favoritos e adiciona à lista de receitas ----------
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "favoritos.db").build()
        val recipeResponses = intent.getSerializableExtra("recipes") as? ArrayList<RecipeResponse>
        if (!recipeResponses.isNullOrEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                for (recipe in recipeResponses) {
                    val isCurrentFavorite = db.favoriteDAO().selectById(recipe.id) != null
                    val newRecipe = Recipe(
                        recipe.title,
                        recipe.id,
                        recipe.usedIngredients,
                        recipe.missedIngredients,
                        isCurrentFavorite
                    )
                    // Adiciona a receita na lista de receitas
                    recipes.add(newRecipe)
                }

                // Chama o adapter na thread principal
                withContext(Dispatchers.Main) {
                    // Inicia o adapter e botando no RecyclerView
                    recyclerRecipes.adapter?.notifyDataSetChanged()
                    recyclerRecipes.adapter = RecipeAdapter(recipes) { recipe ->
                        val intent = Intent(this@ResultsActivity, RecipeDetailActivity::class.java)
                        intent.putExtra("recipe_id", recipe.id)
                        intent.putExtra("recipe_name", recipe.name)
                        intent.putExtra("is_favorite", recipe.isFavorite)
                        startActivity(intent)
                    }
                }
            }
        } else {
            val ingredients = intent.getStringExtra("ingredients") ?: ""
            if (ingredients.isNotEmpty()) {
                recipes.add(Recipe("No online results for: $ingredients"))
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun toggleFavorite(view: View) {
        val imageView = view as ImageView
        val position = recyclerRecipes.getChildAdapterPosition(view.parent.parent as View)

        if (position != RecyclerView.NO_POSITION) {
            val recipe = recipes[position]
            val isFavoriteNow = !recipe.isFavorite
            recipe.isFavorite = isFavoriteNow

            // Atualiza o ícone do coração
            imageView.setImageResource(
                if (isFavoriteNow) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            )

            // Operação de banco de dados em uma thread de fundo
            val apiKey = "1aea402617064353a12a4bbe9e8e64f5"
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val instructions = ApiClient.service.getAnalyzedRecipeInstructions(recipe.id, apiKey)
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
                        // Cria um objeto Favorite
                        val favorite = Favorite(
                            id = recipe.id,
                            name = traduzir(recipe.name),
                            ingredients = traduzir(ingredientNames.joinToString(", ")),
                            steps = traduzir(stepStrings.joinToString("\n\n"))
                        )

                        // Verifica se é favorito ou não
                        if (isFavoriteNow) {
                            // Insere no banco de dados
                            db.favoriteDAO().insert(favorite)
                        } else {
                            // Remove do banco de dados
                            db.favoriteDAO().delete(favorite)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        print("Error loading recipe: ${e.message}")
                    }
                }
            }
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
            holder.tvRecipeName.text = traduzir(recipe.name)
            val allIngredients = recipe.usedIngredients + recipe.missedIngredients
            val allIngredientsStr = allIngredients.joinToString(", ") { it.name }
            holder.tvIngredientsList.text = traduzir(allIngredientsStr)
            holder.ivFavorite.setImageResource(R.drawable.ic_heart_outline)
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