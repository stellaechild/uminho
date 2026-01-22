import random
import json

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)
    
def genAnimesTF1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesTF1.json")

    anime_to_genres = {}
    all_genres = set()

    for item in lista:
        genres = [g.strip() for g in item["generos"].split(";") if g.strip()]
        anime_to_genres[item["anime"]] = genres
        all_genres.update(genres)

    questions = []
    used_animes = set()
    all_genres = list(all_genres)
    random.shuffle(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        anime = item["anime"]
        genres = anime_to_genres.get(anime, [])
        if not genres or anime in used_animes:
            continue
        used_animes.add(anime)

        if random.choice([True, False]):
            genre = random.choice(genres)
            answer = "Verdadeiro"
            question = {
            "question": f"O anime '{anime}' é do género {genre}?",
            "options": ["Verdadeiro", "Falso"],
            "answer": answer,
            "correct_info": f"O anime '{anime}' possui os seguintes géneros: {', '.join(genres)}"
        }
        else:
            wrong_genres = [g for g in all_genres if g not in genres]
            if not wrong_genres:
                continue
            genre = random.choice(wrong_genres)
            answer = "Falso"

            question = {
                "question": f"O anime '{anime}' é do género {genre}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": answer,
                "correct_info": f"O(s) género(s) de '{anime}' são: {', '.join(genres)}",
                "correct_info2": f"O(s) género(s) de '{anime}' são: {', '.join(genres)}",
            }

        questions.append(question)

    print(f"Generated {len(questions)} AnimesTF1 questions.")
    return questions

def genAnimesTF2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesTF2.json")
    if not lista:
        return []

    for item in lista:
        if "url" in item:
            item["url"] = item["url"].replace("myanimelist.cdn-dena.com", "myanimelist.net")

    questions = []
    random.shuffle(lista)

    for item in lista[:question_count]:
        anime = item['anime']
        estreia = item['estreia']

        if random.choice([True, False]):
            question = {
                "question": f"O anime '{anime}' estreou em {estreia}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"'{anime}' estreou em {estreia}."
            }
        else:
            other = random.choice([i for i in lista if i['anime'] != anime])
            wrong_estreia = other['estreia']
            question = {
                "question": f"O anime '{anime}' estreou em {wrong_estreia}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"'{anime}' estreou em {estreia}, não em {wrong_estreia}.",
                "correct_info2": f"'{anime}' estreou em {estreia}, não em {wrong_estreia}.",
            }
        questions.append(question)

    print(f"Generated {len(questions)} AnimesTF2 questions.")
    return questions


def genAnimesTF3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesTF3.json")
    
    anime_to_producer = {}
    all_animes = []
    all_producers = set()

    for entry in lista:
        producer = entry["producer"]
        animes = [anime.strip() for anime in entry["animes"].split(";") if anime.strip()]
        for anime in animes:
            anime_to_producer[anime] = producer
            all_animes.append(anime)
        all_producers.add(producer)

    questions = []
    used_animes = set()

    random.shuffle(all_animes)

    for anime in all_animes:
        if len(questions) >= question_count:
            break
        if anime in used_animes:
            continue
        used_animes.add(anime)

        correct_producer = anime_to_producer[anime]

        if random.choice([True, False]):
            question = {
                "question": f"Quem produziu o anime '{anime}' foi {correct_producer}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O produtor correto de '{anime}' é {correct_producer}."
            }
        else:
            wrong_producers = [p for p in all_producers if p != correct_producer]
            if not wrong_producers:
                continue 
            wrong_producer = random.choice(wrong_producers)
            question = {
                "question": f"Quem produziu o anime '{anime}' foi {wrong_producer}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O produtor correto de '{anime}' é {correct_producer}.",
                "correct_info2": f"O produtor correto de '{anime}' é {correct_producer}."
            }

        questions.append(question)

    print(f"Generated {len(questions)} AnimesTF3 questions.")
    return questions

def genAnimesMC1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMC1.json")
    num_query = len(lista)

    anime_to_songs = {}
    all_songs = []

    for item in lista:
        anime = item["anime"]
        songs = [s.strip() for s in item["musicas"].split(";") if s.strip()]
        anime_to_songs[anime] = songs
        all_songs.extend(songs)

    questions = []
    used_animes = set()
    random.shuffle(lista)

    for item in lista:
        if len(questions) >= question_count:
            break

        anime = item["anime"]
        correct_songs = anime_to_songs.get(anime, [])
        if not correct_songs or anime in used_animes:
            continue
        used_animes.add(anime)

        correct_song = random.choice(correct_songs)

        wrong_choices = [s for s in all_songs if s not in correct_songs]
        if len(wrong_choices) < 3:
            continue  
        distractors = random.sample(wrong_choices, 3)

        options = [correct_song] + distractors
        random.shuffle(options)

        question = {
            "question": f"Qual das músicas seguintes é do anime '{anime}'?",
            "options": options,
            "answer": correct_song,
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} AnimesMC1 questions.")
    return questions

def genAnimesMC2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMC2.json")
    num_query = len(lista)

    for item in lista:
        item["num_animes"] = int(item["num_animes"])

    questions = []
    used_sets = set()

    while len(questions) < question_count and len(lista) >= 4:
        group = random.sample(lista, 4)

        producers_key = tuple(sorted([p["produtor"] for p in group]))
        if producers_key in used_sets:
            continue
        used_sets.add(producers_key)

        correct = max(group, key=lambda x: x["num_animes"])
        options = [p["produtor"] for p in group]
        random.shuffle(options)

        question = {
            "question": "Qual destes produtores produziu mais animes?",
            "options": options,
            "answer": correct["produtor"],
            "correct_info": f"O produtor {correct['produtor']} produziu {correct['num_animes']} animes, enquanto que os outros pruduziram: {', '.join(str(p['num_animes']) for p in group if p != correct)}.",
            "correct_info2": f"O produtor {correct['produtor']} produziu {correct['num_animes']} animes, enquanto que os outros pruduziram: {', '.join(str(p['num_animes']) for p in group if p != correct)}.",
        }
        questions.append(question)

    print(f"Out of {num_query} producers ---> generated {len(questions)} AnimesMC2 questions.")
    return questions

def genAnimesMC3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMC3.json")
    num_query = len(lista)

    for item in lista:
        item["score"] = float(item["score"])

    questions = []
    used_sets = set()

    while len(questions) < question_count and len(lista) >= 4:
        group = random.sample(lista, 4)

        anime_names = tuple(sorted([a["anime"] for a in group]))
        if anime_names in used_sets:
            continue
        used_sets.add(anime_names)

        correct = max(group, key=lambda x: x["score"])
        options = [anime["anime"] for anime in group]
        random.shuffle(options)

        others = ', '.join("{} ({})".format(a['anime'], a['score']) for a in group if a != correct)
        question = {
            "question": "Qual dos seguintes animes tem melhor pontuação no AnimePlanet?",
            "options": options,
            "answer": correct["anime"],
            "correct_info": f"O anime '{correct['anime']}' tem a melhor pontuação com {correct['score']} pontos, enquanto que os outros animes têm: {others}.",
            "correct_info2": f"O anime '{correct['anime']}' tem a melhor pontuação com {correct['score']} pontos, enquanto que os outros animes têm: {others}.",
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} AnimesMC3 questions.")
    return questions

def genAnimesMatch1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMatch1.json")
    if not lista:
        return []

    num_query = len(lista)
    questions = []
    used_pairs = set()
    random.shuffle(lista)

    for _ in range(question_count):

        available_pairs = [pair for pair in lista if (pair["english"], pair["japanese"]) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista[:]

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair["english"], pair["japanese"])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  

        for pair in selected:
            used_pairs.add((pair["english"], pair["japanese"]))

        formatted_pairs = [(pair["english"], pair["japanese"]) for pair in selected]

        question = {
            "question": "Faça a correspondência entre o título em inglês e japonês dos animes:",
            "pairs": formatted_pairs,
            "answer": formatted_pairs
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} AnimesMatch1 questions.")
    return questions

import random

def genAnimesMatch2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMatch2.json")
    if not lista:
        return []

    flat_pairs = []
    for item in lista:
        studio = item["estudio"]
        animes = [a.strip() for a in item["animes"].split(";") if a.strip()]
        for anime in animes:
            flat_pairs.append({"anime": anime, "studio": studio})

    questions = []
    used_pairs = set()
    random.shuffle(flat_pairs)

    for _ in range(question_count):
        available_pairs = [
            pair for pair in flat_pairs
            if (pair["anime"], pair["studio"]) not in used_pairs
        ]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = flat_pairs

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair["anime"], pair["studio"])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue

        for pair in selected:
            used_pairs.add((pair["anime"], pair["studio"]))

        # Criar a pergunta
        question = {
            "question": "Faça a correspondência entre o anime e o seu respetivo estúdio:",
            "pairs": [(pair["anime"], pair["studio"]) for pair in selected],
            "answer": [(pair["anime"], pair["studio"]) for pair in selected]
        }
        questions.append(question)

    print(f"Out of {len(flat_pairs)} flat pairs ---> generated {len(questions)} AnimesMatch2 questions.")
    return questions


def genAnimesMatch3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/AnimesMatch3.json")
    num_query = len(lista)

    questions = []
    used_pairs = set()
    random.shuffle(lista)

    for _ in range(question_count):
        
        available_pairs = [pair for pair in lista if (pair["titulo"], pair["artista"]) not in used_pairs]

        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = lista

        selected = []
        seen_in_question = set()
        for pair in available_pairs:
            key = (pair["titulo"], pair["artista"])
            if key not in seen_in_question:
                selected.append(pair)
                seen_in_question.add(key)
            if len(selected) == 4:
                break

        if len(selected) < 4:
            continue  

        for pair in selected:
            used_pairs.add((pair["titulo"], pair["artista"]))

        question = {
            "question": "Qual é o artista de cada música?",
            "pairs": [(pair["titulo"], pair["artista"]) for pair in selected],
            "answer": [(pair["titulo"], pair["artista"]) for pair in selected]
        }
        questions.append(question)

    print(f"Out of {num_query} query results ---> generated {len(questions)} AnimesMatch3 questions.")
    return questions