import requests
from tabulate import tabulate

def query_graphdb(endpoint_url, sparql_query):
    # Set up the headers
    headers = {
        'Accept': 'application/json',  # You can change this based on the response format you need
    }
    
    # Make the GET request to the GraphDB endpoint
    response = requests.get(endpoint_url, params={'query': sparql_query}, headers=headers)
    
    if response.status_code == 200:
        return response.json()  # Return the JSON response from the GraphDB endpoint
    else:
        raise Exception(f"Error {response.status_code}: {response.text}")

# Example usage:
endpoint = "http://localhost:7200/repositories/HistoriaPT"
sparql_query = """
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
SELECT ?n ?o
    WHERE {
        ?s a :Rei.
    	?s :nome ?n .
    	?s :nascimento ?o.
    }
"""
result = query_graphdb(endpoint, sparql_query)

lista = []
for r in result['results']['bindings']:
    t = (r['n']['value'].split('#')[-1], r['o']['value'].split('#')[-1])
    lista.append(t)
print(tabulate(lista))
