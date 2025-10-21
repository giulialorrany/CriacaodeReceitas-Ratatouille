package com.example.ratatouille

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.util.Log
import android.widget.*
import com.example.ratatouille.api.ApiClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins

class MainActivity : AppCompatActivity() {

    private lateinit var containerIngredientes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                else -> false
            }
        }

        containerIngredientes = findViewById(R.id.containerIngredientes)

        // Adiciona a primeira linha inicial de ingrediente + quantidade
        adicionarNovaLinha()

        val btnGenerate: Button = findViewById(R.id.btn_search)
        btnGenerate.setOnClickListener {
            val ingredientes = obterListaIngredientes()
            if (ingredientes.isNotEmpty()) {
                generateRecipes(ingredientes.joinToString(",") { it.first }) // envia apenas nomes
            } else {
                Toast.makeText(this, "Digite ao menos um ingrediente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para criar nova linha de ingrediente + quantidade + botão remover
    private fun adicionarNovaLinha() {
        val linha = LinearLayout(this)
        linha.orientation = LinearLayout.HORIZONTAL
        linha.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 8, 0, 0) }
        linha.gravity = Gravity.CENTER_VERTICAL

        val inputIngrediente = EditText(this).apply {
            hint = "Ingrediente"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }

        val inputQuantidade = EditText(this).apply {
            hint = "Qtd"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(8, 0, 0, 0)
            }
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        val btnRemover = Button(this).apply {
            text = "🗑"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(8, 0, 0, 0) }

            setOnClickListener {
                containerIngredientes.removeView(linha)
            }
        }

        linha.addView(inputIngrediente)
        linha.addView(inputQuantidade)
        linha.addView(btnRemover)

        containerIngredientes.addView(linha)

        // Adiciona comportamento: se digitar no último ingrediente, cria nova linha
        inputIngrediente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && linha == containerIngredientes.getChildAt(containerIngredientes.childCount - 1)) {
                    adicionarNovaLinha()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Retorna a lista de ingredientes preenchidos
    private fun obterListaIngredientes(): List<Pair<String, Int>> {
        val lista = mutableListOf<Pair<String, Int>>()
        for (i in 0 until containerIngredientes.childCount) {
            val linha = containerIngredientes.getChildAt(i) as LinearLayout
            val nome = (linha.getChildAt(0) as EditText).text.toString().trim()
            val qtdText = (linha.getChildAt(1) as EditText).text.toString().trim()
            val qtd = qtdText.toIntOrNull() ?: 0
            if (nome.isNotEmpty() && qtd > 0) {
                lista.add(Pair(nome, qtd))
            }
        }
        return lista
    }

    private fun generateRecipes(ingredients: String) {
        val apiKey = "1aea402617064353a12a4bbe9e8e64f5"

        GlobalScope.launch(Dispatchers.IO) {
            val call = ApiClient.service.findRecipesByIngredients(ingredients, 10, apiKey)
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val recipes = response.body()
                    launch(Dispatchers.Main) {
                        Log.d("Recipes", recipes.toString())
                        // Atualize UI, RecyclerView etc.
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
