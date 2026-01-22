# Queries com SPARQL - GraphDB

A ontologia utilizada como foco de estudo contém todos os detalhes sobre a História de Portugal.

## Alínea A - Quantos triplos existem na Ontologia?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
SELECT (COUNT(*) AS ?totalTriplos) WHERE { 
    ?s ?p ?o 
}
```

### Output

```(txt)
4587 
```


## Alínea B - Que classes estão definidas?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
select ?class where {
    ?class a owl:Class .
}
```

### Output

```(txt)
:Rainha
:Monarca
_:node1

:Casa
:Individuo
:Regime
:Batalha
:Acontecimento
:Partido
:Dinastia
:Descobrimento
:Local
:Reinado
_:node12

:Conquista
:Mandato
:Rei
_:node16

:Presidente
_:node20
```

## Alínea C - Que propriedades tem a classe "Rei"?

```(sparql)
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
SELECT DISTINCT ?p WHERE { 
    ?s ?p ?o .
    ?s a :Rei .
}
```

### Output

```(txt)
rdf:type
:sexo
:casa
:enterro
:nascimento
:morte
:localNascimento
:nome
:temReinado
owl:topObjectProperty
:predecessor
:sucessor
:descendente
:notas
:cognomes
:posicao
:ascendente
```

## Alínea D - Quantos reis aparecem na ontologia?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>
SELECT DISTINCT (COUNT(?rei) AS ?totalReis) WHERE { 
    ?rei a :Rei .
}
```

### Output

```(txt)
32
```

## Alínea E - Calcula uma tabela com o seu nome, data de nascimento e cognome.

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?rei ?nome ?dataNascimento ?cognomes WHERE { 
    ?rei a :Rei .
    ?rei :nome ?nome . 
    ?rei :nascimento ?dataNascimento . 
    ?rei :cognomes ?cognomes .
}
```

### Output

| Rei      | Nome                          | Data de Nascimento      | Cognomes                                                                 |
|----------|-------------------------------|-------------------------|--------------------------------------------------------------------------|
| :rei14   | D. Manuel I                    | 31 de maio de 1469      | O Venturoso, O Bem-Aventurado, O Pomposo                                |
| :rei10   | D. João I                      | 11 de abril de 1357     | O da Boa Memória                                                         |
| :rei8    | D. Pedro I                     | 8 de abril de 1320      | O Justiceiro, O Cruel, O Cru, O Vingativo, O Tartamudo, O Até-ao-Fim-do-Mundo-Apaixonado |
| :rei7    | D. Afonso IV                   | 8 de fevereiro de 1291  | O Bravo                                                                  |
| :rei6    | D. Dinis I                     | 9 de outubro de 1261    | O Lavrador, O Rei-Trovador, O Rei-Poeta, O Rei-Agricultor               |
| :rei5    | D. Afonso III                  | 5 de maio de 1210       | O Bolonhês                                                               |
| :rei3    | D. Afonso II                   | 23 de abril de 1185     | O Gordo, O Crasso, O Gafo, O Legislador                                  |
| :rei2    | D. Sancho I                    | 11 de novembro de 1154  | O Povoador                                                              |
| :rei1    | D. Afonso I                    | 1109                    | O Conquistador, O Fundador, O Grande                                     |
| :rei4    | D. Sancho II                   | 8 de setembro de 1209   | O Capelo, O Piedoso, O Pio                                               |
| :rei9    | D. Fernando I                  | 31 de outubro de 1345   | O Formoso, O Belo, O Inconstante, O Inconsciente                         |
| :rei11   | D. Duarte I                    | 31 de outubro de 1391   | O Eloquente, O Rei-Filósofo                                              |
| :rei12   | D. Afonso V                    | 15 de janeiro de 1432   | O Africano                                                               |
| :rei13   | D. João II                     | 3 de março de 1455      | O Príncipe Perfeito, O Tirano                                           |
| :rei15   | D. João III                    | 7 de junho de 1502      | O Piedoso, O Pio                                                         |
| :rei16   | D. Sebastião I                 | 20 de janeiro de 1554   | O Desejado, O Encoberto, O Adormecido                                    |
| :rei17   | D. Henrique I                  | 31 de janeiro de 1512   | O Casto, O Cardeal-Rei, O Eborense/O de Évora                           |
| :rei18   | Filipe I                        | 21 de maio de 1527      | O Prudente                                                               |
| :rei19   | Filipe II                       | 14 de abril de 1578     | O Pio, O Piedoso                                                         |
| :rei20   | Filipe III                      | 8 de abril de 1605      | O Grande                                                                 |
| :rei21   | D. João IV                      | 19 de março de 1604     | O Restaurador, O Afortunado                                              |
| :rei22   | D. Afonso VI                    | 21 de agosto de 1643    | O Vitorioso, O Prisioneiro                                               |
| :rei23   | D. Pedro II                     | 26 de abril de 1648     | O Pacífico                                                               |
| :rei24   | D. João V                       | 22 de outubro de 1689   | O Magnânimo, O Magnífico, O Rei-Sol Português, O Freirático              |
| :rei25   | D. José I                       | 6 de junho de 1714      | O Reformador                                                             |
| :rei27   | D. João VI                      | 13 de maio de 1767      | O Clemente                                                               |
| :rei28   | D. Pedro IV                     | 12 de outubro de 1798   | O Rei-Soldado, O Rei-Imperador, O Libertador                             |
| :rei31   | D. Pedro V                      | 16 de setembro de 1837  | O Esperançoso, O Bem-Amado, O Muito Amado                                |
| :rei30   | D. Miguel I                     | 26 de outubro de 1802   | O Rei Absoluto, O Absolutista, O Tradicionalista, O Usurpador            |
| :rei32   | D. Luís I                       | 31 de outubro de 1838   | O Popular, O Bom, O Rei-Marinheiro                                       |
| :rei33   | D. Carlos I                     | 28 de setembro de 1863  | O Diplomata, O Martirizado, O Mártir, O Oceanógrafo, O Rei-Pintor       |
| :rei34   | D. Manuel II                    | 15 de novembro de 1889  | O Patriota, O Desventurado, O Estudioso, O Bibliófilo, O Rei-Saudade    |


## Alínea F - Acrescenta à tabela anterior a dinastia em que cada rei reinou.

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?rei ?nome ?dataNascimento ?cognomes ?dinastia ?name WHERE { 
    ?rei a :Rei .
    ?rei :nome ?nome . 
    ?rei :nascimento ?dataNascimento . 
    ?rei :cognomes ?cognomes .
    ?rei :temReinado ?reinado .  
    ?reinado :dinastia ?dinastia .
    ?dinastia :nome ?name
}
```

### Ouput 

| Rei        | Nome                        | Data de Nascimento    | Cognomes                                                       | Dinastia                                |
|------------|-----------------------------|-----------------------|----------------------------------------------------------------|-----------------------------------------|
| :rei14     | D. Manuel I                  | 31 de maio de 1469    | O Venturoso, O Bem-Aventurado, O Pomposo                      | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei10     | D. João I                    | 11 de abril de 1357   | O da Boa Memória                                               | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei8      | D. Pedro I                   | 8 de abril de 1320    | O Justiceiro, O Cruel, O Cru, O Vingativo, O Tartamudo, O Até-ao-Fim-do-Mundo-Apaixonado | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei7      | D. Afonso IV                 | 8 de fevereiro de 1291| O Bravo                                                        | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei6      | D. Dinis I                   | 9 de outubro de 1261  | O Lavrador, O Rei-Trovador, O Rei-Poeta, O Rei-Agricultor     | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei5      | D. Afonso III                | 5 de maio de 1210     | O Bolonhês                                                     | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei3      | D. Afonso II                 | 23 de abril de 1185   | O Gordo, O Crasso, O Gafo, O Legislador                        | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei2      | D. Sancho I                  | 11 de novembro de 1154| O Povoador                                                     | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei1      | D. Afonso I                  | 1109                  | O Conquistador, O Fundador, O Grande                           | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei4      | D. Sancho II                 | 8 de setembro de 1209 | O Capelo, O Piedoso, O Pio                                     | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei9      | D. Fernando I                | 31 de outubro de 1345 | O Formoso, O Belo, O Inconstante, O Inconsciente               | :dinastia1 - Dinastia de Borgonha / Dinastia Afonsina |
| :rei11     | D. Duarte I                   | 31 de outubro de 1391 | O Eloquente, O Rei-Filósofo                                    | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei12     | D. Afonso V                   | 15 de janeiro de 1432 | O Africano                                                     | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei13     | D. João II                   | 3 de março de 1455    | O Príncipe Perfeito, O Tirano                                  | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei15     | D. João III                  | 7 de junho de 1502    | O Piedoso, O Pio                                               | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei16     | D. Sebastião I               | 20 de janeiro de 1554 | O Desejado, O Encoberto, O Adormecido                         | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei17     | D. Henrique I                | 31 de janeiro de 1512 | O Casto, O Cardeal-Rei, O Eborense/O de Évora                 | :dinastia2 - Dinastia de Avis / Dinastia Joanina    |
| :rei18     | Filipe I                      | 21 de maio de 1527    | O Prudente                                                     | :dinastia3 - Dinastia Filipina                      |
| :rei19     | Filipe II                     | 14 de abril de 1578   | O Pio, O Piedoso                                               | :dinastia3 - Dinastia Filipina                      |
| :rei20     | Filipe III                    | 8 de abril de 1605    | O Grande                                                       | :dinastia3 - Dinastia Filipina                      |
| :rei21     | D. João IV                    | 19 de março de 1604   | O Restaurador, O Afortunado                                    | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei22     | D. Afonso VI                  | 21 de agosto de 1643  | O Vitorioso, O Prisioneiro                                     | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei23     | D. Pedro II                   | 26 de abril de 1648   | O Pacífico                                                     | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei24     | D. João V                     | 22 de outubro de 1689 | O Magnânimo, O Magnífico, O Rei-Sol Português, O Freirático   | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei25     | D. José I                     | 6 de junho de 1714    | O Reformador                                                   | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei27     | D. João VI                    | 13 de maio de 1767    | O Clemente                                                     | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei28     | D. Pedro IV                   | 12 de outubro de 1798 | O Rei-Soldado, O Rei-Imperador, O Libertador                   | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei31     | D. Pedro V                    | 16 de setembro de 1837| O Esperançoso, O Bem-Amado, O Muito Amado                     | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei30     | D. Miguel I                   | 26 de outubro de 1802 | O Rei Absoluto, O Absolutista, O Tradicionalista, O Usurpador | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei32     | D. Luís I                     | 31 de outubro de 1838 | O Popular, O Bom, O Rei-Marinheiro                            | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei33     | D. Carlos I                   | 28 de setembro de 1863| O Diplomata, O Martirizado, O Mártir, O Oceanógrafo, O Rei-Pintor | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |
| :rei34     | D. Manuel II                  | 15 de novembro de 1889| O Patriota, O Desventurado, O Estudioso, O Bibliófilo, O Rei-Saudade | :dinastia4 - Dinastia de Bragança / Dinastia Brigantina |


## Alínea G - Qual a distribuição dos reis pelas 4 dinastias?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?dinastia (COUNT(distinct ?rei) AS ?numeroDeReis) WHERE {
  ?rei a :Rei .
  ?rei :temReinado ?reinado .
# ?rei :temReinado/:dinastia/:nome ?dinastia
  ?reinado :dinastia ?dinastia .
} 
GROUP BY ?dinastia
# ORDER BY ?dinastia
```

### Output

| Dinastia  | Número de Reis         |
|-----------|------------------------|
| :dinastia2| 8                      |
| :dinastia1| 9                      |
| :dinastia3| 3                      |
| :dinastia4| 12                     |


## Alínea H - Lista os descobrimentos (sua descrição) por ordem cronológica.

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?descobrimento ?data ?descricao WHERE {
  ?descobrimento a :Descobrimento .
  ?descobrimento :data ?data .
  ?descobrimento :notas ?descricao .
} 
ORDER BY ?data
```

### Output

| Descobrimento      | Data      | Descrição                                              |
|--------------------|-----------|--------------------------------------------------------|
| :descobrimento1              | 1336      | Provável primeira expedição às ilhas Canárias, a que se seguiram expedições adicionais em 1340 e 1341, embora tal seja disputado. |
| :descobrimento2              | 1415      | Tropas portuguesas sob o comando de João I de Portugal conquistam Ceuta. Este acontecimento é geralmente referido como o início da expansão ou descobrimentos Portugueses |
| :descobrimento3              | 1419      | João Gonçalves Zarco e Tristão Vaz Teixeira descobrem a Ilha de Porto Santo, na Madeira. |
| :descobrimento4              | 1420      | Os mesmos navegadores, com Bartolomeu Perestrelo, descobrem a Ilha da Madeira, que foi de imediato colonizada. |
| :descobrimento82             | 1422      | Após sucessivas viagens, o cabo Não, considerado o limite navegável a sul por árabes e europeus, é ultrapassado, alcançando-se o Bojador. |
| :descobrimento6              | 1427      | Diogo de Silves descobre (ou redescobre) as ilhas açorianas ocidentais e centrais, que seriam colonizadas em 1431 por Gonçalo Velho Cabral. |
| :descobrimento7              | 1434      | Gil Eanes dobra o Cabo Bojador, dissipando o terror que este promontório inspirava. |
| :descobrimento8              | 1435-1436 | Gil Eanes e Afonso Gonçalves Baldaia descobrem Angra de Ruivos e este último chegou ao Rio de Ouro, no Saara Ocidental. |
| :descobrimento9              | 1441      | Nuno Tristão chega ao Cabo Branco com Gonçalo Afonso. |
| :descobrimento11             | 1443      | O regente D. Pedro decreta o monopólio da navegação na costa oeste Africana, reconhecido pela bula Rex regum, que atribui ao irmão Infante D. Henrique “o Navegador”. |
| :descobrimento10             | 1443      | Nuno Tristão entrou no golfo de Arguim. |
| :descobrimento12             | 1444      | Dinis Dias descobriu o cabo Cabo Verde (Dakar) e a Ilha de Palma (Goreia). |
| :descobrimento13             | 1445      | Álvaro Fernandes passou além do Cabo Verde (cabo), Goreia e chegou ao “Cabo dos Mastros” (Cabo Roxo? provavelmente entre o Cabo Verde e o rio Gâmbia). |
| :descobrimento14             | 1446      | Álvaro Fernandes chegou ao norte da Guiné-Bissau. |
| :descobrimento15             | 1452      | Diogo de Teive descobriu as ilhas Flores e Corvo nos Açores. |
| :descobrimento16             | 1455      | Bula Romanus Pontifex do Papa Nicolau V confirma as explorações portuguesas e declara que todas as terras e mares a sul do Cabo Bojador e do Cabo Não são pertença dos reis de Portugal. |
| :descobrimento17             | 1455      | Alvise Cadamosto explora do Rio Gâmbia ao Rio Geba (Guiné-Bissau) e o arquipélago dos Bijagós. Na foz do Gâmbia, ao notar o quase desaparecimento da estrela polar no horizonte, esboçou a primeira representação da constelação Cruzeiro do Sul. |
| :descobrimento18             | 1456 – 1460 | Viagens de Alvise Cadamosto, Diogo Gomes e António da Noli descobriram as primeiras ilhas do arquipélago de Cabo Verde (Boavista, Santiago, Maio e Sal). |
| :descobrimento0              | 1459      | Afonso V encomenda o mapa-múndi que reúne o conhecimento geográfico da época aos venezianos Fra Mauro e Andrea Bianco. |
| :descobrimento19             | 1460      | Morte do Infante D. Henrique. A sua exploração sistemática do Atlântico alcançou em vida os 8º N na costa africana, com a chegada de Pêro de Sintra à que chama Serra Leoa, e os 40º O no oceano Atlântico (Mar dos Sargaços). |
| :descobrimento20             | 1461      | Diogo Gomes e Antonio da Noli descobrem mais ilhas de Cabo Verde. |
| :descobrimento21             | 1462      | Diogo Afonso descobre as cinco ilhas mais ocidentais do arquipélago de Cabo Verde (Brava, São Nicolau, São Vicente, Santo Antão e os ilhéus Branco e Raso). |
| :descobrimento22             | 1469      | Contrato de Fernão Gomes para a exploração da costa da Guiné a sul da Serra Leoa e comércio de Pimenta-da-Guiné que duraria até 1475. |
| :descobrimento23             | 1471      | João de Santarém e Pêro Escobar encontram a costa da Mina, chegam região do Cabo Três Pontos (Gana) e ao delta do Níger. |
| :descobrimento24             | 1472      | Rui de Sequeira chega a Benim nomeando a área Lagoa de Lagos (actual Lagos (Nigéria)). |
| :descobrimento25             | 1472-1473 | Fernão Pó explora o rio Wouri que nomeia “dos Camarões“ e as ilhas Formosa (Bioko) e Ano Bom. |
| :descobrimento26             | 1472-1474 | João Vaz Corte-Real e Álvaro Martins Homem poderão ter atingido a Terra Nova. Descoberta não comprovada |
| :descobrimento27             | 1473      | Lopo Gonçalves descobre o Cabo Lopez , na boca do rio Ogooué e é creditado como o primeiro a cruzar a linha do equador e chegar ao Hemisfério Sul |
| :descobrimento29             | 1474      | Afonso V consulta o geógrafo Toscanelli, que aconselha a navegação para oeste, para chegar à Índia |
| :descobrimento28             | 1475      | Antes do contrato de Fernão Gomes terminar, Rui de Sequeira e Lopo Gonçalves chegam ao cabo de St. Catarina, 2º latitude Sul |
| :descobrimento30             | 1479      | O Tratado das Alcáçovas estabelece o controlo português dos Açores, Guiné, Elmina, Madeira e Cabo Verde e o controlo castelhano das ilhas Canárias |
| :descobrimento31             | 1482 – 1484 | Diogo Cão chegou ao estuário do Rio Congo (Congo) onde deixou um Padrão de pedra, substituindo as habituais cruzes de madeira; subiu 150Km a montante do rio até às cataratas de Ielala. |
| :descobrimento32             | 1484-1486 | Afonso de Paiva, Duarte Pacheco Pereira e José Vizinho contactam o Reino de Benim onde a presença de pimenta indicia a proximidade da Índia. |
| :descobrimento33             | 1485-1486 | Segunda viagem de Diogo Cão leva Martin Behaim como cosmógrafo, passa o Cabo Padrão (Cape Cross) e terá chegado a Walvis Bay, na Namíbia. |
| :descobrimento35             | 1487      | Afonso de Paiva e Pêro da Covilhã partiram de Lisboa, viajando por terra em busca do reino do Preste João, na Etiópia. |
| :descobrimento34             | 1487      | Gonçalo Eanes e Pêro de Évora sobem o rio Senegal numa expedição ao interior africano até Tucurol e Timbuctu. |
| :descobrimento36             | 1487-1488 | Bartolomeu Dias dobra o Cabo das Tormentas, futuro Cabo da Boa Esperança, coroando 50 anos de esforço e numerosas expedições, entrando pela primeira vez no Oceano Índico. Afonso de Paiva e Pêro da Covilhã chegam a Adém. |
| :descobrimento37             | 1489 – 1492 | Foram feitas várias expedições ao Atlântico Sul para mapear os ventos. |
| :descobrimento38             | 1494      | Assinado o Tratado de Tordesilhas “dividindo” o mundo por descobrir entre Portugal e o recém-formado Reino da Espanha, na sequência da contestação de D. João II às pretensões espanholas após a viagem de Cristovão Colombo. |
| :descobrimento39             | 1495 ou 1500 | Viagem de João Fernandes Lavrador e Pêro de Barcelos à Gronelândia (Terra do Bacalhau). Nesta viagem avistaram a terra que nomearam Labrador (lavrador). |
| :descobrimento40             | 1497 – 1499 | Vasco da Gama comandou a primeira frota a contornar África e chegar a Calecute na Índia, e regressou. |
| :descobrimento41             | 1498      | Duarte Pacheco Pereira terá explorado o Atlântico Sul (e terá alcançado a foz do rio Amazonas e a ilha do Marajó no que terá sido uma expedição secreta - não existem provas) |
| :descobrimento44             | 1500      | A 10 de Agosto Diogo Dias, navegador da frota de Pedro Álvares Cabral na segunda armada à Índia, descobriu uma ilha a que deu o nome de São Lourenço, mais tarde designada Madagáscar. |
| :descobrimento43             | 1500 – 1501 | Gaspar Corte Real e Miguel Corte Real atingiram a Terra Nova, que nomeou Terras de Corte Real (Canadá). |
| :descobrimento42             | 1500-1501 | A segunda frota para a Índia comandada por Pedro Álvares Cabral atinge o Brasil, aportando em Porto Seguro. |
| :descobrimento45             | 1502      | Vasco da Gama vindo da Índia avistou as então chamadas Ilhas do Almirante (Seychelles). |
| :descobrimento46             | 1502      | Miguel Corte-Real partiu para a Nova Inglaterra em busca do seu irmão Gaspar. João da Nova descobriu a Ilha de Ascensão. Fernão de Noronha descobriu as ilhas que mantêm o seu nome, Fernando Noronha, em Pernambuco. |
| :descobrimento47             | 1503      | De regresso do Oriente Estêvão da Gama descobriu a Ilha de Santa Helena. |
| :descobrimento48             | 1503      | A caminho da Índia António de Saldanha foi o primeiro europeu a ancorar na Baía de Saldanha e a ascender à Montanha da Mesa, que nomeou, na atual África do Sul. |
| :descobrimento49             | 1505      | Gonçalo Álvares, da frota do primeiro vice-rei, ruma a sul, onde “a água e até o vinho gelavam” descobrindo a Ilha de Gonçalo Álvares (Gough Island) |
| :descobrimento5              | 1505-1506 | Lourenço de Almeida é enviado às Maldivas e chegou ao que chamou Ceilão (Sri Lanka), a “Taprobana” dos registos clássicos gregos. |
| :descobrimento51             | 1506      | Tristão da Cunha descobriu a Ilha de Tristão da Cunha no Atlântico Sul. Navegadores portugueses aportaram em Madagáscar. |
| :descobrimento52             | 1507-1512 | Os portugueses foram os primeiros europeus a aportar nas Ilhas Mascarenhas, no Índico, nomeadas em homenagem a Pedro Mascarenhas |
| :descobrimento53             | 1509      | Diogo Lopes de Sequeira atravessou o golfo de Bengala e chegou a Sumatra e Malaca (Malásia), com ele viajava Fernão de Magalhães, que viria a fazer a primeira viagem de circum-navegação ao globo, ao serviço de Espanha em 1519-22. |
| :descobrimento54             | 1511      | Duarte Fernandes foi o primeiro europeu a chegar ao Reino do Sião (Tailândia), enviado por Afonso de Albuquerque durante a conquista de Malaca. |
| :descobrimento56             | 1511      | António de Abreu, Francisco Serrão e Simão Afonso Bisagudo são enviados por Albuquerque em busca das “ilhas das especiarias”. Serrão naufraga em Ternate nas Molucas (Indonésia). |
| :descobrimento55             | 1511      | Rui Nunes da Cunha foi o primeiro europeu a chegar a Pegu (Birmânia, Myanmar), enviado por Albuquerque. |
| :descobrimento57             | 1512      | António de Abreu chegou à ilha de Timor e às ilhas Banda, Ambão e Seram. |
| :descobrimento58             | 1512      | Pedro Mascarenhas descobriu a ilha Diego Garcia. Chegou também à ilha Maurícia, que poderia já ser conhecida em expedições anteriores de Diogo Dias e Afonso de Albuquerque. |
| :descobrimento59             | 1512-1514 | João de Lisboa e Estevão Fróis exploram o estuário do Rio da Prata e provavelmente o Golfo de San Matias a 42° Sul na Argentina22 Cristóvão de Haro, financiador da expedição, testemunha a viagem e as notícias que Lisboa e Fróis recolheram do Rei Branco(o imperador Inca), assim como o machado de prata obtido junto dos nativos e que ofereceram ao rei D. Manuel I. |
| :descobrimento61             | 1513      | Jorge Álvares partindo de Malaca é o primeiro europeu a aportar no sul da China, na Ilha de Lintin, no estuário do Rio das Pérolas. |
| :descobrimento60             | 1513      | Afonso de Albuquerque atravessa o estreito de Bab-el-Mandeb no Mar Vermelho liderando a primeira frota europeia a navegar nessas águas. |
| :descobrimento62             | 1516      | Portugueses aportam em Da Nang, no Reino de Champa que nomeiam Cochinchina, (actual Vietname). |
| :descobrimento63             | 1516-1517 | Rafael Perestrelo comandou o primeiro navio comercial a aportar na China continental, em Cantão. Fernão Pires de Andrade e Tomé Pires foram enviados de D. Manuel I para estabelecer relações oficiais entre o Império Português e a Dinastia Ming, no reinado do imperador Zhengde. |
| :descobrimento64             | 1520      | Francisco Álvares e uma embaixada portuguesa chegam à Etiópia onde encontram Pêro da Covilhã |
| :descobrimento81             | 1520      | (Diogo Pacheco terá visitado Kimberley, na Austrália em busca da “Ilha do Ouro” a sul e a sudeste de Sumatra, Java e Sunda, informado por Malaios e Sumatreses, presentes na sua tripulação.- descoberta não reconhecida oficialmente) |
| :descobrimento65             | 1522      | (Cristovão de Mendonça terá descoberto a Austrália e Nova Zelândia, partindo de Belém em 1521.[carece de fontes]- descoberta não reconhecida oficialmente) |
| :descobrimento67             | 1525      | Aleixo Garcia explora o Rio da Prata ao serviço de Espanha, como membro da expedição de Juan Díaz de Solís e, mais tarde, liderando uma expedição privada de Europeus e indios Guarani, explora o Paraguai e a Bolívia. Aleixo Garcia foi o primeiro europeu a atravessar o Chaco e a conseguir penetrar nas defesas exteriores do Império Inca, já nas colinas dos Andes e na região de Sucre, na atual Bolívia. Aleixo Garcia foi o primeiro europeu a fazê-lo, realizando a façanha oito anos antes de Francisco Pizarro. |
| :descobrimento66             | 1525      | Gomes de Sequeira e Diogo da Rocha foram enviados pelo governador Jorge de Meneses, à descoberta de territórios a norte das Molucas, foram os primeiros europeus a chegar às Ilhas Carolinas, a nordeste da Nova Guiné, que então nomearam “Ilhas de Sequeira”. (A Austrália teria sido descoberta por Cristóvão de Mendonça e Gomes de Sequeira em 1525 – descoberta não reconhecida oficialmente) |
| :descobrimento68             | 1526      | Jorge de Meneses aportou na ilha Waigeo na Papua Ocidental (Nova Guiné) |
| :descobrimento69             | 1528      | Diogo Rodrigues explorou o arquipélago das Mascarenhas, que nomeou em homenagem ao seu companheiro Pedro de Mascarenhas, que compreende a Reunião, Maurícia e Rodrigues. |
| :descobrimento70             | 1529      | É assinado o Tratado de Saragoça, que estabelece o antimeridiano de Tordesilhas, limite leste das explorações portuguesas e espanholas, para solucionar a chamada “Questão das Molucas“. |
| :descobrimento71             | 1538      | João Fogaça chega à Papua-Nova Guiné enviado por António Galvão. |
| :descobrimento72             | 1542-1543 | Fernão Mendes Pinto, Diogo Zeimoto e Cristovão Borralho chegaram ao Japão. |
| :descobrimento73             | 1586      | António da Madalena um frade Capuchinho foi um dos primeiros visitantes ocidentais a chegar a Angkor (actual Camboja) |
| :descobrimento74             | 1602 – 1606 | Bento de Góis, missionário jesuíta, foi o primeiro europeu a percorrer o caminho terrestre da Índia para a China, através da Ásia Central. |
| :descobrimento75             | 1606      | Pedro Fernandes de Queirós alcançou as Novas Hébridas e aportou numa grande ilha que pensou ser parte do procurado continente a sul, a que chamou Ilha do Espírito Santo, actual Vanuatu. |
| :descobrimento76             | 1606      | Luís Vaz de Torres (nacionalidade incerta) descobriu o que é hoje chamado Estreito de Torres. |
| :descobrimento77             | 1624      | António de Andrade e Manuel Marques, missionários jesuítas, viajaram de Agra (Índia) a Chaparangue, sendo os primeiros europeus documentados a chegar ao Tibete |
| :descobrimento78             | 1626      | Estêvão Cacella, missionário jesuíta, viajou através dos Himalaias e foi o primeiro europeu a entrar no Butão34 |
| :descobrimento79             | 1636 – 1638 | Pedro Teixeira partiu de Belém do Pará subindo o rio Amazonas e alcançou Quito, no Equador, numa expedição de mais de mil homens. |
| :descobrimento80             | 1660 – 1662 | (David Melgueiro partindo de Tanegashima poderá ter realizado a primeira travessia da passagem do Nordeste. 35 Viagem pouco documentada, ao serviço da marinha holandesa) |


## Alínea I - Lista as várias conquistas, nome e data, juntamente com o nome que reinava no momento.

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?c ?data ?nome ?reinado ?monarca ?name WHERE {
  ?c a :Conquista ;
     :data ?data ;
     :nome ?nome ;
     :temReinado ?reinado .
  ?reinado :temMonarca ?monarca .
  ?monarca :nome ?name .
} 
```
### Output

| Conquista      | Data  | Nome                        | Monarca  | Nome       |
|---------------|-------|----------------------------------|--------|------------|
| :conquista11  | 1159  | Conquista de Beja                | :rei1  | D. Afonso I |
| :conquista9   | 1159  | Conquista do Castelo de Cera     | :rei1  | D. Afonso I |
| :conquista12  | 1162  | Reconquista de Beja              | :rei1  | D. Afonso I |
| :conquista8   | 1158  | Conquista                        | :rei1  | D. Afonso I |
| :conquista13  | 1165  | Conquista de Évora               | :rei1  | D. Afonso I |
| :conquista14  | 1166  | Tomada de Serpa                  | :rei1  | D. Afonso I |
| :conquista20  | 1340  | Batalha do Salado                | :rei7  | D. Afonso IV |
| :conquista5   | 1147  | Batalha de Sacavém               | :rei1  | D. Afonso I |
| :conquista4   | 1147  | Conquista de Lisboa              | :rei1  | D. Afonso I |
| :conquista3   | 1147  | Tomada do Castelo                | :rei1  | D. Afonso I |
| :conquista7   | 1147  | Tomada do Castelo                | :rei1  | D. Afonso I |
| :conquista10  | 1159  | Conquista de Évoramonte          | :rei1  | D. Afonso I |
| :conquista6   | 1147  | Tomada do Castelo                | :rei1  | D. Afonso I |
| :conquista19  | 1212  | Batalha Navas de Tolosa          | :rei3  | D. Afonso II |
| :conquista15  | 1166  | Tomada de Moura                  | :rei1  | D. Afonso I |
| :conquista16  | 1169  | Batalha de Badajoz               | :rei1  | D. Afonso I |
| :conquista17  | 1189  | Conquista de Alvor               | :rei2  | D. Sancho I |
| :conquista18  | 1189  | Cerco de Silves                  | :rei2  | D. Sancho I |
| :conquista1   | 1135  | Fundação do Castelo              | :rei1  | D. Afonso I |
| :conquista2   | 1139  | Batalha de Ourique               | :rei1  | D. Afonso I |


## Alínea J - Calcula uma tabela com o nome, data de nascimento e número de mandatos de todos os presidentes portugueses.

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?presidente ?nome ?dataDeNascimento (COUNT(?mandatos) AS ?numMandatos) WHERE {
  ?presidente a :Presidente ;
                :nome ?nome ;  
                :nascimento ?dataDeNascimento ;  
                :mandato ?mandatos .
}  
GROUP BY ?presidente ?nome ?dataDeNascimento
```

### Output

| Presidente             | Nome                                          | Data de Nascimento        | Número de Mandatos |
|---------------|-------------------------------------------------|---------------------------|--------------------|
| :presidente8  | José Mendes Cabeçadas Júnior                   | 19 de setembro de 1883    | 1 |
| :presidente10 | António Óscar de Fragoso Carmona               | 24 de novembro de 1869    | 4 |
| :presidente11 | António de Oliveira Salazar                    | 28 de abril de 1889      | 1 |
| :presidente12 | Francisco Higino Craveiro Lopes                | 12 de abril de 1894      | 1 |
| :presidente13 | Américo Deus Rodrigues Thomaz                 | 19 de novembro de 1894   | 1 |
| :presidente14 | António Sebastião Ribeiro de Spínola          | 11 de abril de 1910      | 2 |
| :presidente4  | Sidónio Bernardino Cardoso da Silva Pais      | 1 de maio de 1872        | 2 |
| :presidente5  | João do Canto e Castro Silva Antunes Júnior   | 19 de maio de 1862       | 2 |
| :presidente7  | Manuel Teixeira Gomes                         | 27 de maio de 1860       | 1 |
| :presidente3  | Bernardino Luís Machado Guimarães            | 28 de março de 1851      | 2 |
| :presidente1  | Joaquim Teófilo Fernandes Braga              | 24 de fevereiro de 1843  | 2 |
| :presidente2  | Manuel José de Arriaga Brum da Silveira e Peyrelongue | 8 de julho de 1840  | 1 |
| :presidente16 | Mário Alberto Nobre Lopes Soares             | 7 de dezembro de 1924    | 1 |
| :presidente6  | António José de Almeida                      | 17 de julho de 1866      | 1 |
| :presidente17 | Aníbal António Cavaco Silva                  | 15 de julho de 1939      | 1 |
| :presidente9  | Manuel de Oliveira Gomes da Costa            | 14 de janeiro de 1863    | 2 |
| :presidente15 | Francisco da Costa Gomes                     | 30 de junho de 1914      | 1 |


## Alínea K - Quantos mandatos teve o presidente Sidónio Pais? Em que datas iniciaram e terminaram estes mandatos?

Dois mandatos, como poderíamos verificar com a alínea anterior. 

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?presidente ?m ?c ?f WHERE {
  ?presidente a :Presidente .
  ?presidente :nome "Sidónio Bernardino Cardoso da Silva Pais" . 
  ?presidente :mandato ?m .
  ?m :comeco ?c .
  ?m :fim ?f .
}  
```

### Output

| Presidente    | Mandato   | Início                | Fim                   |
|--------------|----------|----------------------|----------------------|
| :presidente4 | :mandato5 | 12 de dezembro de 1917 | 27 de dezembro de 1917 |
| :presidente4 | :mandato6 | 27 de dezembro de 1917 | 14 de dezembro de 1918 |


## Alínea L - Quais os nomes dos partidos políticos presentes na ontologia?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?partido ?nome WHERE {
  ?partido a :Partido .
  ?partido :nome ?nome
} 
```

### Output

| Partido      | Nome do Partido       |
|--------------|-----------------------|
| :partido4    | Independente          |
| :partido9    | União Nacional        |
| :partido5    | Nacional Republicano  |
| :partido3    | Democrático           |
| :partido1    | Republicano           |
| :partido10   | Socialista            |
| :partido6    | Evolucionista         |
| :partido7    | Liberal               |
| :partido8    | Nacionalista          |
| :partido11   | Social Democrata      |


## Alínea M - Qual a distribuição dos militantes por cada partido político?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?partido ?nome ?name ?militante WHERE {
  ?partido a :Partido .
  ?partido :nome ?nome .
  ?partido :temMilitante ?militante .
  ?militante :nome ?name .
}  
GROUP BY ?partido ?nome ?name ?militante

versão alternativa:

SELECT ?n (COUNT(?militante) AS ?nmilitante) WHERE {
  ?p a :Partido;
  :nome ?n;
  :temMilitante ?militante;
}
group by ?p ?n
```

### Output
 
| Partido ID  | Nome do Partido      | Presidente Nome                             | Presidente ID  |
|-------------|----------------------|---------------------------------------------|----------------|
| :partido4   | Independente          | José Mendes Cabeçadas Júnior               | :presidente8   |
| :partido4   | Independente          | António Óscar de Fragoso Carmona           | :presidente10  |
| :partido4   | Independente          | António Sebastião Ribeiro de Spínola       | :presidente14  |
| :partido4   | Independente          | Sidónio Bernardino Cardoso da Silva Pais   | :presidente4   |
| :partido9   | União Nacional        | António Óscar de Fragoso Carmona           | :presidente10  |
| :partido9   | União Nacional        | António de Oliveira Salazar                | :presidente11  |
| :partido9   | União Nacional        | Francisco Higino Craveiro Lopes            | :presidente12  |
| :partido9   | União Nacional        | Américo Deus Rodrigues Thomaz              | :presidente13  |
| :partido5   | Nacional Republicano  | Sidónio Bernardino Cardoso da Silva Pais   | :presidente13  |
| :partido5   | Nacional Republicano  | João do Canto e Castro Silva Antunes Júnior| :presidente4   |
| :partido3   | Democrático           | Manuel Teixeira Gomes                      | :presidente7   |
| :partido3   | Democrático           | Bernardino Luís Machado Guimarães          | :presidente3   |
| :partido1   | Republicano           | Joaquim Teófilo Fernandes Braga            | :presidente1   |
| :partido1   | Republicano           | Manuel José de Arriaga Brum da Silveira e Peyrelongue | :presidente2   |
| :partido10  | Socialista            | Mário Alberto Nobre Lopes Soares           | :presidente2   |
| :partido6   | Evolucionista         | António José de Almeida                    | :presidente6   |
| :partido7   | Liberal               | António José de Almeida                    | :presidente6   |
| :partido8   | Nacionalista          | António José de Almeida                    | :presidente6   |
| :partido11  | Social Democrata      | Aníbal António Cavaco Silva                | :presidente17  |


## Alínea N - Qual o partido com o maior número de presidentes?

```(sparql)
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix : <http://www.semanticweb.org/andre/ontologies/2015/6/historia#>

SELECT ?partido ?n (COUNT(?presidente) AS ?numPresidentes) WHERE {
  ?presidente :partido ?partido .
  ?partido :nome ?n .
} 
GROUP BY ?partido ?n
ORDER BY DESC(?numPresidentes)
LIMIT 1
```

### Output 

| Partido    | Nome       | Número de Presidentes |
|------------|------------|-----------------------|
| :partido4  | Independente | 4                     |
