import random
import json

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)
    
# funcao auxiliar para formatar o valor do revenue/budget
def format_money(amount):
    amount = int(amount)
    if amount >= 1_000_000_000:
        return f"{amount / 1_000_000_000:.1f}B$".replace('.0B$', 'B$')
    elif amount >= 1_000_000:
        return f"{amount // 1_000_000}M$"
    elif amount >= 1_000:
        return f"{amount // 1_000}K$"
    else:
        return f"{amount}$"
    

def genFilmesTF1(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesTF1.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        if random.choice([True, False]):
            question = {
                "question": f"O filme '{item['movie_title']}' foi realizado por {item['directors']}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O filme '{item['movie_title']}' foi realizado por {item['directors']}"
            }
        else:
            other_director = random.choice([i['directors'] for i in lista if i['directors'] != item['directors']])
            question = {
                "question": f"O filme '{item['movie_title']}' foi realizado por {other_director}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O filme '{item['movie_title']}' foi realizado por {item['directors']}",
                "correct_info2": f"O filme '{item['movie_title']}' foi realizado por {item['directors']}"
            }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesTF1 questions.")
    return questions

def genFilmesTF2(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesTF2.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for _ in range(question_count):
        film1, film2 = random.sample(lista, 2)
        budget1 = format_money(film1['budget'])
        budget2 = format_money(film2['budget'])
        answer = "Verdadeiro" if float(film1['budget']) > float(film2['budget']) else "Falso"
        question = {
            "question": f"O filme '{film1['movieTitle']}' (orçamento de {budget1}) tem um maior orçamento que o filme '{film2['movieTitle']}'?",
            "options": ["Verdadeiro", "Falso"],
            "answer": answer,
            "correct_info": f"O filme '{film2['movieTitle']}' tem um orçamento de {budget2}",
            "correct_info2": f"O filme '{film2['movieTitle']}' tem um orçamento de {budget2}"
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesTF2 questions.")
    return questions

def genFilmesTF3(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesTF3.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for _ in range(question_count):
        film1, film2 = random.sample(lista, 2)
        answer = "Verdadeiro" if int(film1['duration']) > int(film2['duration']) else "Falso"
        question = {
            "question": f"O filme '{film1['movieTitle']}' (duração de {film1['duration']} minutos) tem uma duração maior que o filme '{film2['movieTitle']}'?",
            "options": ["Verdadeiro", "Falso"],
            "answer": answer,
            "correct_info": f"O filme '{film2['movieTitle']}' tem {film2['duration']} minutos",
            "correct_info2": f"O filme '{film2['movieTitle']}' tem {film2['duration']} minutos"
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesTF3 questions.")
    return questions

def genFilmesMC1(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMC1.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        correct_year = int(item['releaseDate'][:4])
        other_years = random.sample([str(correct_year + offset) for offset in range(-5, 6) if offset != 0 and correct_year + offset <= 2025], 3)
        options = [str(correct_year)] + other_years
        random.shuffle(options)
        question = {
            "question": f"Em que ano estreou o filme '{item['movieTitle']}'?",
            "options": options,
            "answer": str(correct_year)
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMC1 questions.")
    return questions

def genFilmesMC2(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMC2.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        correct_tagline = item['description']
        other_taglines = random.sample([i['description'] for i in lista if i != item], 3)
        options = [correct_tagline] + other_taglines
        random.shuffle(options)
        question = {
            "question": f"Qual é a tagline do filme '{item['movieTitle']}'?",
            "options": options,
            "answer": correct_tagline
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMC2 questions.")
    return questions

def genFilmesMC3(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMC3.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    for item in lista[:question_count]:
        correct_company = item['companyNames']
        other_companies = random.sample(list(set(i['companyNames'] for i in lista if i != item)), 3)
        options = [correct_company] + other_companies
        random.shuffle(options)
        question = {
            "question": f"Qual destas opções produziu o filme '{item['movieTitle']}'?",
            "options": options,
            "answer": correct_company
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMC3 questions.")
    return questions

def genFilmesMatch1(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMatch1.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
            
        selected_pairs = []
        selected_casts = set()
        
        temp_available = available_pairs.copy()
        random.shuffle(temp_available)
        
        for pair in temp_available:
            if pair['cast'] not in selected_casts and len(selected_pairs) < 4:
                selected_pairs.append(pair)
                selected_casts.add(pair['cast'])
                used_pairs.add(tuple(pair.items()))
            
            if len(selected_pairs) == 4:
                break
                
        if len(selected_pairs) < 4:
            pairs = random.sample(available_pairs, min(4, len(available_pairs)))
            for pair in pairs:
                used_pairs.add(tuple(pair.items()))
        else:
            pairs = selected_pairs
            
        question = {
            "question": "Corresponde os filmes aos seus elencos:",
            "pairs": [(pair['movieTitle'], pair['cast']) for pair in pairs],
            "answer": [(pair['movieTitle'], pair['cast']) for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMatch1 questions.")
    return questions

def genFilmesMatch2(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMatch2.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
            
        selected_pairs = []
        selected_genres = set()
        
        temp_available = available_pairs.copy()
        random.shuffle(temp_available)
        
        for pair in temp_available:
            if pair['genreNames'] not in selected_genres and len(selected_pairs) < 4:
                selected_pairs.append(pair)
                selected_genres.add(pair['genreNames'])
                used_pairs.add(tuple(pair.items()))
            
            if len(selected_pairs) == 4:
                break
                
        if len(selected_pairs) < 4:
            pairs = random.sample(available_pairs, min(4, len(available_pairs)))
            for pair in pairs:
                used_pairs.add(tuple(pair.items()))
        else:
            pairs = selected_pairs
            
        question = {
            "question": "Corresponde os filmes aos seus géneros:",
            "pairs": [(pair['movieName'], pair['genreNames']) for pair in pairs],
            "answer": [(pair['movieName'], pair['genreNames']) for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMatch2 questions.")
    return questions

def genFilmesMatch3(question_count):
    if question_count == 0:
        return []
    lista = load_query_results("temas/query_results/FilmesMatch3.json")
    num_query = len(lista)

    questions = []
    random.shuffle(lista)
    used_pairs = set()
    for _ in range(question_count):
        available_pairs = [pair for pair in lista if tuple(pair.items()) not in used_pairs]
        if not available_pairs:
            used_pairs.clear()
            available_pairs = lista
            
        selected_pairs = []
        selected_revenues = set()
        
        temp_available = available_pairs.copy()
        random.shuffle(temp_available)
        
        for pair in temp_available:
            revenue_formatted = format_money(pair['revenue'])
            if revenue_formatted not in selected_revenues and len(selected_pairs) < 4:
                selected_pairs.append(pair)
                selected_revenues.add(revenue_formatted)
                used_pairs.add(tuple(pair.items()))
            
            if len(selected_pairs) == 4:
                break
                
        if len(selected_pairs) < 4:
            pairs = random.sample(available_pairs, min(4, len(available_pairs)))
            for pair in pairs:
                used_pairs.add(tuple(pair.items()))
        else:
            pairs = selected_pairs
            
        question = {
            "question": "Corresponde os filmes às suas receitas:",
            "pairs": [(pair['movieTitle'], format_money(pair['revenue'])) for pair in pairs],
            "answer": [(pair['movieTitle'], format_money(pair['revenue'])) for pair in pairs]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} FilmesMatch3 questions.")
    return questions