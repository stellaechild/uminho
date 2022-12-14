-- | Este módulo define funções comuns da Tarefa 3 do trabalho prático.
module Tarefa3_2019li1g126 where

import LI11920
import Tarefa0_2019li1g126
import Data.List
--import Tarefa1_2019li1g126
-- * Testes

-- | Testes unitários da Tarefa 3.
--
-- Cada teste é um 'Mapa'.
testesT3 :: [Mapa]
testesT3 = [
            [
             [Recta Terra 0, Recta Relva 0], 
             [Recta Terra 0, Recta Terra 0], 
             [Recta Terra 0, Recta Terra 0], 
             [Recta Terra 0, Recta Terra 0], 
             [Recta Terra 0, Rampa Terra 0 2], 
             [Recta Terra 0, Rampa Terra 0 1]
            ],
            [
             [Recta Terra 0, Recta Relva 0, Rampa Boost 0 9], 
             [Recta Terra 0, Recta Terra 0, Recta Lama 0], 
             [Recta Terra 0, Recta Terra 0, Rampa Relva 0 2], 
             [Recta Terra 0, Recta Terra 0, Recta Terra 0], 
             [Recta Terra 0, Rampa Terra 0 2, Rampa Relva 2 1], 
             [Recta Terra 0, Rampa Terra 0 1, Rampa Boost 1 0],
             [Recta Terra 0, Rampa Boost 0 3, Rampa Lama 3 1]
            ],
            [
             [Recta Terra 0,Recta Boost 0,Recta Boost 0,Recta Boost 0,Recta Lama 0,Recta Terra 0,Recta Terra 0,Rampa Lama 0 2,Rampa Lama 2 0,Rampa Relva 0 1,Rampa Relva 1 0,Recta Lama 0,Recta Terra 0,Rampa Terra 0 2,Rampa Boost 2 0,Recta Terra 0,Recta Terra 0,Recta Terra 0,Recta Lama 0,Recta Terra 0], 
             [Recta Terra 0,Recta Terra 0,Recta Terra 0,Recta Terra 0,Recta Terra 0,Recta Relva 0,Rampa Relva 0 2,Rampa Relva 2 0,Recta Boost 0,Recta Boost 0,Recta Terra 0,Recta Terra 0,Recta Relva 0,Recta Relva 0,Recta Terra 0,Recta Terra 0,Recta Terra 0,Recta Relva 0,Recta Relva 0,Recta Boost 0], 
             [Recta Terra 0,Recta Relva 0,Recta Relva 0,Recta Relva 0,Recta Relva 0,Recta Terra 0,Recta Terra 0,Rampa Boost 0 1,Rampa Relva 1 0,Recta Relva 0,Rampa Relva 0 1,Recta Lama 1,Rampa Lama 1 3,Rampa Lama 3 0,Rampa Relva 0 1,Rampa Relva 1 0,Recta Relva 0,Recta Relva 0,Recta Relva 0,Recta Relva 0]
            ],
            [
             [Recta Terra 0, Rampa Lama 0 8, Rampa Boost 8 6, Recta Relva 6],
             [Recta Terra 0, Recta Boost 0, Rampa Boost 0 10, Rampa Terra 10 2],
             [Recta Terra 0, Recta Relva 0, Recta Relva 0, Recta Relva 0]
            ]
           ]

-- * Funções principais da Tarefa 3.

-- | Desconstrói um 'Mapa' numa sequência de 'Instrucoes'.
--
-- __NB:__ Uma solução correcta deve retornar uma sequência de 'Instrucoes' tal que, para qualquer mapa válido 'm', executar as instruções '(desconstroi m)' produza o mesmo mapa 'm'.
--
-- __NB:__ Uma boa solução deve representar o 'Mapa' dado no mínimo número de 'Instrucoes', de acordo com a função 'tamanhoInstrucoes'.
desconstroi :: Mapa -> Instrucoes
desconstroi mapa = (padroesHorizontais (gInstrucoes mapa))
           

-- *** Gera Instrucao
-- | dada uma lista verifica se a cabeca e igual a um tipo de peca e da a instrucao de acordo com a peca --
gerarInstrucao :: Mapa -> Int -> Instrucao 
gerarInstrucao mapa@(((Recta piso alturaf):xs):t) a = Anda [a] piso 
gerarInstrucao mapa@(((Rampa piso alturai alturaf):xs):t) a | (alturai<alturaf) = Sobe [a] piso (alturaf-alturai)
                                                            | (alturai>alturaf) = Desce [a] piso (alturai-alturaf)
                                            
-- ** Gera Instrucoes
-- | mapa = [pista] = [[peca]] --
-- | gerarinstrucoes e uma funcao que tem que gerar uma [instrucao], ou seja instucoes, utilizando a funcao 'gerarInstrucao', 'listaSemPRimeiraInstrucao' e a funcao 'bEs' --
gerarInstrucoes :: Mapa -> (Int,Int) -> Int -> Instrucoes
gerarInstrucoes [] _ _ = []
gerarInstrucoes mapa@(h:t) (a,b) d = (gerarInstrucao mapa a) : (gerarInstrucoes listasemprimeirainstrucao (bEs (a,b) d mapa) d)
                              where  listasemprimeirainstrucao | head (listaSemPRimeiraInstrucao mapa) == [] = tail (listaSemPRimeiraInstrucao mapa)
                                                               | otherwise = listaSemPRimeiraInstrucao mapa
-- *** Retira Primeiro Elemento da Lista
-- | dado o mapa, retira sempre a primeira instrucao para poder gerar a proxima instrucao --
listaSemPRimeiraInstrucao :: Mapa -> Mapa
listaSemPRimeiraInstrucao [] = []
listaSemPRimeiraInstrucao mapa@(h:t) | h == [] = t
                                     | otherwise = (drop 1 h):t

-- *** Contador Pistas
-- | um contador que serve para indicar a pista em que a peca se enconte, nomeadamente o [x] --
bEs :: (Int,Int) -> Int -> Mapa -> (Int,Int)
bEs (a,b) d mapa | b +1  <= d  = (a,b+1)
                 | otherwise = (a+1,1)  
             
-- * Gera as Instrucoes Finais
-- | dado um mapa cria uma lista de instrucoes de acordo com a peca, usando a funcao 'gerarInstrucoes' e a funcao 'dimensaoMatriz' da Tarefa 0 --
gInstrucoes :: Mapa -> Instrucoes
gInstrucoes mapa  = gerarInstrucoes mapaSemReTerra0 (0,1) d
                      where d = snd (dimensaoMatriz mapaSemReTerra0)
                            mapaSemReTerra0 = map (drop 1) mapa

-- * Padroes Horizontais 
-- | dada uma lista de instrucoes, vai verificar quais sao os padroes e comprimir assim, em um repete. Usa a funao 'nmrVezes' para saber quantas vezes repete o elemento na 
padroesHorizontais :: Instrucoes ->  Instrucoes
padroesHorizontais []  = []
padroesHorizontais [x] = [x]
padroesHorizontais instrucoes@((Anda [x] piso):t)  | (head instrucoes == head t) = (Repete (vezes+1) [(head instrucoes)]) : (padroesHorizontais (drop vezes t) )
                                                   | otherwise = (head instrucoes): (padroesHorizontais t)
                                                          where    vezes = (nmrVezes instrucoes 0)
padroesHorizontais instrucoes@((Sobe [x] piso altura):t)  | (head instrucoes == head t) = (Repete (vezes+1) [(head instrucoes)]) : (padroesHorizontais (drop vezes t) )
                                                          | otherwise = (head instrucoes): (padroesHorizontais t)
                                                             where    vezes = (nmrVezes instrucoes 0)
padroesHorizontais instrucoes@((Desce [x] piso altura):t)  | (head instrucoes == head t) = (Repete (vezes+1) [(head instrucoes)]) : (padroesHorizontais (drop vezes t) )
                                                           | otherwise = (head instrucoes): (padroesHorizontais t)
                                                              where    vezes = (nmrVezes instrucoes 0)

-- *** Contador Vezes de Elementos Repetidos
-- | contador do numero de vezes que uma determinada instrucao aparece repetido
nmrVezes :: Eq a => [a] -> Int -> Int
nmrVezes [] _ = 0
nmrVezes [x] _ = 0
nmrVezes (h:t) x | (h == head t) && ((length (h:t))>0) = x+1 + (nmrVezes t x)
                 | otherwise = 0 


-- * Padroes Verticais
--  dada uma lista de instrucoes, vai verificar quais sao os padroes e comprimir assim, em um repete. Usa a funao 'nmrVezes' para saber quantas vezes repete o elemento na 
-- Nao conseguimos implementar a funcao dos padroes verticais pois nao conseguimos verificar certos casos, assim como nao cosnsguimos arranjar uma maneira de colocar os elementos das instrucoes por [0] -> [1] -> ... -> [x] -> [0] -> .... quando se recebe algo do genero [0] -> [0] -> [1] -> [1] -> ... [x] 
{--
padroesVerticais :: Instrucoes -> Instrucoes
padroesVerticais []  = []
padroesVerticais [x] = [x]
padroesVerticais instrucoes@((Anda [x] piso):t)   | (head t == Anda [y] piso) = (Anda [x,y] piso) : (padroesVerticais (drop 1 t) )
                                                  | otherwise = (head instrucoes): (padroesVerticais t)
                                                  where y = x+1 
padroesVerticais instrucoes@((Sobe [x] piso altura):t)    | (head t == Sobe [y] piso altura) = (Sobe [x,y] piso altura) : (padroesVerticais (drop 1 t) )
                                                          | otherwise = (head instrucoes): (padroesVerticais t)
                                                           where y = x+1
padroesVerticais instrucoes@((Desce [x] piso altura):t)   | (head t == Desce [y] piso altura) = (Desce [x,y] piso altura) : (padroesVerticais (drop 1 t) )
                                                          | otherwise = (head instrucoes): (padroesVerticais t)
                                                           where y = x+1
--}

-- * Padroes Verticais Desfasados
-- | Dado ao facto de nao termos cosenguido fazer os padroes verticais, deduzimos que tambem nao conseguiriamos fazer os padroes verticais desfazados





-- | Relatório da Tarefa 3:
-- | Introdução: A realização da tarefa 3 foi feita de maneira a que se pudesse criar instruções, lendo o mapa.

-- | Objetivos: O nosso objetivo era gerar instruções com padrões verticais e horizontais, fieis aos dados de um mapa.

-- | Discussão e Conclusão: Devido ao problema da tarefa 1 que atrasou o desenvolvimento das tarefas 2 e 3 ,os membros decidiram designar uma das tarefas para cada um.
-- | Esta tarefa revelou-se um desafio, no facto de haver funções que não nos foi possível implementar.
-- | Não conseguimos implementar a funcao dos padroes verticais pois nao conseguimos verificar certos casos, assim como nao cosnsguimos arranjar uma maneira de colocar os elementos das instrucoes por [0] -> [1] -> ... -> [x] -> [0] -> .... quando se recebe algo do genero [0] -> [0] -> [1] -> [1] -> ... [x] 
-- | Dado ao facto de nao termos cosenguido fazer os padroes verticais, deduzimos que tambem nao conseguiriamos fazer os padroes verticais desfazados.







              