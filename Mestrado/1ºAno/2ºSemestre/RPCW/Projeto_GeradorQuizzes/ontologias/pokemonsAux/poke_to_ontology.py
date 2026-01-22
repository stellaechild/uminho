import pandas as pd
from rdflib import Graph, Namespace, URIRef, Literal
from rdflib.namespace import RDF, OWL, RDFS, XSD

def create_uri(value):
    return str(value).strip().replace(" ", "_").replace("-", "_").replace("'", "").replace('"', "").capitalize()


def create_pokemon_ontology(csv_path, ttl_output):
    df = pd.read_csv(csv_path)
    g = Graph()

    ns = Namespace("http://www.semanticweb.org/yunzhu/ontologies/2025/pokemon#")
    g.bind("", ns)
    g.bind("rdf", RDF)
    g.bind("rdfs", RDFS)
    g.bind("owl", OWL)
    g.bind("xsd", XSD)

    contra_map = {
        2.0: ns.ExcelenteContra,
        1.75: ns.MuitoBomContra,
        1.5: ns.BomContra,
        1.25: ns.NormalBomContra,
        1.0: ns.NormalContra,
        0.75: ns.NormalMauContra,
        0.5: ns.MauContra,
        0.25: ns.MuitoMauContra,
    }

    elementos = [
        "bug", "dark", "dragon", "electric", "fairy", "fight", "fire", "flying",
        "ghost", "grass", "ground", "ice", "normal", "poison", "psychic",
        "rock", "steel", "water"
    ]

    for el in elementos:
        el_uri = URIRef(ns[create_uri(el)])
        g.add((el_uri, RDF.type, OWL.NamedIndividual))
        g.add((el_uri, RDF.type, ns.Elemento))

    individual_cache = {}
    index_by_prefix = {}

    def get_or_create_individual(class_type, prop, value, prefix, datatype=XSD.integer):
        key = (class_type, prop, str(value))
        if key not in individual_cache:
            index = index_by_prefix.get(prefix, 0)
            uri = ns[f"{prefix}_{index}"]
            index_by_prefix[prefix] = index + 1

            individual_cache[key] = uri

            g.add((uri, RDF.type, OWL.NamedIndividual))
            g.add((uri, RDF.type, class_type))
            g.add((uri, prop, Literal(value, datatype=datatype)))
        return individual_cache[key]

    for _, row in df.iterrows():
        poke_uri = ns[f"pokemon_{int(row['pokedex_number'])}"]
        g.add((poke_uri, RDF.type, OWL.NamedIndividual))
        g.add((poke_uri, RDF.type, ns.Pokémon))

        g.add((poke_uri, ns.temNome, Literal(row["name"], datatype=XSD.string)))
        g.add((poke_uri, ns.temNomeJaponês, Literal(row["japanese_name"], datatype=XSD.string)))
        g.add((poke_uri, ns.temAltura, Literal(float(row["height_m"]), datatype=XSD.float)))
        g.add((poke_uri, ns.temPeso, Literal(float(row["weight_kg"]), datatype=XSD.float)))
        g.add((poke_uri, ns.éLendário, Literal(bool(row["is_legendary"]), datatype=XSD.boolean)))

        g.add((poke_uri, ns.temElemento1, ns[create_uri(row["type1"])]))
        if pd.notna(row["type2"]):
            g.add((poke_uri, ns.temElemento2, ns[create_uri(row["type2"])]))

        for el in elementos:
            val = float(row.get(f"against_{el}", 1.0))
            prop = contra_map.get(val)
            if prop:
                g.add((poke_uri, prop, ns[create_uri(el)]))

        try:
            abilities = eval(row["abilities"])
            for ab in abilities:
                ab_uri = ns[create_uri(ab)]
                g.add((ab_uri, RDF.type, OWL.NamedIndividual))
                g.add((ab_uri, RDF.type, ns.Abilidade))
                g.add((poke_uri, ns.temAbilidade, ab_uri))
        except:
            pass

        ataque_n = get_or_create_individual(ns.AtaqueNormal, ns.temAtaque, row["attack"], "ataque_normal")
        ataque_e = get_or_create_individual(ns.AtaqueEspecial, ns.temAtaque, row["sp_attack"], "ataque_especial")
        defesa_n = get_or_create_individual(ns.DefesaNormal, ns.temDefesa, row["defense"], "defesa_normal")
        defesa_e = get_or_create_individual(ns.DefesaEspecial, ns.temDefesa, row["sp_defense"], "defesa_especial")
        hp       = get_or_create_individual(ns.HP, ns.temHP, row["hp"], "hp")
        vel      = get_or_create_individual(ns.Velocidade, ns.temVelocidade, row["speed"], "velocidade")
        passos   = get_or_create_individual(ns.PassosChocarOvo, ns.temPassosChocarOvo, row["base_egg_steps"], "passos")
        felicidade = get_or_create_individual(ns.Felicidade, ns.temFelicidade, row["base_happiness"], "felicidade")
        exp      = get_or_create_individual(ns.ExperiênciaParaCrescimento, ns.experiênciaCrescimento, row["experience_growth"], "experiencia")
        taxa = get_or_create_individual(ns.TaxaDeCaptura, ns.taxaCaptura, row["capture_rate"], "taxa_captura")
        perc     = get_or_create_individual(ns.PercentagemMacho, ns.temPercentagemMacho, row["percentage_male"], "percentagem_macho", XSD.decimal)
        geracao  = get_or_create_individual(ns.Geração, ns.númeroGeração, row["generation"], "geracao")
        classif  = get_or_create_individual(ns.Classificação, ns.temClassificação, row["classfication"], "classificacao", XSD.string)

        g.add((poke_uri, ns.temAtaqueNormal, ataque_n))
        g.add((poke_uri, ns.temAtaqueEspecial, ataque_e))
        g.add((poke_uri, ns.temDefesaNormal, defesa_n))
        g.add((poke_uri, ns.temDefesaEspecial, defesa_e))
        g.add((poke_uri, ns.temHP, hp))
        g.add((poke_uri, ns.temVelocidade, vel))
        g.add((poke_uri, ns.temPassosDeOvo, passos))
        g.add((poke_uri, ns.temFelicidade, felicidade))
        g.add((poke_uri, ns.temExperiênciaParaCrescimento, exp))
        g.add((poke_uri, ns.temTaxaDeCaptura, taxa))
        g.add((poke_uri, ns.temPercentagemMacho, perc))
        g.add((poke_uri, ns.temGeração, geracao))
        g.add((poke_uri, ns.temClassificação, classif))

    g.serialize(destination=ttl_output, format="turtle")
    print(f"Ontologia TTL gerada com sucesso: {ttl_output}")


if __name__ == "__main__":
    create_pokemon_ontology("pokemon.csv", "../pokemon.ttl")
