-- | Este módulo define funções genéricas sobre vetores e matrizes, que serão úteis na resolução do trabalho prático.
module Tarefa0_2019li1g126 where

-- * Funções não-recursivas.

-- | Um ponto a duas dimensões dado num referencial cartesiado (distâncias aos eixos vertical e horizontal)
--
-- <<http://li1.lsd.di.uminho.pt/images/cartesiano.png cartesisano>>
-- , ou num referencial polar (distância à origem e ângulo do respectivo vector com o eixo horizontal).
--
-- <<http://li1.lsd.di.uminho.pt/images/polar.png polar>>
data Ponto = Cartesiano Double Double | Polar Double Angulo
     deriving (Show)
-- | Um ângulo em graus.
type Angulo = Double

-- ** Funções sobre vetores

-- | Um 'Vetor' na representação escalar é um 'Ponto' em relação à origem.
type Vetor = Ponto
-- ^ <<http://li1.lsd.di.uminho.pt/images/vetor.png vetor>>

-- | *** Funções gerais sobre 'Vetor'es.
polarToCartesiano:: Vetor -> Vetor
polarToCartesiano (Polar n a) = Cartesiano x y
     where x = n * cos ((a*pi)/180)
           y = n * sin ((a*pi)/180)
polarToCartesiano (Cartesiano x y) = Cartesiano x y


-- | calcula a distancia do ponto ao eixo vertical --
posx :: Ponto -> Double
posx (Cartesiano x y) = x
posx (Polar n a) = n * cos ((a*pi)/180)

-- | calcula a distancia de um ponto ao eixo horizontal --
posy :: Ponto -> Double
posy (Cartesiano x y) = y
posy (Polar n a) = n * sin ((a*pi)/180)

-- | Soma dois 'Vetor'es.
somaVetores :: Vetor -> Vetor -> Vetor    
somaVetores (Cartesiano x1 y1) (Cartesiano x2 y2) = Cartesiano (x1+x2) (y1+y2)
somaVetores v1 v2 = 
     somaVetores (polarToCartesiano v1) (polarToCartesiano v2)

-- | somaVetores (Polar x1 y1) (Polar x2 y2)=            
-- | Subtrai dois 'Vetor'es.
subtraiVetores :: Vetor -> Vetor -> Vetor
subtraiVetores (Cartesiano x1 y1) (Cartesiano x2 y2) = Cartesiano (x1-x2) (y1-y2)
subtraiVetores v1 v2 = 
     subtraiVetores (polarToCartesiano v1) (polarToCartesiano v2)
-- | Multiplica um escalar por um 'Vetor'.
multiplicaVetor :: Double -> Vetor -> Vetor
multiplicaVetor a (Cartesiano x1 y1) = Cartesiano (a*x1) (a*y1)
multiplicaVetor a v1 = 
     multiplicaVetor a (polarToCartesiano v1)

-- ** Funções sobre rectas.

-- | Um segmento de reta é definido por dois pontos.
type Reta = (Ponto,Ponto)

-- | Testar se dois segmentos de reta se intersetam.
--
-- | __NB:__ Aplique as equações matemáticas bem conhecidas, como explicado por exemplo em <http://www.cs.swan.ac.uk/~cssimon/line_intersection.html>.
intersetam :: Reta -> Reta -> Bool
intersetam (ponto1,ponto2) (ponto3,ponto4)
                                           | (1>=ta && ta>=0) && (1>=tb && tb>=0) = True
                                           | otherwise = False
    where 
          x1 = posx ponto1
          y1 = posy ponto1
          x2 = posx ponto2
          y2 = posy ponto2
          x3 = posx ponto3
          y3 = posy ponto3
          x4 = posx ponto4
          y4 = posy ponto4
          ta = ((y3-y4)*(x1-x3)+(x4-x3)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3))
          tb = ((y1-y2)*(x1-x3)+(x2-x1)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3))
--
-- | __NB:__ Aplique as equações matemáticas bem conhecidas, como explicado por exemplo em <http://www.cs.swan.ac.uk/~cssimon/line_intersection.html>.
intersecao :: Reta -> Reta -> Ponto
intersecao (ponto1,ponto2) (ponto3,ponto4)= if (intersetam (ponto1,ponto2) (ponto3,ponto4) == False) then error "As retas nao se intersetam" else somaVetores ponto1 (multiplicaVetor ta (subtraiVetores ponto2 ponto1))

   where 
         x1 = posx ponto1
         y1 = posy ponto1
         x2 = posx ponto2
         y2 = posy ponto2
         x3 = posx ponto3
         y3 = posy ponto3
         x4 = posx ponto4
         y4 = posy ponto4
         ta = ((y3-y4)*(x1-x3)+(x4-x3)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3))
         tb = ((y1-y2)*(x1-x3)+(x2-x1)*(y1-y3))/((x4-x3)*(y1-y2)-(x1-x2)*(y4-y3))

-- ** Funções sobre listas

-- *** Funções gerais sobre listas.
--
-- Funções não disponíveis no 'Prelude', mas com grande utilidade.

-- | Verifica se o indice pertence à lista.
--
-- | __Sugestão:__ use a função 'length' que calcula tamanhos de listas
eIndiceListaValido ::  Int -> [a] -> Bool
eIndiceListaValido i lista = if (0<= i && i<= (length lista)-1) then True else False
                        

-- ** Funções sobre matrizes.

-- *** Funções gerais sobre matrizes.

-- | A dimensão de um mapa dada como um par (/número de linhas/,/número de colunhas/).
type DimensaoMatriz = (Int,Int)

-- | Uma posição numa matriz dada como um par (/linha/,/colunha/).
-- As coordenadas são dois números naturais e começam com (0,0) no canto superior esquerdo, com as linhas incrementando para baixo e as colunas incrementando para a direita:
--
-- <<http://li1.lsd.di.uminho.pt/images/posicaomatriz.png posicaomatriz>>
type PosicaoMatriz = (Int,Int)

-- | Uma matriz é um conjunto de elementos a duas dimensões.
--
-- Em notação matemática, é geralmente representada por:
--
-- <<https://upload.wikimedia.org/wikipedia/commons/d/d8/Matriz_organizacao.png matriz>>
type Matriz a = [[a]]

-- | Calcula a dimensão de uma matriz.
--
-- __NB:__ Note que não existem matrizes de dimensão /m * 0/ ou /0 * n/, e que qualquer matriz vazia deve ter dimensão /0 * 0/.
--
-- | __Sugestão:__ relembre a função 'length', referida anteriormente.
dimensaoMatriz :: Matriz a -> DimensaoMatriz
dimensaoMatriz [] = (0,0)
dimensaoMatriz (h:t) = if linhas > 0 then (colunas,linhas) else (0,0)
                where colunas = (length t)+ 1
                      linhas = length h

-- | Verifica se a posição pertence à matriz.
ePosicaoMatrizValida :: PosicaoMatriz -> Matriz a -> Bool 
ePosicaoMatrizValida x [] = False
ePosicaoMatrizValida (x,y) m  = 0<=x &&  x<w && 0<=y && y<z                            
                    where (w,z) = dimensaoMatriz m 

-- * Funções recursivas.

-- ** Funções sobre ângulos

-- | Normaliza um ângulo na gama [0..360).
--  Um ângulo pode ser usado para representar a rotação
--  que um objecto efectua. Normalizar um ângulo na gama [0..360)
--  consiste, intuitivamente, em extrair a orientação do
--  objecto que resulta da aplicação de uma rotação. Por exemplo, é verdade que:
--
-- prop> normalizaAngulo 360 = 0
-- prop> normalizaAngulo 390 = 30
-- prop> normalizaAngulo 720 = 0
-- prop> normalizaAngulo (-30) = 330
normalizaAngulo :: Angulo -> Angulo
normalizaAngulo 0 = 0
normalizaAngulo a = if a> 360 then a-360
                    else ( if (a<0) then  a + 360
                           else ( if (a>360 || a <0) then normalizaAngulo a
                                else a)
                           )
                    

-- ** Funções sobre listas.

-- | Devolve o elemento num dado índice de uma lista.
--
-- __Sugestão:__ Não use a função (!!) :: [a] -> Int -> a :-)
encontraIndiceLista :: Int -> [a] -> a
encontraIndiceLista i (h:t) = if (length (h:t)- i)== length (h:t) && i<length (h:t) then h
                              else encontraIndiceLista (i-1) t

--  Modifica um elemento num dado índice.
--
-- | __NB:__ Devolve a própria lista se o elemento não existir.
atualizaIndiceLista :: Int -> a -> [a] -> [a]
atualizaIndiceLista 0 n (h:t) = (n:t)
atualizaIndiceLista x n (h:t) = if (eIndiceListaValido x (h:t)== False) then error "Não existe o índice que inseriu"
                                else h: (atualizaIndiceLista (x-1) n t) 

-- ** Funções sobre matrizes.

-- | Devolve o elemento numa dada 'Posicao' de uma 'Matriz'.
encontraPosicaoMatriz :: PosicaoMatriz -> Matriz a -> a
encontraPosicaoMatriz (a,b) ((h:t):t1) = if ePosicaoMatrizValida (a,b) ((h:t):t1) == True then encontraIndiceLista b (encontraIndiceLista a ((h:t):t1))
                                         else error "Nao existe esse elemento" 


-- | Modifica um elemento numa dada 'Posicao'
--
-- | __NB:__ Devolve a própria 'Matriz' se o elemento não existir.
atualizaPosicaoMatriz :: PosicaoMatriz -> a -> Matriz a -> Matriz a
atualizaPosicaoMatriz (x,y) n [] = []
atualizaPosicaoMatriz (0,y) n (h:t) = (atualizaIndiceLista y n h) : t
atualizaPosicaoMatriz (x,y) n (h:t) = h : atualizaPosicaoMatriz (x-1,y) n t


-- | Relatório da Tarefa 0
-- | Esta tarefa teve um desolvimento relativamente rápido. Ambos os membros do grupo desenvolveram funções para esta de modo equilibrado. 