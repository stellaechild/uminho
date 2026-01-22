from query_utils import fetch_query_results

ontologia_file = "ontologias/Histria_de_Portugal.rdf"

# HISTORIA DE PORTUGAL queries
sparql_query_historia_tf1 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?nomeConq ?nomeRei WHERE {
        ?s a :Conquista ;
            :nome ?nomeConq ;
            :temReinado ?reinado .
        ?reinado :temMonarca ?monarca .
        ?monarca :nome ?nomeRei .
    }
"""
fetch_query_results(sparql_query_historia_tf1, ontologia_file, "query_results/HistoriaTF1.json")

sparql_query_historia_tf2 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?nome (COUNT(?mandato) AS ?numMandatos) WHERE {
        ?s a :Presidente .
        ?s :mandato ?mandato ;
            :nome ?nome .
    }
    GROUP BY ?nome
"""
fetch_query_results(sparql_query_historia_tf2, ontologia_file, "query_results/HistoriaTF2.json")

sparql_query_historia_tf3 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?batalha ?resultado WHERE {
        ?s a :Batalha ;
            :nome ?batalha ;
            :resultado ?resultado .
    }
"""
fetch_query_results(sparql_query_historia_tf3, ontologia_file, "query_results/HistoriaTF3.json")

sparql_query_historia_mc1 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?s ?p ?o WHERE {
        ?s a :Rei ;
        :nome ?p;
        :nascimento ?o.
    }
"""
fetch_query_results(sparql_query_historia_mc1, ontologia_file, "query_results/HistoriaMC1.json")

sparql_query_historia_mc2 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?acontecimento ?data WHERE {
        ?s a :Acontecimento ;
            :nome ?acontecimento ;
            :data ?data .
    }
"""
fetch_query_results(sparql_query_historia_mc2, ontologia_file, "query_results/HistoriaMC2.json")

sparql_query_historia_mc3 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?presidente ?partido WHERE {
        ?s a :Presidente ;
            :nome ?presidente ;
            :partido ?partidoURI .
        ?partidoURI :nome ?partido .
    }
"""
fetch_query_results(sparql_query_historia_mc3, ontologia_file, "query_results/HistoriaMC3.json")

sparql_query_historia_match1 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?nome ?cognomes WHERE {
        ?s a :Rei .
        ?s :nome ?nome .              
        ?s :cognomes ?cognomes . 
    }
"""
fetch_query_results(sparql_query_historia_match1, ontologia_file, "query_results/HistoriaMatch1.json")

sparql_query_historia_match2 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?monarca ?sucessor WHERE {
        ?monarcaURI a :Monarca ;
            :nome ?monarca ;
            :sucessor ?sucessorURI .
        ?sucessorURI :nome ?sucessor .
    }
"""
fetch_query_results(sparql_query_historia_match2, ontologia_file, "query_results/HistoriaMatch2.json")

sparql_query_historia_match3 = """
    PREFIX : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
    SELECT DISTINCT ?monarca ?comeco ?fim WHERE {
        ?s a :Reinado ;
            :temMonarca ?monarcaURI ;
            :comeco ?comeco ;
            :fim ?fim .
        ?monarcaURI :nome ?monarca .
    }
"""
fetch_query_results(sparql_query_historia_match3, ontologia_file, "query_results/HistoriaMatch3.json")
