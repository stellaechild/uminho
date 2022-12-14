-- | Este módulo define funções comuns da Tarefa 2 do trabalho prático.
module Tarefa1_2019li1g126 where

import LI11920
import System.Random

-- * Testes

-- | Testes unitários da Tarefa 1.
--
-- | Cada teste é um tuplo (/número de 'Pista's/,/comprimento de cada 'Pista' do 'Mapa'/,/semente de aleatoriedades/).
testesT1 :: [(Int,Int,Int)]
testesT1 = [(0,1,3),(1,1,20),(4,6,1),(1,500,3),(5,6,7),(4,9,3),(1,5,6),(7,2,9),(9,6,3),(3,6,0),(2,5,1),(6,2,2),(3,4,3),(9,8,4),(8,2,5),(5,9,5),(5,5,6),(1,9,7),(6,2,8),(5,9,9)]

-- * Funções pré-definidas da Tarefa 1.
-- | o geraAleatorios é uma funcao dada
geraAleatorios :: Int -> Int -> [Int]
geraAleatorios n seed = take n (randomRs (0,9) (mkStdGen seed))


-- | * Funções principais da Tarefa 1.
-- | gera npistas comprimento semente 
-- | n do geraALeatorios == 2*npistas*comprimentoPistas-2*npistas --
-- | a gera é a funcao principal
gera :: Int -> Int -> Int -> Mapa
gera numeropistas comprimentopistas seed = if (comprimentopistas /= 1) then retaTerra (map (geraListaPecas 0 Terra) listaParesSplit) 
                                           else (gerAux numeropistas)
                                             where n = 2*numeropistas*(comprimentopistas-1)
                                                   listaParesSplit = geraListaPistas (comprimentopistas -1) ((agrupaPares (geraAleatorios n seed)))
-- | funcao auxiliar para gera
gerAux :: Int -> Mapa
gerAux numeropistas = if numeropistas > 0 then [(Recta Terra 0)] : gerAux (numeropistas-1) else []

{--
-- calcula o comprimento das pistas --
comprimentoPistas :: [[(Int,Int)]] -> Int
comprimentoPistas (h:t) = length h + 1 --(Recta terra 0)-- --

-- calcula o numero de pistas --
npistas :: [[(Int,Int)]] -> Int
npistas (h:t) =  length (h:t) 
--}
-- | a retaTerra é uma funcao que adiciona reta terra no inicio de cada pista
retaTerra :: [Pista] -> [Pista]
retaTerra [] = []
retaTerra (h:t) = (Recta Terra 0:h) : retaTerra t

-- | o geraListaPistas tem a funcao de gerar listas de pistas
geraListaPistas :: Int -> [(Int,Int)] -> [[(Int,Int)]]
geraListaPistas cPistas [] = []
geraListaPistas cpistas (h:t) = (take cpistas (h:t)): (geraListaPistas cpistas (drop cpistas (h:t)))


-- | recebe npistas o comprimento e a seed -> um mapa -> [PISTAS] -> [PECAS] --
agrupaPares::[Int]->[(Int,Int)]
agrupaPares [] = []
agrupaPares [x] = []
agrupaPares (h:t) = (h,a) : agrupaPares(drop 1 t) 
 where     a = head t

 
 

{--
--serve para agrupar a lista dos pisos com a lista das pecas --
agrupaListasEmPares::[Piso]->[Peca]->[(Piso,Peca)]
agrupaListasEmPares [] [] =[]
agrupaListasEmPares (x:xs) (y:ys) = (x,y):agrupaListasEmPares xs ys
--}




-- | o primeiro numero do par e o tipo de piso, o segundo numero do par e o tipo da peca --

escolhePiso :: Int -> Piso -> Piso
escolhePiso piso  pisoanterior    | (piso>=0) && (piso<=1) = Terra 
                                  | (piso>=2) && (piso<=3) = Relva 
                                  | (piso==4)              = Lama 
                                  | (piso==5)              = Boost         
                                  | (piso>=6) && (piso<=9) = pisoanterior 

-- | gera lista pisos gera , exatamente o que o nome diz, lista de pisos                  
--geraListaPisos :: [(Int,Int)] -> Piso -> [Piso]
--geraListaPisos ((piso,peca):t) pisoanterior = pisob: geraListaPisos t pisob
 --            where pisob = escolhePiso piso pisoanterior 
--geraListaPisos [] pisoanterior = []
 

-- | esta funcao recebe h que é a altura da pista neste momento e n que é a gama que determina o tipo de peca
-- | como  as rampas podem descer e/ou subir e como hmin=0 entao pus de parte estes casos
-- | esta funcao calcula a altura final de uma peca
alturaFinal :: Peca -> Int
alturaFinal (Recta piso altura) = altura
alturaFinal (Rampa piso alturainicial alturaf) | (alturainicial >= 0) && (alturainicial < alturaf) = alturaf--(alturainicial+(peca+1))
                                               | (alturainicial >= 0) && (alturainicial > alturaf) = alturaf--(alturainicial-(peca-1))
                                               | otherwise = 0

-- | o escolhe peca calcula a peca dada atraves de dados anteriores
escolhePeca:: Int -> (Int,Int) -> Piso -> Peca 
escolhePeca alturainicial (piso,peca) pisoanterior  | alturainicial>=0 && ((peca>=0) && (peca<=1)) = Rampa  pisof alturainicial (alturainicial+(peca+1))
                                                    | alturainicial>=1 && ((peca>=2) && (peca<=5)) = Rampa  pisof alturainicial alturaf                   
                                                    | alturainicial>=0 && alturaf==0 && ((peca>=2) && (peca<=5)) = Recta pisof 0
                                                    | alturainicial>=0 && ((peca>=6) && (peca<=9)) = Recta  pisof alturainicial 
                                          
                                          where pisof = (escolhePiso piso pisoanterior)
                                                alturaf = if ((alturainicial-(peca-1)) <= 0) then 0 else (alturainicial-(peca-1)) 

-- | a geraListaPecas, gera listas de pecas tal como o nome sugere
geraListaPecas :: Int -> Piso -> [(Int,Int)] -> [Peca] 
geraListaPecas alturainicial pisoanterior []  = [] 
geraListaPecas alturainicial pisoanterior ((piso,peca):t)  = pecab : (geraListaPecas alturaf novopiso t ) 
                 where novopiso = escolhePiso piso pisoanterior
                       pecab = escolhePeca alturainicial (piso,peca) pisoanterior  
                       alturaf = alturaFinal pecab


-- | Relatório da Tarefa 1:
-- | A Tarefa 1 teve um desenvolvimento rápido e eficiente com a exceção de um problema de alturas com as rampas. Este problema atrasou o desenvolvimento das tarefas 2 e 3 mas, felizmente, conseguimos resolvê-lo.
-- | Assim, esta tarefa foi desenvolivada conforme as expetativas dos docentes.