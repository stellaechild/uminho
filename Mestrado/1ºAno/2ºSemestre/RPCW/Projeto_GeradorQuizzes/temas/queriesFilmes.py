from query_utils import fetch_query_results

ontologia_file = "ontologias/FilmesTop500.ttl"

# FILMES queries
sparql_query_filmes_tf1 = """
    PREFIX schema1: <http://schema.org/>
    PREFIX tmdb: <http://www.semanticweb.org/tomas/ontologies/2025/3/tmdb/>
    SELECT DISTINCT ?movie_title (GROUP_CONCAT(DISTINCT ?director_name; separator=", ") AS ?directors)
    WHERE {
        ?movie a schema1:Movie ;
                schema1:name ?movie_title ;
                schema1:director ?director .
        OPTIONAL { ?director rdfs:label ?director_name }
    }
    GROUP BY ?movie_title
"""
fetch_query_results(sparql_query_filmes_tf1, ontologia_file, "query_results/FilmesTF1.json", format="turtle")

sparql_query_filmes_tf2 = """
    PREFIX schema: <http://schema.org/>
    PREFIX tmdb: <http://www.semanticweb.org/tomas/ontologies/2025/3/tmdb/>
    SELECT DISTINCT ?movieTitle ?budget WHERE {
        ?movie rdf:type schema:Movie ;
            schema:name ?movieTitle ;
            tmdb:budget ?budget .
        FILTER(?budget != 0)
    }
"""
fetch_query_results(sparql_query_filmes_tf2, ontologia_file, "query_results/FilmesTF2.json", format="turtle")

sparql_query_filmes_tf3 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieTitle ?duration WHERE {
        ?movie rdf:type schema:Movie ;
               schema:name ?movieTitle ;
               schema:duration ?duration .
    }
"""
fetch_query_results(sparql_query_filmes_tf3, ontologia_file, "query_results/FilmesTF3.json", format="turtle")

sparql_query_filmes_mc1 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieTitle ?releaseDate WHERE {
        ?movie rdf:type schema:Movie ;
               schema:name ?movieTitle ;
               schema:datePublished ?releaseDate .
    }
"""
fetch_query_results(sparql_query_filmes_mc1, ontologia_file, "query_results/FilmesMC1.json", format="turtle")

sparql_query_filmes_mc2 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieTitle ?description WHERE {
        ?movie rdf:type schema:Movie ;
               schema:name ?movieTitle ;
               schema:description ?description .
    }
"""
fetch_query_results(sparql_query_filmes_mc2, ontologia_file, "query_results/FilmesMC2.json", format="turtle")

sparql_query_filmes_mc3 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieTitle (GROUP_CONCAT(DISTINCT ?companyName; separator=", ") AS ?companyNames) WHERE {
        ?movie rdf:type schema:Movie ;
            schema:name ?movieTitle ;
            schema:productionCompany ?company .
        ?company rdfs:label ?companyName .
    }
    GROUP BY ?movieTitle
"""
fetch_query_results(sparql_query_filmes_mc3, ontologia_file, "query_results/FilmesMC3.json", format="turtle")

sparql_query_filmes_match1 = """
    PREFIX schema: <http://schema.org/>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX tmdb: <http://www.semanticweb.org/tomas/ontologies/2025/3/tmdb/>
        SELECT DISTINCT ?movieTitle (GROUP_CONCAT(DISTINCT ?actorName; separator=", ") AS ?cast)
        WHERE {
          {
            SELECT ?movieTitle ?actorName ?order
            WHERE {
              ?movie a schema:Movie ;
                     schema:name ?movieTitle ;
                     schema:actor ?seq .
              ?seq a rdf:Seq ;
                   ?p ?actor .
              FILTER(regex(str(?p), "#_"))
              BIND(xsd:integer(REPLACE(str(?p), ".*_(\\\\d+)$", "$1")) AS ?order)
              ?actor rdfs:label ?actorName .
            }
            ORDER BY ?order
          }
        }
        GROUP BY ?movieTitle
"""
fetch_query_results(sparql_query_filmes_match1, ontologia_file, "query_results/FilmesMatch1.json", format="turtle")

sparql_query_filmes_match2 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieName (GROUP_CONCAT(DISTINCT ?genreName; separator=", ") AS ?genreNames)
    WHERE {
        ?movie rdf:type schema:Movie ;
               schema:name ?movieName ;
               schema:genre ?genre .
        ?genre rdfs:label ?genreName .
    }
    GROUP BY ?movieName
"""
fetch_query_results(sparql_query_filmes_match2, ontologia_file, "query_results/FilmesMatch2.json", format="turtle")

sparql_query_filmes_match3 = """
    PREFIX schema: <http://schema.org/>
    SELECT DISTINCT ?movieTitle ?revenue WHERE {
        ?movie rdf:type schema:Movie ;
            schema:name ?movieTitle ;
            tmdb:revenue ?revenue .
        FILTER(?revenue != 0)
    }
"""
fetch_query_results(sparql_query_filmes_match3, ontologia_file, "query_results/FilmesMatch3.json", format="turtle")
