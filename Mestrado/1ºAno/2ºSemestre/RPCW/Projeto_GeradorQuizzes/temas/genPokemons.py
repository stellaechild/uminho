import random
import json
from itertools import permutations

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)

def genPokemonsTF1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsTF1.json")

    all_pokemons = []
    for item in lista:
        ataque = int(item["ataque"])
        nomes = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for nome in nomes:
            all_pokemons.append((nome, ataque))

    all_pairs = [
        (p1, p2)
        for p1, p2 in permutations(all_pokemons, 2)
        if p1[1] != p2[1]
    ]

    random.shuffle(all_pairs)
    selected_pairs = all_pairs[:question_count]

    questions = []
    for (poke1, atk1), (poke2, atk2) in selected_pairs:
        question = {
            "question": f"O pokémon '{poke1}' tem mais ataque que o pokémon '{poke2}'?",
            "options": ["Verdadeiro", "Falso"],
            "answer": "Verdadeiro" if atk1 > atk2 else "Falso",
            "correct_info": f"O ataque de '{poke1}' é {atk1}, e o de '{poke2}' é {atk2}.",
            "correct_info2": f"O ataque de '{poke1}' é {atk1}, e o de '{poke2}' é {atk2}.",
        }
        questions.append(question)

    print(f"Generated {len(questions)} PokémonsTF1 questions.")
    return questions

def genPokemonsTF2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsTF2.json")

    pokemon_to_class = {}
    all_classifications = set()

    for item in lista:
        classificacao = item["classificacao"]
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for poke in pokemons:
            pokemon_to_class[poke] = classificacao
        all_classifications.add(classificacao)

    questions = []
    pokemons = list(pokemon_to_class.keys())
    random.shuffle(pokemons)

    for poke in pokemons:
        if len(questions) >= question_count:
            break

        correct_class = pokemon_to_class[poke]
        if random.choice([True, False]):
            question = {
                "question": f"O pokémon '{poke}' é do tipo '{correct_class}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"'{poke}' é do tipo : {correct_class}."
            }
        else:
            wrong_classes = [c for c in all_classifications if c != correct_class]
            if not wrong_classes:
                continue
            wrong_class = random.choice(wrong_classes)
            question = {
                "question": f"O pokémon '{poke}' é um '{wrong_class}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"'{poke}' tem a classificação: {correct_class}.",
                "correct_info2": f"'{poke}' tem a classificação: {correct_class}."
            }

        questions.append(question)

    print(f"Generated {len(questions)} PokémonsTF2 questions.")
    return questions

def genPokemonsTF3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsTF3.json")

    pokemon_to_exp = {}
    all_exp = set()

    for item in lista:
        exp = item["exp"]
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for poke in pokemons:
            pokemon_to_exp[poke] = exp
        all_exp.add(exp)

    questions = []
    all_pokemons = list(pokemon_to_exp.keys())
    random.shuffle(all_pokemons)

    for poke in all_pokemons:
        if len(questions) >= question_count:
            break

        correct_exp = pokemon_to_exp[poke]

        if random.choice([True, False]):
            question = {
                "question": f"O pokémon '{poke}' precisa de {correct_exp} de experiência para evoluir?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O '{poke}' precisa de {correct_exp} de experiência para evoluir."
            }
        else:
            wrong_exp = random.choice([e for e in all_exp if e != correct_exp])
            question = {
                "question": f"O pokémon '{poke}' precisa de {wrong_exp} de experiência para evoluir?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O '{poke}' precisa de {correct_exp} de experiência para evoluir.",
                "correct_info2": f"O '{poke}' precisa de {correct_exp} de experiência para evoluir."
            }

        questions.append(question)

    print(f"Generated {len(questions)} PokémonsTF3 questions.")
    return questions

def genPokemonsMC1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMC1.json")
    num_query = len(lista)

    pokemon_hp_list = []
    for item in lista:
        hp = int(item["hp"])
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for poke in pokemons:
            pokemon_hp_list.append((poke, hp))

    used_pokemons = set()
    questions = []

    random.shuffle(pokemon_hp_list)

    for i in range(len(pokemon_hp_list)):
        if len(questions) >= question_count:
            break

        correct_poke, correct_hp = pokemon_hp_list[i]
        if correct_poke in used_pokemons:
            continue

        distractors = []
        for poke, hp in pokemon_hp_list:
            if poke != correct_poke and poke not in used_pokemons:
                distractors.append((poke, hp))
            if len(distractors) >= 3:
                break

        if len(distractors) < 3:
            continue

        options = [correct_poke] + [d[0] for d in distractors]
        options_hps = {correct_poke: correct_hp}
        for p, h in distractors:
            options_hps[p] = h

        random.shuffle(options)

        question = {
            "question": "Qual dos seguintes Pokémon tem mais vida (HP)?",
            "options": options,
            "answer": max(options, key=lambda p: options_hps[p]),
            "correct_info": ", ".join([f"{p} ({options_hps[p]})" for p in options])
        }

        used_pokemons.update(options)
        questions.append(question)

    print(f"Out of {num_query} query groups ---> generated {len(questions)} PokémonsMC1 questions.")
    return questions

def genPokemonsMC2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMC2.json")

    pokemon_lendario = {}
    for item in lista:
        is_legendary = item["lendario"].strip().lower() == "true"
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for poke in pokemons:
            pokemon_lendario[poke] = is_legendary

    all_pokemons = list(pokemon_lendario.keys())
    random.shuffle(all_pokemons)

    questions = []

    for _ in range(min(question_count, len(all_pokemons) // 4)):
        options = random.sample(all_pokemons, 4)
        correct_answer = next((poke for poke in options if pokemon_lendario[poke]), None)

        if not correct_answer:
            legendary_candidates = [p for p in all_pokemons if pokemon_lendario[p] and p not in options]
            if not legendary_candidates:
                continue
            correct_answer = random.choice(legendary_candidates)
            options[random.randint(0, 3)] = correct_answer

        question = {
            "question": "Qual dos seguintes Pokémon é lendário?",
            "options": options,
            "answer": correct_answer,
            "correct_info": f"{correct_answer} é um Pokémon lendário."
        }
        questions.append(question)

    print(f"Generated {len(questions)} PokémonsMC2 questions.")
    return questions

def genPokemonsMC3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMC3.json")

    pokemon_to_steps = {}
    all_steps = set()

    for item in lista:
        passos = item["passos"].strip()
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        for poke in pokemons:
            pokemon_to_steps[poke] = passos
        all_steps.add(passos)

    all_pokemons = list(pokemon_to_steps.keys())
    all_steps = list(all_steps)
    random.shuffle(all_pokemons)

    questions = []

    for pokemon in all_pokemons[:question_count]:
        correct_steps = pokemon_to_steps[pokemon]
        wrong_steps = [s for s in all_steps if s != correct_steps]
        if len(wrong_steps) < 3:
            continue
        options = [correct_steps] + random.sample(wrong_steps, 3)
        random.shuffle(options)

        question = {
            "question": f"Quantos passos são necessários para chocar o ovo de {pokemon}?",
            "options": options,
            "answer": correct_steps,
            "correct_info": f"{pokemon} precisa de {correct_steps} passos para chocar o ovo."
        }
        questions.append(question)

    print(f"Generated {len(questions)} PokémonsMC3 questions.")
    return questions

def genPokemonsMatch1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMatch1.json")
    num_query = len(lista)

    questions = []
    used_pairs = set()
    random.shuffle(lista)

    for _ in range(question_count):
        available_pairs = [pair for pair in lista if (pair['pokemon'], pair['habilidades']) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair['pokemon'], pair['habilidades'])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  

        for pair in selected:
            used_pairs.add((pair['pokemon'], pair['habilidades']))

        formatted_pairs = [(p['pokemon'], p['habilidades']) for p in selected]

        question = {
            "question": "Corresponda as habilidades de cada pokémon:",
            "pairs": formatted_pairs,
            "answer": formatted_pairs
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} PokemonsMatch1 questions.")
    return questions

def genPokemonsMatch2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMatch2.json")
    num_query = len(lista)

    questions = []
    used_pairs = set()
    random.shuffle(lista)

    for _ in range(question_count):
        available_pairs = [pair for pair in lista if (pair['pokemon'], pair['japones']) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair['pokemon'], pair['japones'])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  

        for pair in selected:
            used_pairs.add((pair['pokemon'], pair['japones']))

        formatted_pairs = [(p['pokemon'], p['japones']) for p in selected]

        question = {
            "question": "Faça a correspondência de cada pokémon ao seu nome em japonês:",
            "pairs": formatted_pairs,
            "answer": formatted_pairs
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} PokemonsMatch2 questions.")
    return questions

def genPokemonsMatch3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/pokemonsMatch3.json")
    num_query = len(lista)

    all_pairs = []
    for item in lista:
        geracao = item["geracao"]
        pokemons = [p.strip() for p in item["pokemons"].split(";") if p.strip()]
        all_pairs.extend([(pokemon, geracao) for pokemon in pokemons])

    questions = []
    used_pairs = set()
    random.shuffle(all_pairs)

    for _ in range(question_count):
        available_pairs = [p for p in all_pairs if p not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = all_pairs[:]

        selected_pairs = []
        seen_generations = set()

        for pair in available_pairs:
            pokemon, geracao = pair
            if geracao in seen_generations:
                continue
            selected_pairs.append(pair)
            seen_generations.add(geracao)
            if len(selected_pairs) == 4:
                break

        if len(selected_pairs) < 4:
            continue  

        for pair in selected_pairs:
            used_pairs.add(pair)

        question = {
            "question": "Corresponda a geração de cada pokémon:",
            "pairs": selected_pairs,
            "answer": selected_pairs
        }
        questions.append(question)

    print(f"Out of {num_query} grouped entries ---> generated {len(questions)} PokemonsMatch3 questions.")
    return questions