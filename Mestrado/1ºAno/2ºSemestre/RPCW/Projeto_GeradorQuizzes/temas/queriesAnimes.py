from query_utils import fetch_query_results

ontologia_file = "ontologias/animes.ttl"

# Anime
# Este anime é de ___?
sparql_query_anime_tf1 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?anime (GROUP_CONCAT(?genero; separator="; ") as ?generos) WHERE {
        ?s a :Anime ;
        :title_english ?anime ;
        :hasGenre/:name ?genero .
            
    }group by ?anime 
"""
#fetch_query_results(sparql_query_anime_tf1, ontologia_file, "query_results/AnimesTF1.json",format="turtle")

# Este anime estreou em ___?
sparql_query_anime_tf2 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?anime ?estreia WHERE {
        ?s a :Anime ;
            :title_english ?anime ;
            :premiered ?estreia.
            FILTER(STRLEN(STR(?estreia)) > 0)
    }
"""
fetch_query_results(sparql_query_anime_tf2, ontologia_file, "query_results/AnimesTF2.json",format="turtle")

# Quem produziu o anime foi ______?
sparql_query_anime_tf3 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?producer (GROUP_CONCAT(?anime; separator = ";" ) as ?animes) WHERE {
        ?s a :Anime ;
            :title_english ?anime ;
            :hasProducer/:name ?producer .
    } group by ?producer
"""
#fetch_query_results(sparql_query_anime_tf3, ontologia_file, "query_results/AnimesTF3.json",format="turtle")

# Qual das músicas seguintes é do anime ____?
sparql_query_anime_mc1 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?anime (GROUP_CONCAT(?musica; separator = ";" ) as ?musicas) WHERE {
        ?s a :Anime ;
            :title_english ?anime ;
            :hasTheme/:title ?musica .
    } group by ?anime
"""

#fetch_query_results(sparql_query_anime_mc1, ontologia_file, "query_results/AnimesMC1.json",format="turtle")

# Qual destes produtores produziu mais animes?
sparql_query_anime_mc2 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?produtor (COUNT(?anime) as ?num_animes) WHERE {
        ?anime a :Anime ;
            :hasProducer/:name ?produtor ;
    } group by ?produtor
    order by desc(?num_animes) 
"""
#fetch_query_results(sparql_query_anime_mc2, ontologia_file, "query_results/AnimesMC2.json",format="turtle")

# Qual dos seguintes animes tem melhor pontuação?
sparql_query_anime_mc3 = """
        PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT ?anime ?score WHERE {
        ?s a :Anime ;
            :title_english ?anime ;
            :score ?score .
    } order by desc(?score)
"""

#fetch_query_results(sparql_query_anime_mc3, ontologia_file, "query_results/AnimesMC3.json",format="turtle")

# Faça a correspondência entre o título em inglês e japonês dos animes:  
sparql_query_anime_match1 = """
        PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT ?english ?japanese WHERE {
        ?s a :Anime ;
            :title_english ?english ;
            :title_japanese ?japanese .
    } 
"""
#fetch_query_results(sparql_query_anime_match1, ontologia_file, "query_results/AnimesMatch1.json",format="turtle")

# Faça a correspondência entre o anime e o seu respetivo estúdio:
sparql_query_anime_match2 = """
    PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>

    SELECT DISTINCT ?estudio (GROUP_CONCAT(?anime; separator = ";" ) as ?animes) WHERE {
        ?a a :Anime ;
        :title_english ?anime ;
        :hasStudio/:name ?estudio ; 
    } group by ?estudio
"""

#fetch_query_results(sparql_query_anime_match2, ontologia_file, "query_results/AnimesMatch2.json",format="turtle")

# Qual é o artista de cada música?
sparql_query_anime_match3 = """
        PREFIX : <http://www.semanticweb.org/rodrigop/ontologies/2021/3/animes#>
    SELECT DISTINCT ?artista ?titulo WHERE {
        ?m a :Theme_Music ;
        :artist ?artista ;
        :title ?titulo ;
    } 
"""
#fetch_query_results(sparql_query_anime_match3, ontologia_file, "query_results/AnimesMatch3.json",format="turtle")


