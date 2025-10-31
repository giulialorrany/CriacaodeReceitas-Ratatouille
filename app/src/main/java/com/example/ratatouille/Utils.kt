package com.example.ratatouille

object Tradutor {

    // Ingredientes comuns (inglês → português)
    private val ingredientes = mapOf(
        // Frutas
        "apple" to "maçã", "apples" to "maçãs",
        "banana" to "banana", "bananas" to "bananas",
        "orange" to "laranja", "oranges" to "laranjas",
        "lemon" to "limão", "lemons" to "limões",
        "lime" to "lima", "limes" to "limas",
        "strawberry" to "morango", "strawberries" to "morangos",
        "blueberry" to "mirtilo", "blueberries" to "mirtilos",
        "grape" to "uva", "grapes" to "uvas",
        "watermelon" to "melancia", "watermelons" to "melancias",
        "pineapple" to "abacaxi", "pineapples" to "abacaxis",
        "mango" to "manga", "mangos" to "mangas",

        // Vegetais
        "tomatoes" to "tomates", "tomato" to "tomate",
        "onions" to "cebolas", "onion" to "cebola",
        "garlic" to "alho", "cloves" to "dentes", "clove" to "dente",
        "carrots" to "cenouras", "carrot" to "cenoura",
        "potatoes" to "batatas", "potato" to "batata",
        "lettuce" to "alface",
        "cucumbers" to "pepinos", "cucumber" to "pepino",
        "bell pepper" to "pimentão", "bell peppers" to "pimentões",
        "broccoli" to "brócolis",
        "spinach" to "espinafre",
        "zucchini" to "abobrinha", "zucchinis" to "abobrinhas",
        "eggplant" to "berinjela", "eggplants" to "berinjelas",

        // Cereais e grãos
        "rice" to "arroz",
        "pasta" to "macarrão",
        "bread" to "pão",
        "flour" to "farinha",
        "oat" to "aveia", "oats" to "aveia",
        "corn" to "milho",

        // Proteínas
        "chicken" to "frango",
        "beef" to "carne bovina", "ground beef" to "carne moída",
        "filet" to "filé",
        "steak" to "bife",
        "pork" to "carne suína",
        "fish" to "peixe",
        "salmon" to "salmão",
        "egg" to "ovo", "eggs" to "ovos",
        "cheese" to "queijo",
        "yogurt" to "iogurte",
        "butter" to "manteiga",
        "cream" to "creme de leite", "heavy cream" to "creme de leite fresco",
        "anchovy" to "anchova",
        "shrimps" to "camarões", "shrimp" to "camarão",

        // Temperos e ervas
        "salt" to "sal",
        "pepper" to "pimenta",
        "sugar" to "açúcar",
        "honey" to "mel",
        "oil" to "óleo", "olive oil" to "azeite",
        "vinegar" to "vinagre",
        "soy sauce" to "shoyu",
        "basil" to "manjericão",
        "parsley" to "salsinha",
        "cilantro" to "coentro",
        "mint" to "hortelã",
        "cinnamon" to "canela",
        "vanilla" to "baunilha",
        "leaves" to "folhas","leaf" to "folha",
        "sage" to "sálvia",

        // Outros
        "milk" to "leite",
        "water" to "água",
        "chocolate" to "chocolate",
        "coffee" to "café",
        "tea" to "chá",
        "wine" to "vinho",
        "beer" to "cerveja",
        "salad" to "salada",
        "roll-ups" to "enrolados", "roll-up" to "enrolado", "rolls" to "enrolados", "roll" to "enrolado",
        "soup" to "sopa",
        "roasted" to "assado",
        "french fries" to "batatas fritas",
        "fried" to "frito",
        "wedges" to "fatias",
        "pan" to "panela",
        "oven" to "forno",
        "mashed" to "purê",
        "gnocchi" to "nhoque",
        "gluten free" to "sem glúten",
        "vegan" to "vegano",
        "parmesan" to "parmesão",
        "remove skin" to "descascado", "peeled" to "descascado",
        "peel" to "descascar",
        "chili" to "pimenta",
        "powder" to "pó",
        "pie" to "torta",

        // Linguagem
        "of" to "de",
        "with" to "com",
        "and" to "e", "&" to "e",
        "slow" to "devagar",
        "quick" to "rápido",
        "simple" to "simples",
        "easy" to "fácil",
        "savory" to "saboroso",
        "homemade" to "caseiro",
        "old-fashioned" to "antiquado",

        // Animais
        "cow" to "vaca",
        "goat" to "cabra",
        "pig" to "porco",

        // Cores
        "red" to "vermelho",
        "green" to "verde",
        "blue" to "azul",
        "yellow" to "amarelo",
        "black" to "preto",
        "white" to "branco"
    )

    // Unidades
    private val unidades = mapOf(
        "cup" to "xícara", "cups" to "xícaras",
        "tablespoon" to "colher de sopa", "tablespoons" to "colheres de sopa",
        "teaspoon" to "colher de chá", "teaspoons" to "colheres de chá",
        "ounce" to "onça", "ounces" to "onças",
        "pound" to "libra", "pounds" to "libras",
        "gram" to "grama", "grams" to "gramas",
        "kilogram" to "quilograma", "kilograms" to "quilogramas",
        "liter" to "litro", "liters" to "litros",
        "milliliter" to "mililitro", "milliliters" to "mililitros",
        "piece" to "unidade", "pieces" to "unidades"
    )

    // Traduz um ingrediente
    fun traduzirIngrediente(ing: String): String {
        val lower = ing.lowercase()
        return ingredientes[lower] ?: ing
    }

    // Traduz uma unidade
    fun traduzirUnidade(unit: String): String {
        val lower = unit.lowercase()
        return unidades[lower] ?: unit
    }

    // Traduz título da receita (ex: "Apple Pie" → "Torta de Maçã")
    fun traduzirTitulo(titulo: String): String {
        var traduzido = titulo
        ingredientes.forEach { (en, pt) ->
            traduzido = traduzido.replace(en, pt, ignoreCase = true)
        }
        return traduzido
    }
}
