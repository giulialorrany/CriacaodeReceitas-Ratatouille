package com.example.ratatouille

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerRecipes: RecyclerView
    private val recipes = mutableListOf<Recipe>()

    data class Recipe(val name: String, var isFavorite: Boolean = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        recyclerRecipes = findViewById(R.id.recycler_recipes)
        recyclerRecipes.layoutManager = LinearLayoutManager(this)
        recyclerRecipes.adapter = RecipeAdapter(recipes)

        val ingredients = intent.getStringExtra("ingredients") ?: ""
        if (ingredients.isNotEmpty()) {
            recipes.add(Recipe("Receita 1 baseada em $ingredients"))
            recipes.add(Recipe("Receita 2 baseada em $ingredients"))
        }
        recyclerRecipes.adapter?.notifyDataSetChanged()

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

    inner class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.results_example, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.tvRecipeName.text = recipe.name
            holder.ivFavorite.setImageResource(if (recipe.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
            holder.ivFavorite.setOnClickListener { toggleFavorite(it) }
        }

        override fun getItemCount(): Int = recipes.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvRecipeName: TextView = itemView.findViewById(R.id.tv_recipe_name)
            val ivFavorite: ImageView = itemView.findViewById(R.id.iv_favorite)
        }
    }
}