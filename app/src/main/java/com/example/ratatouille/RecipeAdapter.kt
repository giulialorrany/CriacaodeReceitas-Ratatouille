package com.example.ratatouille

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class RecipeDetailActivity : AppCompatActivity() {

    private var isFavorite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_recipe)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_favorites // Reutilizando nav_favorites para RecipeDetail
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_favorites -> true
                else -> {
                    // Navegação manual para outras atividades
                    when (item.itemId) {
                        R.id.nav_search -> {
                            startActivity(Intent(this, ResultsActivity::class.java))
                            true
                        }
                        R.id.nav_favorites -> {
                            startActivity(Intent(this, FavoritesActivity::class.java))
                            true
                        }
                    }
                    false
                }
            }
        }

        val recipeName = intent.getStringExtra("recipe_name") ?: "Ratatouille"
        findViewById<TextView>(R.id.tv_recipe_name)?.text = recipeName
        findViewById<ImageView>(R.id.iv_favorite)?.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
    }

    fun toggleFavorite(view: View) {
        val imageView = view as ImageView
        isFavorite = !isFavorite
        imageView.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
    }
}