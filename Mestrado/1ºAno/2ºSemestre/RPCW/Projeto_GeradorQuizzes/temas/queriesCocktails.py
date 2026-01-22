from query_utils import fetch_query_results

ontologia_file = "ontologias/cocktails.ttl"

# Esta bebida é alcoólica?
sparql_query_cocktail_tf1 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?alchoholic WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :alcoholic ?alchoholic .
    }
"""
fetch_query_results(sparql_query_cocktail_tf1, ontologia_file, "query_results/CocktailsTF1.json",format="turtle")

# O Bartender ___ criou ____?
sparql_query_cocktail_tf2 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?bartender ?nome WHERE {
        ?s a :Bartender ;
            :strBartender ?bartender ;
            :createCocktail/:drinkName ?nome .
    }
"""
fetch_query_results(sparql_query_cocktail_tf2, ontologia_file, "query_results/CocktailsTF2.json",format="turtle")

# Este bar criou ____?
sparql_query_cocktail_tf3 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?bar WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :associatedWithBar/:strBar ?bar ;
    }
"""
fetch_query_results(sparql_query_cocktail_tf3, ontologia_file, "query_results/CocktailsTF3.json",format="turtle")


# Em que copo é preparada a bebida x?
sparql_query_cocktail_mc1 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?copo WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :serveInGlassware/:strGlass ?copo.

    }
"""
fetch_query_results(sparql_query_cocktail_mc1, ontologia_file, "query_results/CocktailsMC1.json",format="turtle")

# Que tipo de bebida é este?
sparql_query_cocktail_mc2 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?tipo WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :hasCategory/:strCategory ?tipo.

    }
"""
fetch_query_results(sparql_query_cocktail_mc2, ontologia_file, "query_results/CocktailsMC2.json",format="turtle")

# Onde foi criado esta bebida?
sparql_query_cocktail_mc3 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?local WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :createdInLocation/:strLocation ?local .

    }
"""
fetch_query_results(sparql_query_cocktail_mc3, ontologia_file, "query_results/CocktailsMC3.json",format="turtle")


# Corresponda a preparação de cada bebida:
sparql_query_cocktail_match1 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome ?preparacao WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :preparationEN ?preparacao .
    }
"""
fetch_query_results(sparql_query_cocktail_match1, ontologia_file, "query_results/CocktailsMatch1.json",format="turtle")

# Corresponda a origem de cada bebida:
sparql_query_cocktail_match2 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?local ?nome WHERE {
        ?s a :Location ;
            :strLocation ?local ;
            :originOfCocktail/:drinkName ?nome .
    }
"""
fetch_query_results(sparql_query_cocktail_match2, ontologia_file, "query_results/CocktailsMatch2.json",format="turtle")

# Corresponda o nome da bebida com a sua imagem:
sparql_query_cocktail_match3 = """
    PREFIX : <http://www.di.uminho.pt/prc2021/projeto#>
    SELECT DISTINCT ?nome (GROUP_CONCAT(?qtd; separator="; ") AS ?quantidades ) WHERE {
        ?s a :Cocktail ;
            :drinkName ?nome ;
            :needQuantity ?qtd .
    } GROUP BY ?nome
"""
fetch_query_results(sparql_query_cocktail_match3, ontologia_file, "query_results/CocktailsMatch3.json",format="turtle")





