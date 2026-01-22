---
Título: TPC1
Data: 10/02/2025
Autor: Maria Cunha
UC: RPCW
---
# Conversão de um ficheiro JSON para Protégé 

O objetivo deste trabalho é a conversão automática de um ficheiro em formato JSON para Protégé.

## Exemplo do Input (Ficheiro JSON)
Os dados do ficheiro JSON são apresentados da seguinte maneira:

```json
[
    {
      "_id": "60b3ee0ee00725274024d5a2",
      "index": 0,
      "dataEMD": "2020-07-27",
      "nome": {
        "primeiro": "Emily",
        "último": "Terrell"
      },
      "idade": 28,
      "género": "F",
      "morada": "Clay",
      "modalidade": "Futebol",
      "clube": "GDGoma",
      "email": "emily.terrell@gdgoma.org",
      "federado": false,
      "resultado": true
    },
]
```
## Classes Geradas
Assim, as classes geradas para adaptar estes dados foram:
- Atleta : contém a maioria das informações de um atleta;
- Exame : contém a informação sobre um exame médico (a data e o seu resultado);
- Clube : contém as informações sobre um clube.

## Relações Geradas
Gerou-se duas relações:
- pertenceAClube : esta relação associa um Atleta a um Clube;
- temExame : esta relação associa um Atleta a um Exame.

## Atributos Gerados
Para a Classe Atleta geraram-se os seguintes atributos: eFederado, temApelido, temClube, temEmail, temGenero, temId, temIdade, temIndex, temModalidade, temMorada, temNome, temPrimeiroNome.

Para a Classe Exame geraram-se os seguintes atributos: temDataEMD e temResultado.

Não foram gerados atributos para a Classe Clube.

## Exemplo do Output (Ficheiro TTL)
```turtle
###  a_60b3ee0ee00725274024d5a2
:a_60b3ee0ee00725274024d5a2 rdf:type owl:NamedIndividual ,
             :Atleta ;
             :temId "60b3ee0ee00725274024d5a2" ;
             :temIndex 0 ;
             :temPrimeiroNome "Emily" ;
             :temApelido "Terrell" ;
             :temNome "Emily Terrell" ;
             :temIdade 28 ;
             :temGenero "F" ;
             :temMorada "Clay" ;
             :temModalidade "Futebol" ;
             :temEmail "emily.terrell@gdgoma.org" ;
             :eFederado "false" ;
             :temExame :e_60b3ee0ee00725274024d5a2 ;
             :pertenceAClube :c_GDGoma .

###  e_60b3ee0ee00725274024d5a2
:e_60b3ee0ee00725274024d5a2 rdf:type owl:NamedIndividual ,
            :Exame ;
            :temDataEMD "2020-07-27" ;
            :temResultado "true" .

###  c_GDGoma
:c_GDGoma rdf:type owl:NamedIndividual ,
            :Clube ;
            :temClube "GDGoma" .
```