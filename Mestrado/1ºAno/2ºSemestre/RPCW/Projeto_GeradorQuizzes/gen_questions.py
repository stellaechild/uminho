import random
from temas.genHistoria import (
    genHistoriaTF1,
    genHistoriaTF2,
    genHistoriaTF3,
    genHistoriaMC1,
    genHistoriaMC2,
    genHistoriaMC3,
    genHistoriaMatch1,
    genHistoriaMatch2,
    genHistoriaMatch3,
)
from temas.genFilmes import (
    genFilmesTF1,
    genFilmesTF2,
    genFilmesTF3,
    genFilmesMC1,
    genFilmesMC2,
    genFilmesMC3,
    genFilmesMatch1,
    genFilmesMatch2,
    genFilmesMatch3,
)

from temas.genAnimes import (
    genAnimesTF1,
    genAnimesTF2,
    genAnimesTF3,
    genAnimesMC1,
    genAnimesMC2,
    genAnimesMC3,
    genAnimesMatch1,
    genAnimesMatch2,
    genAnimesMatch3,
)

from temas.genCocktails import (
    genCocktailsTF1,
    genCocktailsTF2,
    genCocktailsTF3,
    genCocktailsMC1,
    genCocktailsMC2,
    genCocktailsMC3,
    genCocktailsMatch1,
    genCocktailsMatch2,
    genCocktailsMatch3,
)

from temas.genPokemons import (
    genPokemonsTF1,
    genPokemonsTF2,
    genPokemonsTF3,
    genPokemonsMC1,
    genPokemonsMC2,
    genPokemonsMC3,
    genPokemonsMatch1,
    genPokemonsMatch2,
    genPokemonsMatch3,
)

from temas.genLivros import (
    genLivrosTF1,
    genLivrosTF2,
    genLivrosTF3,
    genLivrosMC1,
    genLivrosMC2,
    genLivrosMC3,
    genLivrosMatch1,
    genLivrosMatch2,
    genLivrosMatch3,
)

def distribute(total, parts):
    base = total // parts
    rest = total % parts
    return [base + 1 if i < rest else base for i in range(parts)]

def generate_questions(theme, question_count, modes):
    #endpoints = {"História de Portugal": "http://localhost:7200/repositories/HistoriaPT", "Filmes": "http://localhost:7200/repositories/Filmes"}

    mode_combinations = {
        ("tf",): (3, [1, 0, 0]),
        ("mc",): (3, [0, 1, 0]),
        ("match",): (3, [0, 0, 1]),
        ("tf", "mc"): (6, [1, 1, 0]),
        ("mc", "match"): (6, [0, 1, 1]),
        ("tf", "match"): (6, [1, 0, 1]),
        ("tf", "mc", "match"): (9, [1, 1, 1]),
    }

    active_modes = tuple(mode for mode in ["tf", "mc", "match"] if mode in modes)
    total_parts, distribution_order = mode_combinations[active_modes]

    counts = distribute(question_count, total_parts)
    tf_counts = [counts.pop(0) if distribution_order[0] else 0 for _ in range(3)]
    mc_counts = [counts.pop(0) if distribution_order[1] else 0 for _ in range(3)]
    match_counts = [counts.pop(0) if distribution_order[2] else 0 for _ in range(3)]

    print(f"Tf Counts: {tf_counts}")
    print(f"MC Counts: {mc_counts}")
    print(f"Match Counts: {match_counts}")

    if theme == "História de Portugal":
        #endpoint = endpoints["História de Portugal"]

        true_false_questions = (
            genHistoriaTF1(tf_counts[0]) +
            genHistoriaTF2(tf_counts[1]) +
            genHistoriaTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genHistoriaMC1(mc_counts[0]) +
            genHistoriaMC2(mc_counts[1]) +
            genHistoriaMC3(mc_counts[2])
        )
        matching_questions = (
            genHistoriaMatch1(match_counts[0]) +
            genHistoriaMatch2(match_counts[1]) +
            genHistoriaMatch3(match_counts[2])
        )
    elif theme == "Filmes":
        #endpoint = endpoints["Filmes"]

        true_false_questions = (
            genFilmesTF1(tf_counts[0]) +
            genFilmesTF2(tf_counts[1]) +
            genFilmesTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genFilmesMC1(mc_counts[0]) +
            genFilmesMC2(mc_counts[1]) +
            genFilmesMC3(mc_counts[2])
        )
        matching_questions = (
            genFilmesMatch1(match_counts[0]) +
            genFilmesMatch2(match_counts[1]) +
            genFilmesMatch3(match_counts[2])
        )
        
    elif theme == "Animes":

        true_false_questions = (
            genAnimesTF1(tf_counts[0]) +
            genAnimesTF2(tf_counts[1]) +
            genAnimesTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genAnimesMC1(mc_counts[0]) +
            genAnimesMC2(mc_counts[1]) +
            genAnimesMC3(mc_counts[2])
        )
        matching_questions = (
            genAnimesMatch1(match_counts[0]) +
            genAnimesMatch2(match_counts[1]) +
            genAnimesMatch3(match_counts[2])
        )
    
    elif theme == "Cocktails":

        true_false_questions = (
            genCocktailsTF1(tf_counts[0]) +
            genCocktailsTF2(tf_counts[1]) +
            genCocktailsTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genCocktailsMC1(mc_counts[0]) +
            genCocktailsMC2(mc_counts[1]) +
            genCocktailsMC3(mc_counts[2])
        )
        matching_questions = (
            genCocktailsMatch1(match_counts[0]) +
            genCocktailsMatch2(match_counts[1]) +
            genCocktailsMatch3(match_counts[2])
        )
    
    elif theme == "Pokemons":

        true_false_questions = (
            genPokemonsTF1(tf_counts[0]) +
            genPokemonsTF2(tf_counts[1]) +
            genPokemonsTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genPokemonsMC1(mc_counts[0]) +
            genPokemonsMC2(mc_counts[1]) +
            genPokemonsMC3(mc_counts[2])
        )
        matching_questions = (
            genPokemonsMatch1(match_counts[0]) +
            genPokemonsMatch2(match_counts[1]) +
            genPokemonsMatch3(match_counts[2])
        )

    elif theme == "Livros":

        true_false_questions = (
            genLivrosTF1(tf_counts[0]) +
            genLivrosTF2(tf_counts[1]) +
            genLivrosTF3(tf_counts[2])
        )
        multiple_choice_questions = (
            genLivrosMC1(mc_counts[0]) +
            genLivrosMC2(mc_counts[1]) +
            genLivrosMC3(mc_counts[2])
        )
        matching_questions = (
            genLivrosMatch1(match_counts[0]) +
            genLivrosMatch2(match_counts[1]) +
            genLivrosMatch3(match_counts[2])
        )
    
    random.shuffle(true_false_questions)
    random.shuffle(multiple_choice_questions)
    random.shuffle(matching_questions)
    
    total_questions = {
        "true_false": true_false_questions,
        "multiple_choice": multiple_choice_questions,
        "matching": matching_questions
    }

    return total_questions
