from query_utils import fetch_query_results

ontologia_file = "ontologias/books.ttl"

# Livros queries

#Qual foi a data de publicação do livro x? - true/false
sparql_query_livros_tf1 = """
PREFIX : <http://www.semanticweb.org/books/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
SELECT ?tituloLivro ?dataPublicacao
WHERE {
  ?livro a :Book ;
         :title ?tituloLivro ;
         :publishedIn ?dataPublicacao .
}
ORDER BY ?dataPublicacao

"""
fetch_query_results(sparql_query_livros_tf1, ontologia_file, "temas/query_results/LivrosTF1.json", format="turtle")

#A que série pertence a personagem x? - true/false
sparql_query_livros_tf2 = """
PREFIX : <http://www.semanticweb.org/books/>
SELECT DISTINCT ?nomePersonagem ?tituloSerie
WHERE {
  ?personagem a :Character ;
              :name ?nomePersonagem ;
              :appearsIn ?livro .
  ?livro a :Book ;
         :title ?tituloLivro ;
         :partOfSeries ?serie .
  ?serie :title ?tituloSerie .
}
ORDER BY (?nomePersonagem) (?tituloSerie)
"""
fetch_query_results(sparql_query_livros_tf2, ontologia_file, "temas/query_results/LivrosTF2.json", format="turtle")

#Quantas páginas tem o livro x? - true/false
sparql_query_livros_tf3 = """
PREFIX : <http://www.semanticweb.org/books/>
SELECT ?titulo ?paginas 
WHERE {
  ?livro a :Book ;
         :title ?titulo ;
         :numberOfPages ?paginas .
}
ORDER BY DESC(?paginas)

"""
fetch_query_results(sparql_query_livros_tf3, ontologia_file, "temas/query_results/LivrosTF3.json", format="turtle")

# Quais são os géneros do livro x? - multiplas
sparql_query_livros_mc1 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?titulo (GROUP_CONCAT(?generoNome; separator=", ") AS ?generos)
WHERE {
  ?livro a :Book ;
         :title ?titulo ;
         :hasGenre ?genero .
  ?genero :name ?generoNome .
}
GROUP BY ?titulo
ORDER BY ASC(?titulo)

"""
fetch_query_results(sparql_query_livros_mc1, ontologia_file, "temas/query_results/LivrosMC1.json", format="turtle")

# Quais são os prémios stribuídos ao livro x? - multiplas
sparql_query_livros_mc2 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?tituloLivro (GROUP_CONCAT(?premioNome; separator=", ") AS ?premios)
WHERE {
  ?livro a :Book ;
         :title ?tituloLivro ;
         :hasAwards ?premio .
  ?premio :name ?premioNome .
}
GROUP BY ?tituloLivro
ORDER BY ?tituloLivro

"""
fetch_query_results(sparql_query_livros_mc2, ontologia_file, "temas/query_results/LivrosMC2.json", format="turtle")

# Quais os livros que pertencem à série x? - multiplas
sparql_query_livros_mc3 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?tituloSerie ?tituloLivro
WHERE {
  ?series a :Series ;
          :title ?tituloSerie .
  ?book a :Book ;
        :partOfSeries ?series ;
        :title ?tituloLivro .
}
ORDER BY ?tituloSerie ?tituloLivro
"""
fetch_query_results(sparql_query_livros_mc3, ontologia_file, "temas/query_results/LivrosMC3.json", format="turtle")

# Qual foi a editora que publicou o livro x? - match
sparql_query_livros_match1 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?publisherName ?bookTitle
WHERE {
  ?publisher a :Publisher ;
             :name ?publisherName ;
             :hasPublished ?book .
  ?book a :Book ;
        :title ?bookTitle .
}
ORDER BY ?publisherName ?bookTitle

"""
fetch_query_results(sparql_query_livros_match1, ontologia_file, "temas/query_results/LivrosMatch1.json", format="turtle")

# Que autor escreveu o livro x? - match
sparql_query_livros_match2 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?bookTitle ?authorName
WHERE {
  ?author a :Author ;
          :name ?authorName .
  ?book a :Book ;
        :writtenBy ?author ;
        :title ?bookTitle .
}
ORDER BY ?bookTitle
"""
fetch_query_results(sparql_query_livros_match2, ontologia_file, "temas/query_results/LivrosMatch2.json", format="turtle")


# Qual foi o autor que criou a personagem x? - match
sparql_query_livros_match3 = """
PREFIX : <http://www.semanticweb.org/books/>

SELECT ?authorName (GROUP_CONCAT(DISTINCT ?characterName; separator=", ") AS ?characters)
WHERE {
  ?author a :Author ;
          :name ?authorName ;
          :hasWritten ?work .

  BIND(COALESCE(?book, ?work) AS ?targetBook)

  ?targetBook :hasCharacter ?character .
  ?character :name ?characterName .
}
GROUP BY ?authorName
ORDER BY ?authorName

"""
fetch_query_results(sparql_query_livros_match3, ontologia_file, "temas/query_results/LivrosMatch3.json", format="turtle")

