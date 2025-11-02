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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.ratatouille.model.AppDatabase
import com.example.ratatouille.model.Ingredient
import com.example.ratatouille.model.RecipeResponse
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        recyclerRecipes = findViewById(R.id.recycler_recipes)
        recyclerRecipes.layoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "favoritos.db").build()

        val recipeResponses = intent.getSerializableExtra("recipes") as? ArrayList<RecipeResponse>

        if (!recipeResponses.isNullOrEmpty()) {
            recipes.addAll(recipeResponses.map {
                Recipe(it.title, it.id, it.usedIngredients, it.missedIngredients)
            })
        } else {
            val ingredients = intent.getStringExtra("ingredients") ?: ""
            if (ingredients.isNotEmpty()) {
                recipes.add(Recipe("No online results for: $ingredients"))
            }
        }

        recyclerRecipes.adapter = RecipeAdapter(recipes) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipeName", recipe.name)
            startActivity(intent)
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_search
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> true
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                else -> false
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
            holder.tvRecipeName.text = recipe.name
            val allIngredients = recipe.usedIngredients + recipe.missedIngredients
            holder.tvIngredientsList.text = allIngredients.joinToString(", ") { it.name }
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