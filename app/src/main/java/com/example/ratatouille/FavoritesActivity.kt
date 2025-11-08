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
import com.example.ratatouille.model.AppDatabase
import com.example.ratatouille.model.Favorite
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerFavorites: RecyclerView
    private val favoriteRecipes = mutableListOf<Recipe>()
    private lateinit var db: AppDatabase

    data class Recipe(
        val id: Int = -1,
        val name: String,
        val ingredients: String,
        var steps: String,
        var isFavorite: Boolean = true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Inicialização da Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Inicializando RecyclerView
        recyclerFavorites = findViewById(R.id.recycler_favorites)
        recyclerFavorites.layoutManager = LinearLayoutManager(this)
        recyclerFavorites.adapter = RecipeAdapter(favoriteRecipes)

        // Inicializando o banco de dados Room
        db = AppDatabase.getInstance(this)

        // Carregar as receitas favoritas do banco de dados
        loadFavoriteRecipes()

        // Configuração do BottomNavigation
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

    // Função para carregar as receitas favoritas do banco de dados
    private fun loadFavoriteRecipes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteFavoritesFromDb = db.favoriteDAO().selectAll() // Pega todos os favoritos
            val recipesList = favoriteFavoritesFromDb.map { favorite ->
                Recipe(
                    id = favorite.id,
                    name = favorite.name,
                    ingredients = favorite.ingredients,
                    steps = favorite.steps)
            }

            // Atualiza a lista de receitas favoritas
            favoriteRecipes.clear()
            favoriteRecipes.addAll(recipesList)

            // Notifica o adapter para atualizar a UI
            lifecycleScope.launch(Dispatchers.Main) {
                recyclerFavorites.adapter?.notifyDataSetChanged()
            }
        }
    }

    // Alternar o estado de favorito de uma receita
    fun toggleFavorite(view: View) {
        val imageView = view as ImageView
        val position = recyclerFavorites.getChildAdapterPosition(view.parent.parent as View)
        if (position != RecyclerView.NO_POSITION) {
            val recipe = favoriteRecipes[position]
            recipe.isFavorite = !recipe.isFavorite
            imageView.setImageResource(if (recipe.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)

            // Atualizando o banco de dados
            lifecycleScope.launch(Dispatchers.IO) {
                val favorite = Favorite(id = recipe.id, name = recipe.name, ingredients = recipe.ingredients, steps = recipe.steps)
                if (recipe.isFavorite) {
                    // Inserir no banco de dados
                    db.favoriteDAO().insert(favorite)
                } else {
                    // Remover do banco de dados
                    db.favoriteDAO().delete(favorite)
                }
            }
        }
    }

    // Adapter para a RecyclerView
    inner class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_example, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.tvRecipeName.text = recipe.name
            holder.tvIngredientsList.text = recipe.ingredients
            holder.tvStepsList.text = recipe.steps
            holder.ivFavorite.setImageResource(if (recipe.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
            holder.ivFavorite.setOnClickListener { toggleFavorite(it) }
        }

        override fun getItemCount(): Int = recipes.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvRecipeName: TextView = itemView.findViewById(R.id.tv_recipe_name)
            val tvIngredientsList: TextView = itemView.findViewById(R.id.tv_ingredients_list)
            val tvStepsList: TextView = itemView.findViewById(R.id.tv_steps_list)
            val ivFavorite: ImageView = itemView.findViewById(R.id.iv_favorite)
        }
    }

    // Configurar a ação de voltar
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
