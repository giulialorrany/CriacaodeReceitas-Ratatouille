package com.example.ratatouille

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ajuste o layout conforme necessário

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
    }
}