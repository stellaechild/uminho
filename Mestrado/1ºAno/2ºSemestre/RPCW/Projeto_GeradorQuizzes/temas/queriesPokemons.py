from query_utils import fetch_query_results

ontologia_file = "ontologias/pokemon.ttl"

# Pokemon
# O pokémon pokemon_x tem mais ataque que o pokemon_y ?
sparql_query_pokemon_tf1 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT DISTINCT ?ataque (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temAtaqueNormal/:temAtaque ?ataque .         
    }group by ?ataque
"""
fetch_query_results(sparql_query_pokemon_tf1, ontologia_file, "query_results/pokemonsTF1.json",format="turtle")

# O pokémon pokemon_x é do tipo classificação_x ?
sparql_query_pokemon_tf2 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT DISTINCT ?classificacao (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temClassificação/:temClassificação ?classificacao.
    }group by ?classificacao
"""
fetch_query_results(sparql_query_pokemon_tf2, ontologia_file, "query_results/pokemonsTF2.json",format="turtle")

# O pokémon pokemon_x precisa de qtd_experiência para evoluir?
sparql_query_pokemon_tf3 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT DISTINCT ?exp (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temExperiênciaParaCrescimento/:experiênciaCrescimento ?exp.     
    }group by ?exp
"""
fetch_query_results(sparql_query_pokemon_tf3, ontologia_file, "query_results/pokemonsTF3.json",format="turtle")

# Qual dos pokémons tem mais vida (HP) ?
sparql_query_pokemon_mc1 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT DISTINCT ?hp (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temHP/:temHP ?hp.
    }group by ?hp
"""
fetch_query_results(sparql_query_pokemon_mc1, ontologia_file, "query_results/pokemonsMC1.json",format="turtle")

# Qual dos pokémons é lendário?
sparql_query_pokemon_mc2 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT DISTINCT ?lendario (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :éLendário ?lendario.
    } group by ?lendario
"""
fetch_query_results(sparql_query_pokemon_mc2, ontologia_file, "query_results/pokemonsMC2.json",format="turtle")

# Quantos passos é necessário para chocar o ovo de pokemon_x ?
sparql_query_pokemon_mc3 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT ?passos (GROUP_CONCAT(?pokemon; separator="; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temPassosDeOvo/:temPassosChocarOvo ?passos . 
    }GROUP BY ?passos
"""

fetch_query_results(sparql_query_pokemon_mc3, ontologia_file, "query_results/pokemonsMC3.json",format="turtle")

# Corresponda as habilidades de cada pokémon: 
sparql_query_pokemon_match1 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT ?pokemon (GROUP_CONCAT(?ab; separator="; ") AS ?habilidades) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temHabilidade ?habilidade . 
        BIND(STRAFTER(STR(?habilidade), "#") AS ?ab)
    }GROUP BY ?pokemon
"""

fetch_query_results(sparql_query_pokemon_match1, ontologia_file, "query_results/pokemonsMatch1.json",format="turtle")

# Faça a correspondência cada pokémon e o seu nome em japonês:
sparql_query_pokemon_match2 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT ?pokemon ?japones WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temNomeJaponês ?japones . 
    } 
"""
fetch_query_results(sparql_query_pokemon_match2, ontologia_file, "query_results/pokemonsMatch2.json",format="turtle")

# Corresponda a geração de cada pokémon:
sparql_query_pokemon_match3 = """
    PREFIX : <http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#>
    SELECT ?geracao (GROUP_CONCAT(?pokemon; separator= "; ") AS ?pokemons) WHERE {
        ?s a :Pokémon ;
        :temNome ?pokemon;
        :temGeração/:númeroGeração ?geracao . 
    }group by ?geracao
"""
fetch_query_results(sparql_query_pokemon_match3, ontologia_file, "query_results/pokemonsMatch3.json",format="turtle")


