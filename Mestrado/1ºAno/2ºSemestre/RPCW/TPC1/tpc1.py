import json 

f = open("emd.json")
emds = json.load(f)

output = ""
modalidades = {}
clubes = {}
pessoas = {}

for i,e in enumerate(emds):

    if e['email'] not in pessoas:
        pessoas[e['email']] = "P" + str(len(pessoas))
        output += f"""
###  http://mirtilo/rpcw.di.uminho.pt/2025/EMD#{pessoas[e['email']]}
:{pessoas[e['email']]} rdf:type owl:NamedIndividual ,
             :Pessoa ;
    :email "{e['email']}" ;
    :genero "{e['género']}" ;
    :idade {e['idade']} ;
    :morada "{e['morada']}" ;
    :nome "{e['nome']['primeiro']} {e['nome']['último']}" .

        """
    idPessoa = pessoas[e['email']]

    if e['clube'] not in clubes:
        clubes[e['clube']] = "C" + str(len(clubes))
        output += f"""
###  http://mirtilo/rpcw.di.uminho.pt/2025/EMD#{clubes[e['clube']]}
:{clubes[e['clube']]} rdf:type owl:NamedIndividual ,
             :Clube ;
    :temAtleta :{idPessoa} ;
    :nome "{e['clube']}" .
        """
    idClube = clubes[e['clube']]

    if e['modalidade'] not in modalidades:
        modalidades[e['modalidade']] = "M" + str(len(modalidades))
        output += f"""
###  http://mirtilo/rpcw.di.uminho.pt/2025/EMD#{modalidades[e['modalidade']]}
:{modalidades[e['modalidade']]} rdf:type owl:NamedIndividual ,
             :Modalidade ;
    :temExame :E{i} ;
    :ePraticadaEm :{idClube} ;
    :ePraticadaPor :{idPessoa} ;
    :nome "{e['modalidade']}" .
        """
    else:
        output += f"""
        :{modalidades[e['modalidade']]} :temExame :E{i} ;
                                        :ePraticadaEm :{idClube} ;
                                        :ePraticadaPor :{idPessoa} .
        """
    

    output += f"""
###  http://mirtilo/rpcw.di.uminho.pt/2025/EMD#E{i}
:E{i} rdf:type owl:NamedIndividual ,
             :Exame ;
    :eRealizadoPor :{idPessoa} ;
    :dataEMD "{e['dataEMD']}" ;
    :resultado "{str(e['resultado']).lower()}"^^xsd:boolean .

"""
with open("emd_populado.ttl", "w", encoding="utf-8") as output_file:
    output_file.write(output)

print("Conteúdo guardado.")