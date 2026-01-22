import random
import json
from itertools import permutations

def load_query_results(file_path):
    with open(file_path, "r") as f:
        return json.load(f)

def genLivrosTF1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosTF1.json")

    books = []
    for item in lista:
        title = item["tituloLivro"]
        pub_date = item["dataPublicacao"]  # Mantém o formato original, ex: "MM/DD/YY"
        books.append((title, pub_date))

    questions = []
    random.shuffle(books)

    for title, pub_date in books[:question_count]:
        # Usa a data tal como está, sem conversão
        formatted_date = pub_date
        
        if random.choice([True, False]):
            # Versão verdadeira - data correta
            question = {
                "question": f"O livro '{title}' foi publicado em {formatted_date}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"Correto! '{title}' foi publicado em {formatted_date}."
            }
        else:
            # Versão falsa - altera o ano curto (os últimos 2 dígitos)
            try:
                month, day, year_short = pub_date.split("/")
                # Gera ano incorreto, entre 00 e 99, diferente do original
                wrong_year_short = year_short
                while wrong_year_short == year_short:
                    wrong_year_short = f"{random.randint(0,99):02d}"
                wrong_date = f"{month}/{day}/{wrong_year_short}"
            except Exception:
                # Caso formato inesperado, só altera o ano para "99"
                wrong_date = pub_date[:-2] + "99"
            
            question = {
                "question": f"O livro '{title}' foi publicado em {wrong_date}?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"Incorreto! '{title}' foi publicado em {formatted_date}, não em {wrong_date}."
            }
        
        questions.append(question)

    print(f"Generated {len(questions)} LivrosTF1 questions.")
    return questions[:question_count]

def genLivrosTF2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosTF2.json")

    character_series = {}
    all_series = set()

    for item in lista:
        character = item["nomePersonagem"]
        series = item["tituloSerie"]
        if character not in character_series:
            character_series[character] = []
        character_series[character].append(series)
        all_series.add(series)

    questions = []
    characters = list(character_series.keys())
    random.shuffle(characters)

    for character in characters:
        if len(questions) >= question_count:
            break

        correct_series = character_series[character]
        
        # True version (random correct series)
        if correct_series and random.choice([True, False]):
            series = random.choice(correct_series)
            question = {
                "question": f"A personagem '{character}' aparece na série de livros '{series}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"'{character}' aparece na série '{series}'."
            }
            questions.append(question)
        else:
            # False version (random wrong series)
            wrong_series = random.choice([s for s in all_series if s not in correct_series])
            question = {
                "question": f"A personagem '{character}' aparece na série '{wrong_series}'?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"'{character}' não aparece na série '{wrong_series}'. Ela aparece em: {', '.join(correct_series)}."
            }
            questions.append(question)

    print(f"Generated {len(questions)} LivrosTF2 questions.")
    return questions

def genLivrosTF3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosTF3.json")

    book_pages = []
    all_pages = set()

    for item in lista:
        title = item["titulo"]
        pages = item["paginas"]
        book_pages.append((title, pages))
        all_pages.add(pages)

    questions = []
    random.shuffle(book_pages)

    for title, pages in book_pages[:question_count]:
        if random.choice([True, False]):
            # True version
            question = {
                "question": f"O livro '{title}' tem {pages} páginas?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Verdadeiro",
                "correct_info": f"O livro '{title}' tem {pages} páginas."
            }
        else:
            # False version with random wrong page count
            wrong_pages = random.choice([p for p in all_pages if p != pages])
            question = {
                "question": f"O livro '{title}' tem {wrong_pages} páginas?",
                "options": ["Verdadeiro", "Falso"],
                "answer": "Falso",
                "correct_info": f"O livro '{title}' tem {pages} páginas, não {wrong_pages}."
            }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosTF3 questions.")
    return questions

def genLivrosMC1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMC1.json")

    book_genres = {}
    all_genres = set()

    for item in lista:
        title = item["titulo"]
        genres = [g.strip() for g in item["generos"].split(",")]
        book_genres[title] = genres
        all_genres.update(genres)

    questions = []
    books = list(book_genres.keys())
    random.shuffle(books)

    for book in books[:question_count]:
        correct_genres = book_genres[book]
        if len(correct_genres) < 2:
            continue
            
        # Select 3 wrong genres
        wrong_genres = random.sample([g for g in all_genres if g not in correct_genres], 3)
        
        # Combine 1 correct with 3 wrong
        options = [correct_genres[0]] + wrong_genres
        random.shuffle(options)
        
        question = {
            "question": f"Qual dos seguintes géneros pertence ao livro '{book}'?",
            "options": options,
            "answer": correct_genres[0],
            "correct_info": f"Os géneros de '{book}' são: {', '.join(correct_genres)}."
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMC1 questions.")
    return questions

def genLivrosMC2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMC2.json")

    book_awards = {}
    all_awards = set()

    for item in lista:
        title = item["tituloLivro"]
        awards = [a.strip() for a in item["premios"].split(",")]
        book_awards[title] = awards
        all_awards.update(awards)

    questions = []
    books = list(book_awards.keys())
    random.shuffle(books)

    for book in books[:question_count]:
        correct_awards = book_awards[book]
        if not correct_awards:
            continue
            
        # Select 3 wrong awards
        wrong_awards = random.sample([a for a in all_awards if a not in correct_awards], 3)
        
        # Combine 1 correct with 3 wrong
        options = [correct_awards[0]] + wrong_awards
        random.shuffle(options)
        
        question = {
            "question": f"Qual dos seguintes prémios foi atribuído ao livro '{book}'?",
            "options": options,
            "answer": correct_awards[0],
            "correct_info": f"Os prémios de '{book}' são: {', '.join(correct_awards)}."
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMC2 questions.")
    return questions

def genLivrosMC3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMC3.json")

    # Create dictionary of series to books and collect all books
    series_books = {}
    all_books = set()
    for item in lista:
        series = item["tituloSerie"]
        book = item["tituloLivro"]
        if series not in series_books:
            series_books[series] = []
        series_books[series].append(book)
        all_books.add(book)

    questions = []
    series_list = [s for s in series_books.keys() if len(series_books[s]) >= 2]
    random.shuffle(series_list)

    for series in series_list[:question_count]:
        correct_books = series_books[series]
        
        # Ensure we have enough wrong books to choose from
        available_wrong_books = [b for b in all_books if b not in correct_books]
        if len(available_wrong_books) < 3:
            continue  # Skip if not enough wrong books available
            
        # Select options - 1 correct + 3 wrong
        correct_option = random.choice(correct_books)  # Random correct book from series
        wrong_options = random.sample(available_wrong_books, 3)
        options = [correct_option] + wrong_options
        random.shuffle(options)
        
        question = {
            "question": f"Qual dos seguintes livros pertence à série '{series}'?",
            "options": options,
            "answer": correct_option,
            "correct_info": f"Livros da série '{series}': {', '.join(correct_books)}",
            "all_series_books": correct_books  # Useful for debugging
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMC3 questions (requested: {question_count}).")
    return questions[:question_count]

def genLivrosMatch1(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMatch1.json")

    publisher_books = []
    for item in lista:
        publisher = item["publisherName"]
        book = item["bookTitle"]
        publisher_books.append((publisher, book))

    questions = []
    used_pairs = set()
    random.shuffle(publisher_books)

    for _ in range(question_count):
        available_pairs = [p for p in publisher_books if p not in used_pairs]
        
        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = publisher_books[:]

        selected = random.sample(available_pairs, 4)
        for pair in selected:
            used_pairs.add(pair)

        question = {
            "question": "Corresponda cada livro à sua editora:",
            "pairs": selected,
            "answer": selected
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMatch1 questions.")
    return questions

def genLivrosMatch2(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMatch2.json")

    author_books = []
    for item in lista:
        author = item["authorName"]
        book = item["bookTitle"]
        author_books.append((author, book))

    questions = []
    used_pairs = set()
    random.shuffle(author_books)

    for _ in range(question_count):
        available_pairs = [p for p in author_books if p not in used_pairs]
        
        if len(available_pairs) < 4:
            used_pairs.clear()
            available_pairs = author_books[:]

        selected = random.sample(available_pairs, 4)
        for pair in selected:
            used_pairs.add(pair)

        question = {
            "question": "Corresponda cada livro ao seu autor:",
            "pairs": selected,
            "answer": selected
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMatch2 questions.")
    return questions

def genLivrosMatch3(question_count):
    if question_count == 0:
        return []

    lista = load_query_results("temas/query_results/LivrosMatch3.json")

    # Create a list of author-character pairs
    author_characters = []
    for item in lista:
        author = item["authorName"]
        characters = [c.strip() for c in item["characters"].split(",")]
        author_characters.append((author, characters))

    questions = []
    used_authors = set()
    random.shuffle(author_characters)

    for _ in range(question_count):
        # Find available authors we haven't used yet
        available_authors = [ac for ac in author_characters if ac[0] not in used_authors]
        
        # If we don't have enough, reset and reuse authors
        if len(available_authors) < 4:
            used_authors.clear()
            available_authors = author_characters[:]

        # Select 4 authors with their characters
        selected = random.sample(available_authors, 4)
        for author, _ in selected:
            used_authors.add(author)

        # Create the matching pairs
        pairs = [(author, ", ".join(characters[:3])) for author, characters in selected]
        
        question = {
            "question": "Corresponda cada autor às suas personagens:",
            "pairs": pairs,
            "answer": pairs,
            "additional_info": [f"{len(characters)} personagens no total" for _, characters in selected]
        }
        questions.append(question)

    print(f"Generated {len(questions)} LivrosMatch3 questions.")
    return questions