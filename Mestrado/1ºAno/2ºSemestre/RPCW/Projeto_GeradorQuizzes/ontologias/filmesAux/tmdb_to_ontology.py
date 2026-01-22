import requests
from rdflib import Graph, Namespace, URIRef, Literal, BNode
from rdflib.namespace import RDF, RDFS, XSD
import time

API_KEY = "46243c828949a5830e397053cdcced4f"
BASE_URL = "https://api.themoviedb.org/3"
MOVIES_TO_FETCH = 500

TMDB = Namespace("http://www.semanticweb.org/tomas/ontologies/2025/3/tmdb/")
SCHEMA = Namespace("http://schema.org/")

g = Graph()
g.bind("tmdb", TMDB)
g.bind("schema", SCHEMA)

def fetch_top_movies(api_key, total):
    movies = []
    page = 1
    per_page = 20  # TMDB returns 20 movies per page
    pages_needed = (total + per_page - 1) // per_page
    while page <= pages_needed:
        url = f"{BASE_URL}/movie/top_rated?api_key={api_key}&page={page}"
        response = requests.get(url)
        if response.status_code != 200:
            print(f"Error fetching page {page}")
            break
        data = response.json()
        movies.extend(data.get("results", []))
        page += 1
        time.sleep(0.25)
    return movies[:total]

def fetch_movie_details(movie_id, api_key):
    url = f"{BASE_URL}/movie/{movie_id}?api_key={api_key}"
    response = requests.get(url)
    return response.json() if response.status_code == 200 else {}

def fetch_movie_credits(movie_id, api_key):
    url = f"{BASE_URL}/movie/{movie_id}/credits?api_key={api_key}"
    response = requests.get(url)
    return response.json() if response.status_code == 200 else {}

def add_movie_to_graph(movie, details, credits):
    movie_uri = TMDB[f"movie/{movie['id']}"]
    g.add((movie_uri, RDF.type, SCHEMA.Movie))
    # Title
    if details.get("title"):
        g.add((movie_uri, SCHEMA.name, Literal(details["title"], datatype=XSD.string)))
    # Release Date
    if details.get("release_date"):
        g.add((movie_uri, SCHEMA.datePublished, Literal(details["release_date"], datatype=XSD.date)))
    # Tagline
    if details.get("tagline"):
        g.add((movie_uri, SCHEMA.description, Literal(details["tagline"], datatype=XSD.string)))
    # Runtime
    if details.get("runtime"):
        g.add((movie_uri, SCHEMA.duration, Literal(details["runtime"], datatype=XSD.integer)))
    # Budget and Revenue
    if details.get("budget") is not None:
        g.add((movie_uri, TMDB.budget, Literal(details["budget"], datatype=XSD.integer)))
    if details.get("revenue") is not None:
        g.add((movie_uri, TMDB.revenue, Literal(details["revenue"], datatype=XSD.integer)))
    # Genres (limit to first 3)
    for genre in details.get("genres", [])[:3]:
        genre_uri = TMDB[f"genre/{genre['id']}"]
        g.add((genre_uri, RDF.type, SCHEMA.Genre))
        g.add((genre_uri, RDFS.label, Literal(genre["name"], datatype=XSD.string)))
        g.add((movie_uri, SCHEMA.genre, genre_uri))
    # Production Companies (limit to first 3)
    for comp in details.get("production_companies", [])[:3]:
        comp_uri = TMDB[f"company/{comp['id']}"]
        g.add((comp_uri, RDF.type, SCHEMA.Organization))
        g.add((comp_uri, RDFS.label, Literal(comp["name"], datatype=XSD.string)))
        g.add((movie_uri, SCHEMA.productionCompany, comp_uri))
    # Director (from credits.crew)
    directors = [person for person in credits.get("crew", []) if person.get("job") == "Director"]
    if directors:
        for director in directors:
            director_uri = TMDB[f"person/{director['id']}"]
            g.add((director_uri, RDF.type, SCHEMA.Person))
            g.add((director_uri, RDFS.label, Literal(director["name"], datatype=XSD.string)))
            g.add((movie_uri, SCHEMA.director, director_uri))
    # Cast (top 3 billing) sorted by TMDB's "order" property
    cast_list = sorted(credits.get("cast", []), key=lambda a: a.get("order", 999))[:3]
    if cast_list:
        cast_container = BNode()
        g.add((movie_uri, SCHEMA.actor, cast_container))
        g.add((cast_container, RDF.type, RDF.Seq))
        for idx, cast in enumerate(cast_list, start=1):
            actor_uri = TMDB[f"person/{cast['id']}"]
            g.add((actor_uri, RDF.type, SCHEMA.Person))
            g.add((actor_uri, RDFS.label, Literal(cast["name"], datatype=XSD.string)))
            g.add((cast_container, RDF[f"_{idx}"], actor_uri))

def main():
    movies = fetch_top_movies(API_KEY, MOVIES_TO_FETCH)
    print(f"Fetched {len(movies)} movies from TMDB")
    for movie in movies:
        details = fetch_movie_details(movie["id"], API_KEY)
        credits = fetch_movie_credits(movie["id"], API_KEY)
        add_movie_to_graph(movie, details, credits)
        time.sleep(0.25)
        
    g.serialize(destination="FilmesTop500.ttl", format="turtle")
    print("Ontology saved")

if __name__ == "__main__":
    main()
