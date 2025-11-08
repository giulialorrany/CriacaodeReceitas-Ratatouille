package com.example.ratatouille

object Tradutor {

    // Ingredientes comuns (inglês → português)
    private val dicionario = mapOf(
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
        "cherries" to "cerejas", "cherry" to "cereja",
        "avocados" to "abacates", "avocado" to "abacate",
        "olives" to "azeitonas", "olive" to "azeitona",

        // Vegetais
        "tomatoes" to "tomates", "tomato" to "tomate",
        "onions" to "cebolas", "onion" to "cebola",
        "shallots" to "chalotas", "shallot" to "chalota",
        "scallion" to "cebolinha",
        "garlic" to "alho", "cloves" to "dentes", "clove" to "dente",
        "carrots" to "cenouras", "carrot" to "cenoura",
        "sweet potatoes" to "batatas doce", "sweet potato" to "batata doce",
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
        "breasts" to "peitos", "breast" to "peito",
        "ribs" to "costelas", "rib" to "costela",
        "cutlets" to "costeletas", "cutlet" to "costeleta",

        // Temperos e ervas
        "salt" to "sal",
        "pepper" to "pimenta",
        "sugar" to "açúcar",
        "honey" to "mel",
        "olive oil" to "azeite", "oil" to "óleo",
        "vinegar" to "vinagre",
        "soy sauce" to "shoyu", "soy" to "soja",
        "basil" to "manjericão",
        "parsley" to "salsinha",
        "cilantro" to "coentro",
        "kale" to "couve",
        "cauliflower" to "couve-flor",
        "mint" to "hortelã",
        "cinnamon" to "canela",
        "vanilla" to "baunilha",
        "leaves" to "folhas","leaf" to "folha",
        "sage" to "sálvia",
        "herbs" to "ervas", "herb" to "erva",
        "spices" to "especiarias", "spice" to "especiaria",
        "seasoning" to "tempeiro",
        "sumac" to "sumagre",
        "arugula" to "rúcula",
        "oregano" to "orégano",
        "balsamic" to "balsâmico",
        "hot sauce" to "molho picante",

        // Outros
        "milk" to "leite",
        "water" to "água",
        "coffee" to "café",
        "tea" to "chá",
        "wine" to "vinho",
        "beer" to "cerveja",
        "salad" to "salada",
        "roll-ups" to "enrolados", "roll-up" to "enrolado", "rolls" to "enrolados", "roll" to "enrolado",
        "soup" to "sopa",
        "stew" to "ensopa",
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
        "baby" to "broto de",
        "skillet" to "frigineira",
        "canned" to "em lata",
        "juice" to "suco",
        "broth" to "caldo",
        "vegetable" to "vegetal",

        // Cores
        "red" to "vermelho",
        "green" to "verde",
        "blue" to "azul",
        "yellow" to "amarelo",
        "black" to "preto",
        "white" to "branco",

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
        "any" to "qualquer",
        "round" to "redondo",
        "you like" to "que você quiser",
        "swiss" to "suíço",
        "greek" to "grego",
        "w/" to "c/",
        "w" to "c",

        // Animais
        "cow" to "vaca",
        "goat" to "cabra",
        "oat" to "aveia", "oats" to "aveia",
        "pig" to "porco",

        // Unidades de medida
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

    // Traduz texto
    fun traduzir(texto: String, inverso: Boolean = false): String {
        var traduzido = texto

        dicionario.forEach { (en, pt) ->
            val palavraOriginal = if (inverso) pt else en
            val palavraTraduzida = if (inverso) en else pt

            /*
             * Para garantir que traduza somente correspondência exata
             * Ex: evitar "cowboy" -> "vacagaroto"
             */
            val regex = "\\s*${Regex.escape(palavraOriginal)}\\s*".toRegex(RegexOption.IGNORE_CASE)
            traduzido = regex.replace(traduzido) { matchResult ->
                matchResult.value.replace(palavraOriginal, palavraTraduzida, ignoreCase = true)
            }
        }
        return traduzido
    }
}
