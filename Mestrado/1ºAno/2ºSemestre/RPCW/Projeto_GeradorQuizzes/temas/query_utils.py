import rdflib
from rdflib import URIRef

import json
import os

def fetch_query_results(sparql_query, onto_file, output_file, format="xml"):
    os.makedirs(os.path.dirname(output_file), exist_ok=True)
    
    graph = rdflib.Graph()
    graph.parse(onto_file, format=format)
    result = graph.query(sparql_query)
    
    lista = [{str(var): str(row[var]) for var in row.labels} for row in result]
    
    with open(output_file, "w") as f:
        json.dump(lista, f, indent=4)
