import random
import json

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)

def genHistoriaTF1(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaTF1.json")
    num_query = len(lista)
    
    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        if random.choice([True, False]):
            question = {
                "question": f"A {item['nomeConq']} aconteceu durante o reinado de {item['nomeRei']}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"A {item['nomeConq']} aconteceu durante o reinado de {item['nomeRei']}"
            }
        else:
            other_rei = random.choice([i['nomeRei'] for i in lista if i['nomeRei'] != item['nomeRei']])
            question = {
                "question": f"A {item['nomeConq']} aconteceu durante o reinado de {other_rei}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"A {item['nomeConq']} aconteceu durante o reinado de {item['nomeRei']}",
                "correct_info2": f"A {item['nomeConq']} aconteceu durante o reinado de {item['nomeRei']}"
            }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaTF1 questions.")
    return questions

def genHistoriaTF2(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaTF2.json")
    num_query = len(lista)
    
    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        if random.choice([True, False]):
            question = {
                "question": f"O presidente {item['nome']} teve {item['numMandatos']} mandato(s)?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O presidente {item['nome']} teve {item['numMandatos']} mandato(s)"
            }
        else:
            other_terms = random.choice([i['numMandatos'] for i in lista if i['numMandatos'] != item['numMandatos']])
            question = {
                "question": f"O presidente {item['nome']} teve {other_terms} mandato(s)?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O presidente {item['nome']} teve {item['numMandatos']} mandato(s)",
                "correct_info2": f"O presidente {item['nome']} teve {item['numMandatos']} mandato(s)"
            }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaTF2 questions.")
    return questions

def genHistoriaTF3(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaTF3.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        if random.choice([True, False]):
            question = {
                "question": f"O resultado da '{item['batalha']}' é {item['resultado']}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O resultado da '{item['batalha']}' é {item['resultado']}"
            }
        else:
            other_result = random.choice([i['resultado'] for i in lista if i['resultado'] != item['resultado']])
            question = {
                "question": f"O resultado da '{item['batalha']}' é '{other_result}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O resultado da '{item['batalha']}' é {item['resultado']}",
                "correct_info2": f"O resultado da '{item['batalha']}' é {item['resultado']}"
            }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaTF3 questions.")
    return questions

def genHistoriaMC1(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMC1.json")
    num_query = len(lista)
    
    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        other_dates = random.sample([i['o'] for i in lista if i != item and i['o'] != item['o']], 3)
        options = other_dates + [item['o']]
        random.shuffle(options)
        question = {
            "question": f"Qual é a data de nascimento do Rei {item['p']}?",
            "options": options,
            "answer": item['o']
        }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMC1 questions.")
    return questions

def genHistoriaMC2(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMC2.json")
    num_query = len(lista)
    
    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        other_options = random.sample([i['data'] for i in lista if i != item and i['data'] != item['data']], 3)
        options = [item['data']] + other_options
        random.shuffle(options)
        question = {
            "question": f"Em que data ocorreu o/a '{item['acontecimento']}'?",
            "options": options,
            "answer": item['data']
        }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMC2 questions.")
    return questions

def genHistoriaMC3(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMC3.json")
    num_query = len(lista)
    
    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        other_options = random.sample([i['partido'] for i in lista if i != item and i['partido'] != item['partido']], 3)
        options = [item['partido']] + other_options
        random.shuffle(options)
        question = {
            "question": f"A que partido político pertenceu o presidente '{item['presidente']}'?",
            "options": options,
            "answer": item['partido']
        }
        questions.append(question)
    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMC3 questions.")
    return questions

def genHistoriaMatch1(question_count):
    used_pairs = set()
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMatch1.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
        pairs = random.sample(available_pairs, min(4, len(available_pairs)))
        for pair in pairs:
            used_pairs.add(tuple(pair.items()))
        question = {
            "question": "Corresponde os reis aos seus cognomes:",
            "pairs": [(pair['nome'], pair['cognomes']) for pair in pairs],
            "answer": [(pair['nome'], pair['cognomes']) for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMatch1 questions.")
    return questions

def genHistoriaMatch2(question_count):
    used_pairs = set()
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMatch2.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
        pairs = random.sample(available_pairs, min(4, len(available_pairs)))
        for pair in pairs:
            used_pairs.add(tuple(pair.items()))
        question = {
            "question": "Associe os monarcas aos seus sucessores:",
            "pairs": [(pair['monarca'], pair['sucessor']) for pair in pairs],
            "answer": [(pair['monarca'], pair['sucessor']) for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMatch2 questions.")
    return questions

def genHistoriaMatch3(question_count):
    used_pairs = set()
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/HistoriaMatch3.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
        pairs = random.sample(available_pairs, min(4, len(available_pairs)))
        for pair in pairs:
            used_pairs.add(tuple(pair.items()))
        question = {
            "question": "Corresponde os monarcas aos seus períodos de reinado:",
            "pairs": [(pair['monarca'], f"{pair['comeco']} - {pair['fim']}") for pair in pairs],
            "answer": [(pair['monarca'], f"{pair['comeco']} - {pair['fim']}") for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} HistoriaMatch3 questions.")
    return questions