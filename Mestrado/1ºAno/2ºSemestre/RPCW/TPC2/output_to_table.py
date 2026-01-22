import re

def descobrimento(input_string):
    # Expressão regular para capturar os dados, agora com tratamento para intervalos de datas
    pattern = r":descobrimento(\d+)\s*\"([^\"]+)\"\s*\"(.*?)\""
    
    # Encontrar todas as correspondências
    matches = re.findall(pattern, descobrimentos, re.DOTALL)
    
    # Criar a tabela em markdown
    markdown_table = "| Descobrimento      | Data      | Descrição                                              |\n"
    markdown_table += "|--------------------|-----------|--------------------------------------------------------|\n"
    
    # Iterar sobre as correspondências e adicionar à tabela
    for match in matches:
        descobrimento, data, descricao = match
        markdown_table += f"| :descobrimento{descobrimento:<14} | {data:<9} | {descricao} |\n"
    
    return markdown_table


def conquista(input_string):
    # Expressão regular para capturar conquistas e reis
    pattern = r"(:conquista\d+)\s*\"([^\"]+)\"\s*\"(.*?)\"\s*:reinado\d+\s*(:rei\d+)\s*\"(.*?)\""

    # Encontrar todas as correspondências
    matches = re.findall(pattern, conquistas, re.DOTALL)

    # Criar a tabela em Markdown
    markdown_table = "| Conquista      | Data  | Nome                        | Monarca  | Nome       |\n"
    markdown_table += "|---------------|-------|----------------------------------|--------|------------|\n"

    # Iterar sobre as correspondências e adicionar à tabela
    for conquista_id, data, descricao, rei_id, rei_nome in matches:
        markdown_table += f"| {conquista_id:<13} | {data:<5} | {descricao:<32} | {rei_id:<6} | {rei_nome:<10} |\n"

    return markdown_table

# Exemplo de string de entrada
conquistas = """
:conquista11
"1159"
"Conquista de Beja"
:reinado1
:rei1
"D. Afonso I"

:conquista9
"1159"
"Conquista do Castelo de Cera"
:reinado1
:rei1
"D. Afonso I"

:conquista12
"1162"
"Reconquista de Beja"
:reinado1
:rei1
"D. Afonso I"

:conquista8
"1158"
"Conquista"
:reinado1
:rei1
"D. Afonso I"

:conquista13
"1165"
"Conquista de Évora"
:reinado1
:rei1
"D. Afonso I"

:conquista14
"1166"
"Tomada de Serpa"
:reinado1
:rei1
"D. Afonso I"

:conquista20
"1340"
"Batalha do Salado"
:reinado7
:rei7
"D. Afonso IV"

:conquista5
"1147"
"Batalha de Sacavém"
:reinado1
:rei1
"D. Afonso I"

:conquista4
"1147"
"Conquista de Lisboa"
:reinado1
:rei1
"D. Afonso I"

:conquista3
"1147"
"Tomada do Castelo"
:reinado1
:rei1
"D. Afonso I"

:conquista7
"1147"
"Tomada do Castelo"
:reinado1
:rei1
"D. Afonso I"

:conquista10
"1159"
"Conquista de Évoramonte"
:reinado1
:rei1
"D. Afonso I"

:conquista6
"1147"
"Tomada do Castelo"
:reinado1
:rei1
"D. Afonso I"

:conquista19
"1212"
"Batalha Navas de Tolosa"
:reinado3
:rei3
"D. Afonso II"

:conquista15
"1166"
"Tomada de Moura"
:reinado1
:rei1
"D. Afonso I"

:conquista16
"1169"
"Batalha de Badajoz"
:reinado1
:rei1
"D. Afonso I"

:conquista17
"1189"
"Conquista de Alvor"
:reinado2
:rei2
"D. Sancho I"

:conquista18
"1189"
"Cerco de Silves"
:reinado2
:rei2
"D. Sancho I"

:conquista1
"1135"
"Fundação do Castelo"
:reinado1
:rei1
"D. Afonso I"

:conquista2
"1139"
"Batalha de Ourique"
:reinado1
:rei1
"D. Afonso I"
"""


descobrimentos = """
:descobrimento1
"1336"

"Provável primeira expedição às ilhas Canárias, a que se seguiram expedições adicionais em 1340 e 1341, embora tal seja disputado."

:descobrimento2
"1415"

"Tropas portuguesas sob o comando de João I de Portugal conquistam Ceuta. Este acontecimento é geralmente referido como o início da expansão ou descobrimentos Portugueses"

:descobrimento3
"1419"

"João Gonçalves Zarco e Tristão Vaz Teixeira descobrem a Ilha de Porto Santo, na Madeira."

:descobrimento4
"1420"

"Os mesmos navegadores, com Bartolomeu Perestrelo, descobrem a Ilha da Madeira, que foi de imediato colonizada."

:descobrimento82
"1422"

"Após sucessivas viagens, o cabo Não, considerado o limite navegável a sul por árabes e europeus, é ultrapassado, alcançando-se o Bojador."

:descobrimento6
"1427"

"Diogo de Silves descobre (ou redescobre) as ilhas açorianas ocidentais e centrais, que seriam colonizadas em 1431 por Gonçalo Velho Cabral."

:descobrimento7
"1434"

"Gil Eanes dobra o Cabo Bojador, dissipando o terror que este promontório inspirava."

:descobrimento8
"1435-1436"

"Gil Eanes e Afonso Gonçalves Baldaia descobrem Angra de Ruivos e este último chegou ao Rio de Ouro, no Saara Ocidental."

:descobrimento9
"1441"

"Nuno Tristão chega ao Cabo Branco com Gonçalo Afonso."

:descobrimento11
"1443"

"O regente D. Pedro decreta o monopólio da navegação na costa oeste Africana, reconhecido pela bula Rex regum, que atribui ao irmão Infante D. Henrique “o Navegador”."

:descobrimento10
"1443"

"Nuno Tristão entrou no golfo de Arguim."

:descobrimento12
"1444"

"Dinis Dias descobriu o cabo Cabo Verde (Dakar) e a Ilha de Palma (Goreia)."

:descobrimento13
"1445"

"Álvaro Fernandes passou além do Cabo Verde (cabo), Goreia e chegou ao “Cabo dos Mastros” (Cabo Roxo? provavelmente entre o Cabo Verde e o rio Gâmbia)."

:descobrimento14
"1446"

"Álvaro Fernandes chegou ao norte da Guiné-Bissau."

:descobrimento15
"1452"

"Diogo de Teive descobriu as ilhas Flores e Corvo nos Açores."

:descobrimento16
"1455"

"Bula Romanus Pontifex do Papa Nicolau V confirma as explorações portuguesas e declara que todas as terras e mares a sul do Cabo Bojador e do Cabo Não são pertença dos reis de Portugal."

:descobrimento17
"1455"

"Alvise Cadamosto explora do Rio Gâmbia ao Rio Geba (Guiné-Bissau) e o arquipélago dos Bijagós. Na foz do Gâmbia, ao notar o quase desaparecimento da estrela polar no horizonte, esboçou a primeira representação da constelação Cruzeiro do Sul."

:descobrimento18
"1456 – 1460"

"Viagens de Alvise Cadamosto, Diogo Gomes e António da Noli descobriram as primeiras ilhas do arquipélago de Cabo Verde (Boavista, Santiago, Maio e Sal)."

:descobrimento0
"1459"

"Afonso V encomenda o mapa-múndi que reúne o conhecimento geográfico da época aos venezianos Fra Mauro e Andrea Bianco."

:descobrimento19
"1460"

"Morte do Infante D. Henrique. A sua exploração sistemática do Atlântico alcançou em vida os 8º N na costa africana, com a chegada de Pêro de Sintra à que chama Serra Leoa, e os 40º O no oceano Atlântico (Mar dos Sargaços)."

:descobrimento20
"1461"

"Diogo Gomes e Antonio da Noli descobrem mais ilhas de Cabo Verde."

:descobrimento21
"1462"

"Diogo Afonso descobre as cinco ilhas mais ocidentais do arquipélago de Cabo Verde (Brava, São Nicolau, São Vicente, Santo Antão e os ilhéus Branco e Raso)."

:descobrimento22
"1469"

"Contrato de Fernão Gomes para a exploração da costa da Guiné a sul da Serra Leoa e comércio de Pimenta-da-Guiné que duraria até 1475."

:descobrimento23
"1471"

"João de Santarém e Pêro Escobar encontram a costa da Mina, chegam região do Cabo Três Pontos (Gana) e ao delta do Níger."

:descobrimento24
"1472"

"Rui de Sequeira chega a Benim nomeando a área Lagoa de Lagos (actual Lagos (Nigéria))."

:descobrimento25
"1472-1473"

"Fernão Pó explora o rio Wouri que nomeia “dos Camarões“ e as ilhas Formosa (Bioko) e Ano Bom."

:descobrimento26
"1472-1474"

"João Vaz Corte-Real e Álvaro Martins Homem poderão ter atingido a Terra Nova. Descoberta não comprovada"

:descobrimento27
"1473"

"Lopo Gonçalves descobre o Cabo Lopez , na boca do rio Ogooué e é creditado como o primeiro a cruzar a linha do equador e chegar ao Hemisfério Sul"

:descobrimento29
"1474"

"Afonso V consulta o geógrafo Toscanelli, que aconselha a navegação para oeste, para chegar à Índia"

:descobrimento28
"1475"

"Antes do contrato de Fernão Gomes terminar, Rui de Sequeira e Lopo Gonçalves chegam ao cabo de St. Catarina, 2º latitude Sul"

:descobrimento30
"1479"

"O Tratado das Alcáçovas estabelece o controlo português dos Açores, Guiné, Elmina, Madeira e Cabo Verde e o controlo castelhano das ilhas Canárias"

:descobrimento31
"1482 – 1484"

"Diogo Cão chegou ao estuário do Rio Congo (Congo) onde deixou um Padrão de pedra, substituindo as habituais cruzes de madeira; subiu 150Km a montante do rio até às cataratas de Ielala."

:descobrimento32
"1484-1486"

"Afonso de Paiva, Duarte Pacheco Pereira e José Vizinho contactam o Reino de Benim onde a presença de pimenta indicia a proximidade da Índia."

:descobrimento33
"1485-1486"

"Segunda viagem de Diogo Cão leva Martin Behaim como cosmógrafo, passa o Cabo Padrão (Cape Cross) e terá chegado a Walvis Bay, na Namíbia."

:descobrimento35
"1487"

"Afonso de Paiva e Pêro da Covilhã partiram de Lisboa, viajando por terra em busca do reino do Preste João, na Etiópia."

:descobrimento34
"1487"

"Gonçalo Eanes e Pêro de Évora sobem o rio Senegal numa expedição ao interior africano até Tucurol e Timbuctu."

:descobrimento36
"1487-1488"

"Bartolomeu Dias dobra o Cabo das Tormentas, futuro Cabo da Boa Esperança, coroando 50 anos de esforço e numerosas expedições, entrando pela primeira vez no Oceano Índico. Afonso de Paiva e Pêro da Covilhã chegam a Adém."

:descobrimento37
"1489 – 1492"

"Foram feitas várias expedições ao Atlântico Sul para mapear os ventos."

:descobrimento38
"1494"

"Assinado o Tratado de Tordesilhas “dividindo” o mundo por descobrir entre Portugal e o recém-formado Reino da Espanha, na sequência da contestação de D. João II às pretensões espanholas após a viagem de Cristovão Colombo."

:descobrimento39
"1495 ou 1500"

"Viagem de João Fernandes Lavrador e Pêro de Barcelos à Gronelândia (Terra do Bacalhau). Nesta viagem avistaram a terra que nomearam Labrador (lavrador)."

:descobrimento40
"1497 – 1499"

"Vasco da Gama comandou a primeira frota a contornar África e chegar a Calecute na Índia, e regressou."

:descobrimento41
"1498"

"Duarte Pacheco Pereira terá explorado o Atlântico Sul (e terá alcançado a foz do rio Amazonas e a ilha do Marajó no que terá sido uma expedição secreta - não existem provas)"

:descobrimento44
"1500"

"A 10 de Agosto Diogo Dias, navegador da frota de Pedro Álvares Cabral na segunda armada à Índia, descobriu uma ilha a que deu o nome de São Lourenço, mais tarde designada Madagáscar."

:descobrimento43
"1500 – 1501"

"Gaspar Corte Real e Miguel Corte Real atingiram a Terra Nova, que nomeou Terras de Corte Real (Canadá)."

:descobrimento42
"1500-1501"

"A segunda frota para a Índia comandada por Pedro Álvares Cabral atinge o Brasil, aportando em Porto Seguro."

:descobrimento45
"1502"

"Vasco da Gama vindo da Índia avistou as então chamadas Ilhas do Almirante (Seychelles)."

:descobrimento46
"1502"

"Miguel Corte-Real partiu para a Nova Inglaterra em busca do seu irmão Gaspar. João da Nova descobriu a Ilha de Ascensão. Fernão de Noronha descobriu as ilhas que mantêm o seu nome, Fernando Noronha, em Pernambuco."

:descobrimento47
"1503"

"De regresso do Oriente Estêvão da Gama descobriu a Ilha de Santa Helena."

:descobrimento48
"1503"

"A caminho da Índia António de Saldanha foi o primeiro europeu a ancorar na Baía de Saldanha e a ascender à Montanha da Mesa, que nomeou, na atual África do Sul."

:descobrimento49
"1505"

"Gonçalo Álvares, da frota do primeiro vice-rei, ruma a sul, onde “a água e até o vinho gelavam” descobrindo a Ilha de Gonçalo Álvares (Gough Island)"

:descobrimento5
"1505-1506"

"Lourenço de Almeida é enviado às Maldivas e chegou ao que chamou Ceilão (Sri Lanka), a “Taprobana” dos registos clássicos gregos."

:descobrimento51
"1506"

"Tristão da Cunha descobriu a Ilha de Tristão da Cunha no Atlântico Sul. Navegadores portugueses aportaram em Madagáscar."

:descobrimento52
"1507-1512"

"Os portugueses foram os primeiros europeus a aportar nas Ilhas Mascarenhas, no Índico, nomeadas em homenagem a Pedro Mascarenhas"

:descobrimento53
"1509"

"Diogo Lopes de Sequeira atravessou o golfo de Bengala e chegou a Sumatra e Malaca (Malásia), com ele viajava Fernão de Magalhães, que viria a fazer a primeira viagem de circum-navegação ao globo, ao serviço de Espanha em 1519-22."

:descobrimento54
"1511"

"Duarte Fernandes foi o primeiro europeu a chegar ao Reino do Sião (Tailândia), enviado por Afonso de Albuquerque durante a conquista de Malaca."

:descobrimento56
"1511"

"António de Abreu, Francisco Serrão e Simão Afonso Bisagudo são enviados por Albuquerque em busca das “ilhas das especiarias”. Serrão naufraga em Ternate nas Molucas (Indonésia)."

:descobrimento55
"1511"

"Rui Nunes da Cunha foi o primeiro europeu a chegar a Pegu (Birmânia, Myanmar), enviado por Albuquerque."

:descobrimento57
"1512"

"António de Abreu chegou à ilha de Timor e às ilhas Banda, Ambão e Seram."

:descobrimento58
"1512"

"Pedro Mascarenhas descobriu a ilha Diego Garcia. Chegou também à ilha Maurícia, que poderia já ser conhecida em expedições anteriores de Diogo Dias e Afonso de Albuquerque."

:descobrimento59
"1512-1514"

"João de Lisboa e Estevão Fróis exploram o estuário do Rio da Prata e provavelmente o Golfo de San Matias a 42° Sul na Argentina22 Cristóvão de Haro, financiador da expedição, testemunha a viagem e as notícias que Lisboa e Fróis recolheram do Rei Branco(o imperador Inca), assim como o machado de prata obtido junto dos nativos e que ofereceram ao rei D. Manuel I."

:descobrimento61
"1513"

"Jorge Álvares partindo de Malaca é o primeiro europeu a aportar no sul da China, na Ilha de Lintin, no estuário do Rio das Pérolas."

:descobrimento60
"1513"

"Afonso de Albuquerque atravessa o estreito de Bab-el-Mandeb no Mar Vermelho liderando a primeira frota europeia a navegar nessas águas."

:descobrimento62
"1516"

"Portugueses aportam em Da Nang, no Reino de Champa que nomeiam Cochinchina, (actual Vietname)."

:descobrimento63
"1516-1517"

"Rafael Perestrelo comandou o primeiro navio comercial a aportar na China continental, em Cantão. Fernão Pires de Andrade e Tomé Pires foram enviados de D. Manuel I para estabelecer relações oficiais entre o Império Português e a Dinastia Ming, no reinado do imperador Zhengde."

:descobrimento64
"1520"

"Francisco Álvares e uma embaixada portuguesa chegam à Etiópia onde encontram Pêro da Covilhã"

:descobrimento81
"1520"

"(Diogo Pacheco terá visitado Kimberley, na Austrália em busca da “Ilha do Ouro” a sul e a sudeste de Sumatra, Java e Sunda, informado por Malaios e Sumatreses, presentes na sua tripulação.- descoberta não reconhecida oficialmente)"

:descobrimento65
"1522"

"(Cristovão de Mendonça terá descoberto a Austrália e Nova Zelândia, partindo de Belém em 1521.[carece de fontes]- descoberta não reconhecida oficialmente)"

:descobrimento67
"1525"

"Aleixo Garcia explora o Rio da Prata ao serviço de Espanha, como membro da expedição de Juan Díaz de Solís e, mais tarde, liderando uma expedição privada de Europeus e indios Guarani, explora o Paraguai e a Bolívia. Aleixo Garcia foi o primeiro europeu a atravessar o Chaco e a conseguir penetrar nas defesas exteriores do Império Inca, já nas colinas dos Andes e na região de Sucre, na atual Bolívia. Aleixo Garcia foi o primeiro europeu a fazê-lo, realizando a façanha oito anos antes de Francisco Pizarro."

:descobrimento66
"1525"

"Gomes de Sequeira e Diogo da Rocha foram enviados pelo governador Jorge de Meneses, à descoberta de territórios a norte das Molucas, foram os primeiros europeus a chegar às Ilhas Carolinas, a nordeste da Nova Guiné, que então nomearam “Ilhas de Sequeira”. (A Austrália teria sido descoberta por Cristóvão de Mendonça e Gomes de Sequeira em 1525 – descoberta não reconhecida oficialmente)"

:descobrimento68
"1526"

"Jorge de Meneses aportou na ilha Waigeo na Papua Ocidental (Nova Guiné)"

:descobrimento69
"1528"

"Diogo Rodrigues explorou o arquipélago das Mascarenhas, que nomeou em homenagem ao seu companheiro Pedro de Mascarenhas, que compreende a Reunião, Maurícia e Rodrigues."

:descobrimento70
"1529"

"É assinado o Tratado de Saragoça, que estabelece o antimeridiano de Tordesilhas, limite leste das explorações portuguesas e espanholas, para solucionar a chamada “Questão das Molucas“."

:descobrimento71
"1538"

"João Fogaça chega à Papua-Nova Guiné enviado por António Galvão."

:descobrimento72
"1542-1543"

"Fernão Mendes Pinto, Diogo Zeimoto e Cristovão Borralho chegaram ao Japão."

:descobrimento73
"1586"

"António da Madalena um frade Capuchinho foi um dos primeiros visitantes ocidentais a chegar a Angkor (actual Camboja)"

:descobrimento74
"1602 – 1606"

"Bento de Góis, missionário jesuíta, foi o primeiro europeu a percorrer o caminho terrestre da Índia para a China, através da Ásia Central."

:descobrimento75
"1606"

"Pedro Fernandes de Queirós alcançou as Novas Hébridas e aportou numa grande ilha que pensou ser parte do procurado continente a sul, a que chamou Ilha do Espírito Santo, actual Vanuatu."

:descobrimento76
"1606"

"Luís Vaz de Torres (nacionalidade incerta) descobriu o que é hoje chamado Estreito de Torres."

:descobrimento77
"1624"

"António de Andrade e Manuel Marques, missionários jesuítas, viajaram de Agra (Índia) a Chaparangue, sendo os primeiros europeus documentados a chegar ao Tibete"

:descobrimento78
"1626"

"Estêvão Cacella, missionário jesuíta, viajou através dos Himalaias e foi o primeiro europeu a entrar no Butão34"

:descobrimento79
"1636 – 1638"

"Pedro Teixeira partiu de Belém do Pará subindo o rio Amazonas e alcançou Quito, no Equador, numa expedição de mais de mil homens."

:descobrimento80
"1660 – 1662"

"(David Melgueiro partindo de Tanegashima poderá ter realizado a primeira travessia da passagem do Nordeste. 35 Viagem pouco documentada, ao serviço da marinha holandesa)"
"""

# Gerar a tabela em Markdown
# markdown_output = descobrimento(descobrimentos)
markdown_output = conquista(conquistas)

# Imprimir a tabela resultante
print(markdown_output)

