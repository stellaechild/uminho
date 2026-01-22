import random
import json

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)
    
def genCocktailsTF1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsTF1.json")
    random.shuffle(lista)

    questions = []
    used_names = set()

    for item in lista:
        if len(questions) >= question_count:
            break

        nome = item.get("nome")
        # optional equivale a não alcóolico
        is_alcoholic = not item.get("alchoholic", "").strip().lower() == "non alcoholic"

        if not nome or nome in used_names:
            continue
        used_names.add(nome)

        if random.choice([True, False]):
            answer = "Verdadeiro" if is_alcoholic else "Falso"
            question = {
            "question": f"A bebida '{nome}' é alcoólica?",
            "options": ["Verdadeiro", "Falso"],
            "answer": answer,
        }
        else:
            answer = "Falso" if is_alcoholic else "Verdadeiro"

            question = {
                "question": f"A bebida '{nome}' é alcoólica?",
                "options": ["Verdadeiro", "Falso"],
                "answer": answer,
            }

        questions.append(question)

    print(f"Generated {len(questions)} CocktailsTF1 questions.")
    return questions

def genCocktailsTF2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsTF2.json")
    if not lista:
        return []

    questions = []
    used_pairs = set()

    for _ in range(question_count):
        correct_pair = random.choice(lista)
        correct_bartender = correct_pair["bartender"]
        cocktail = correct_pair["nome"]
        pair_key = (correct_bartender, cocktail)

        if pair_key in used_pairs:
            continue
        used_pairs.add(pair_key)

        if random.choice([True, False]):
            bartender = correct_bartender
            answer = "Verdadeiro"
            question = {
                "question": f"O bartender '{bartender}' criou '{cocktail}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": answer,
                "correct_info": f"A bebida '{cocktail}' foi criada por {correct_bartender}.",
            }

        else:
            wrong_bartenders = [item["bartender"] for item in lista if item["bartender"] != correct_bartender]
            if not wrong_bartenders:
                continue  
            bartender = random.choice(wrong_bartenders)
            answer = "Falso"

            question = {
                "question": f"O bartender '{bartender}' criou '{cocktail}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": answer,
                "correct_info": f"A bebida '{cocktail}' foi criada por {correct_bartender}.",
                "correct_info2": f"A bebida '{cocktail}' foi criada por {correct_bartender}.",
            }
        questions.append(question)

    print(f"Generated {len(questions)} CocktailsTF2 questions.")
    return questions

def genCocktailsTF3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsTF3.json")
    if not lista:
        return []

    questions = []
    used_pairs = set()

    for _ in range(question_count):
        correct_pair = random.choice(lista)
        cocktail = correct_pair["nome"]
        correct_bar = correct_pair["bar"]
        pair_key = (cocktail, correct_bar)

        if pair_key in used_pairs:
            continue
        used_pairs.add(pair_key)

        if random.choice([True, False]):
            bar = correct_bar
            answer = "Verdadeiro"
            question = {
            "question": f"O bar '{bar}' criou '{cocktail}'?",
            "options": ["Verdadeiro", "Falso"],
            "answer": answer,
            "correct_info": f"O cocktail '{cocktail}' foi criado pelo bar '{correct_bar}'."
        }
        else:
            wrong_bars = [item["bar"] for item in lista if item["bar"] != correct_bar]
            if not wrong_bars:
                continue
            bar = random.choice(wrong_bars)
            answer = "Falso"

            question = {
                "question": f"O bar '{bar}' criou '{cocktail}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": answer,
                "correct_info": f"A bebida '{cocktail}' foi criada pelo bar '{correct_bar}'.",
                "correct_info2": f"A bebida '{cocktail}' foi criada pelo bar '{correct_bar}'."
            }

        questions.append(question)

    print(f"Generated {len(questions)} CocktailsTF3 questions.")
    return questions

def genCocktailsMC1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMC1.json")
    if not lista:
        return []

    questions = []
    used_names = set()

    all_glasses = list(set(item["copo"] for item in lista if item["copo"].strip()))
    random.shuffle(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        nome = item["nome"]
        copo = item["copo"]
        if nome in used_names or not copo:
            continue
        used_names.add(nome)

        wrong_glasses = [g for g in all_glasses if g != copo]
        if len(wrong_glasses) < 3:
            continue
        options = [copo] + random.sample(wrong_glasses, 3)
        random.shuffle(options)

        question = {
            "question": f"Em que copo é preparada a bebida '{nome}'?",
            "options": options,
            "answer": copo
        }

        questions.append(question)

    print(f"Generated {len(questions)} CocktailsMC1 questions.")
    return questions

def genCocktailsMC2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMC2.json")
    if not lista:
        return []

    questions = []
    used_names = set()

    all_types = list(set(item["tipo"] for item in lista if item["tipo"].strip()))
    random.shuffle(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        nome = item["nome"]
        tipo = item["tipo"]

        if nome in used_names or not tipo:
            continue
        used_names.add(nome)

        wrong_types = [t for t in all_types if t != tipo]
        if len(wrong_types) < 3:
            continue

        options = [tipo] + random.sample(wrong_types, 3)
        random.shuffle(options)

        question = {
            "question": f"Que tipo de bebida é '{nome}'?",
            "options": options,
            "answer": tipo,
            "correct_info": f"A bebida '{nome}' é '{tipo}'."
        }

        questions.append(question)

    print(f"Generated {len(questions)} sMC2 questions.")
    return questions

def genCocktailsMC3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMC3.json")
    if not lista:
        return []

    questions = []
    used_names = set()

    all_locations = list(set(item["local"] for item in lista if item["local"].strip()))
    random.shuffle(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        nome = item["nome"]
        location = item["local"]

        if nome in used_names or not location:
            continue
        used_names.add(nome)

        wrong_locations = [l for l in all_locations if l != location]
        if len(wrong_locations) < 3:
            continue

        options = [location] + random.sample(wrong_locations, 3)
        random.shuffle(options)

        question = {
            "question": f"Onde foi criada a bebida '{nome}'?",
            "options": options,
            "answer": location,
            "correct_info": f"A bebida '{nome}' foi criada em {location}."
        }

        questions.append(question)

    print(f"Generated {len(questions)} CocktailsMC3 questions.")
    return questions

def genCocktailsMC3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMC3.json")
    if not lista:
        return []

    questions = []
    used_names = set()
    all_locations = list(set(item["local"] for item in lista if item["local"].strip()))
    random.shuffle(lista)
    num_query = len(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        nome = item["nome"]
        local = item["local"]
        if nome in used_names or not local:
            continue
        used_names.add(nome)

        wrong_locations = [l for l in all_locations if l != local]
        if len(wrong_locations) < 3:
            continue

        options = [local] + random.sample(wrong_locations, 3)
        random.shuffle(options)

        question = {
            "question": f"Onde foi criada a bebida '{nome}'?",
            "options": options,
            "answer": local,
            "correct_info": f"A bebida '{nome}' foi criada em {local}."
        }

        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} CocktailsMC3 questions.")
    return questions

def genCocktailsMatch1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMatch1.json")
    if not lista:
        return []

    questions = []
    used_pairs = set()
    random.shuffle(lista)
    num_query = len(lista)

    for _ in range(question_count):

        available_pairs = [pair for pair in lista if (pair["nome"], pair["preparacao"]) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair["nome"], pair["preparacao"])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  

        for pair in selected:
            used_pairs.add((pair["nome"], pair["preparacao"]))

        question = {
            "question": "Corresponda a preparação de cada bebida:",
            "pairs": [(pair["nome"], pair["preparacao"]) for pair in selected],
            "answer": [(pair["nome"], pair["preparacao"]) for pair in selected]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} CocktailsMatch1 questions.")
    return questions

def genCocktailsMatch2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMatch2.json")
    if not lista:
        return []

    questions = []
    used_pairs = set()
    random.shuffle(lista)
    num_query = len(lista)

    for _ in range(question_count):

        available_pairs = [pair for pair in lista if (pair["nome"], pair["local"]) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista[:]

        selected = []
        seen_names = set()
        seen_locals = set()

        for pair in available_pairs:
            nome = pair["nome"]
            local = pair["local"]

            if local in seen_locals:
                continue

            key = (nome, local)
            if key not in seen_names:
                selected.append(pair)
                seen_names.add(key)
                seen_locals.add(local)

            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  
        for pair in selected:
            used_pairs.add((pair["nome"], pair["local"]))

        question = {
            "question": "Corresponda a origem de cada bebida:",
            "pairs": [(pair["nome"], pair["local"]) for pair in selected],
            "answer": [(pair["nome"], pair["local"]) for pair in selected]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} CocktailsMatch2 questions.")
    return questions

def genCocktailsMatch3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/CocktailsMatch3.json")
    if not lista:
        return []

    for item in lista:
        item["quantidades"] = "; ".join([
            q.split("#")[-1] if "#" in q else q
            for q in item["quantidades"].split("; ")
        ])

    questions = []
    used_pairs = set()
    random.shuffle(lista)
    num_query = len(lista)

    for _ in range(question_count):
        
        available_pairs = [pair for pair in lista if (pair["nome"], pair["quantidades"]) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista


        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair["nome"], pair["quantidades"])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  
        
        for pair in selected:
            used_pairs.add((pair["nome"], pair["quantidades"]))

        question = {
            "question": "Corresponda o nome da bebida com a sua quantidade de ingredientes:",
            "pairs": [(pair["nome"], pair["quantidades"]) for pair in selected],
            "answer": [(pair["nome"], pair["quantidades"]) for pair in selected]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} CocktailsMatch3 questions.")
    return questions