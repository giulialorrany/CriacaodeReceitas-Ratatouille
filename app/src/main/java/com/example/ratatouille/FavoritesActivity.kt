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
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerFavorites: RecyclerView
    private val favoriteRecipes = mutableListOf<Recipe>()

    data class Recipe(val name: String, var isFavorite: Boolean = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // CORRIGIDO: toolbar só uma vez
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        recyclerFavorites = findViewById(R.id.recycler_favorites)
        recyclerFavorites.layoutManager = LinearLayoutManager(this)
        recyclerFavorites.adapter = RecipeAdapter(favoriteRecipes)

        favoriteRecipes.add(Recipe("Receita 1"))
        favoriteRecipes.add(Recipe("Receita 2"))
        recyclerFavorites.adapter?.notifyDataSetChanged()

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
    }

    // CORRIGIDO: função fora do onCreate
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun toggleFavorite(view: View) {
        val imageView = view as ImageView
        val position = recyclerFavorites.getChildAdapterPosition(view.parent.parent as View)
        if (position != RecyclerView.NO_POSITION) {
            val recipe = favoriteRecipes[position]
            recipe.isFavorite = !recipe.isFavorite
            imageView.setImageResource(if (recipe.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
        }
    }

    inner class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_example, parent, false)
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