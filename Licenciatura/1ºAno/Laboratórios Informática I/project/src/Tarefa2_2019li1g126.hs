-- | Este módulo define funções comuns da Tarefa 2 do trabalho prático.
module Tarefa2_2019li1g126 where

import LI11920
import Control.Concurrent
import Tarefa0_2019li1g126
-- * Testes

-- | Testes unitários da Tarefa 2.
--
-- Cada teste é um triplo (/identificador do 'Jogador'/,/'Jogada' a efetuar/,/'Estado' anterior/).
testesT2 :: [(Int,Jogada,Estado)]
testesT2 = [(0,Movimenta B, (Estado mapap [Jogador 1 0 0 0 (Chao True),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta C, (Estado mapap [Jogador 1 0.5 0 0 (Chao True),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta C, (Estado mapap [Jogador 1 0.5 0 0 (Chao True),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta C, (Estado mapap [Jogador 0 1 0 0 (Ar 0 0 0),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta B,(Estado mapap [Jogador 0 1 0 0 (Ar 0 0 0),Jogador 0 0 0 5 (Chao False)])),
           (0,Dispara,(Estado mapap [Jogador 0 0 0 1 (Chao True),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta E, (Estado mapap [Jogador 0 0 0 0 (Ar 4.5 90.0 0.0),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta E,(Estado mapap [Jogador 0 0 0 0 (Ar 4.5 50.0 0.0),Jogador 0 0 0 5 (Chao False)])),
           (0,Movimenta D,(Estado mapap [Jogador 0 0 0 0 (Ar 4.5 285.0 0),Jogador 0 0 0 5 (Chao False)]))
            ]
      where mapap=[[Recta Terra 0,Recta Terra 0, Rampa Lama 0 2,Recta Lama 2,Rampa Relva 2 0,Recta Relva 0, Rampa Boost 0 1,Rampa Terra 1 0,Recta Terra 0 ,Recta Boost 0],[Recta Terra 0,Rampa Terra 0 1,Recta Boost 1,Recta Terra 1,Recta Terra 1,Recta Relva 1,Recta Terra 1,Rampa Relva 1 0,Recta Boost 0,Recta Terra 0]]
-- * Funções principais da Tarefa 2.

-- | Efetua uma jogada. 
--
  --   jogada :: Int -- ^ O identificador do 'Jogador' que efetua a jogada.
    --   -> Jogada -- ^ A 'Jogada' a efetuar.
      -- -> Estado -- ^ O 'Estado' anterior.
       -- -> Estado -- ^ O 'Estado' resultante após o jogador efetuar a jogada.
       

-- | trata-se da funcao principal, em que dividi em casos para facilitar o codigo e compreensao
jogada :: Int -> Jogada -> Estado -> Estado -- ^ O 'Estado' resultante após o jogador efetuar a jogada.
jogada njogador Dispara (Estado mapa jogs) = jogadaDispara    njogador (a) (Estado mapa jogs)
                    where a = encontraIndiceLista njogador jogs
jogada njogador Acelera (Estado mapa jogs) = jogadaAcelera    njogador (a) (Estado mapa jogs)
                     where a = encontraIndiceLista njogador jogs
jogada njogador Desacelera (Estado mapa jogs) = jogadaDesacelera njogador (a) (Estado mapa jogs)
                      where a = encontraIndiceLista njogador jogs
jogada njogador (Movimenta x) (Estado mapa jogs )= jogadaMovimenta x njogador (a) (Estado mapa jogs) 
                                       where a = encontraIndiceLista njogador jogs



--jogador1 = Jogador 0 2.3 0 5 (Chao True)
--jogador2 = Jogador 1 0   0 5 (Chao False)


{--
data Jogador--   x                     y                         z                               t               m
    = Jogador { pistaJogador :: Int, distanciaJogador :: Double, velocidadeJogador :: Double, colaJogador :: Int, estadoJogador :: EstadoJogador }
  deriving (Read,Show,Eq)--}

--Quando um jogador morre, a mota para instantaneamente e o seu timeout para renascer é inicializado a 1.0. 

-- | esta funcao é a funcao jogada dispara, recebe um inteiro (o índice do jogador), o jogador (os dados do jogador), e o estado do mapa e da lista de jogadores, retornando esse estado ja atualizado
-- | a funcao dispara analisa os dados do Jogador, nomeadamente se o numero de colas tem é maior que zero,para que possa disparar e se nao esta na primeira peca, pq nao pode colar um piso que nao existe- se sim entao dispara e perde uma municao, se nao nao dispara e retorna o estado do mapa

jogadaDispara:: Int -> Jogador->Estado->Estado
jogadaDispara njogador (Jogador x y z t m) (Estado mapa jogs) | x==0 = (Estado mapa jogs)
                                                              |t>0 && ((m==(Chao True))||(m==(Chao False))) = (Estado (atualizaPosicaoMatriz (x,floor(y-1)) peca mapa) (atualizaIndiceLista njogador (Jogador x y z (t-1) m) jogs))
                                                              |otherwise =(Estado mapa (atualizaIndiceLista njogador (Jogador x y z 0 m) jogs))
                                  where peca =identificaColaPecaAnterior njogador (Jogador x y z t m) (Estado mapa jogs)
-- | esta funcao é a que converte o piso para cola
colaPiso::Peca-> Peca
colaPiso (Recta x y) = (Recta Cola y)
colaPiso (Rampa x y z) = (Rampa Cola y z)


-- | esta funcao é a identificaColaPeca Anterior,vai buscar a peca anterior quando um jogador dispara e converte o piso dessa peca para Cola
identificaColaPecaAnterior:: Int-> Jogador ->Estado-> Peca
identificaColaPecaAnterior njogador (Jogador x y z t m) (Estado mapa jogs) = colaPiso(encontraPosicaoMatriz (x,floor(y-1)) mapa)                                                          

-- | esta funcao identifica a peca anterior (era necessaria para a funcao movimenta)
identificaPecaAnterior::Int->Jogador->Estado ->Peca
identificaPecaAnterior njogador (Jogador 0 0 z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (0,0) mapa)
identificaPecaAnterior njogador (Jogador x 0 z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (x, 0) mapa)
identificaPecaAnterior njogador (Jogador 0 y z t m) (Estado mapa jogs) = if y<=1 then (encontraPosicaoMatriz (0,0) mapa)
                                                                        else (encontraPosicaoMatriz (0,floor (y-1)) mapa)
identificaPecaAnterior njogador (Jogador x y z t m) (Estado mapa jogs) = if y<=1 then (encontraPosicaoMatriz (x,0) mapa)
                                                                        else (encontraPosicaoMatriz (x,floor(y-1)) mapa) 

-- | esta funcao identifica a peca atual (necessaria para o movimenta)
identificaPecaAtual::Int->Jogador->Estado ->Peca
identificaPecaAtual njogador (Jogador 0 0 z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (0,0) mapa)
identificaPecaAtual njogador (Jogador x 0 z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (x,0) mapa)
identificaPecaAtual njogador (Jogador 0 y z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (0,(floor y)) mapa)
identificaPecaAtual njogador (Jogador x y z t m) (Estado mapa jogs) = (encontraPosicaoMatriz (x,(floor y)) mapa) 

-- | esta é a jogadaAcelera, esta recebe um indice, ou seja, qual dos jogadores esta a efetuar esta jogada, depois recebe a informacao deste ,e o estado do mapa e dos jogadores, retornando o estado do mapa e da lista dos jogadores atualizado caso o jogador acelere
jogadaAcelera::Int -> Jogador->Estado-> Estado
jogadaAcelera njogador (Jogador x y z t (Chao False)) (Estado mapa jogs ) = Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Chao True)) jogs)
jogadaAcelera j e f = f

-- | esta é a jogadaDesacelera, faz exatamente, em termos de raciocinio, o que a jogadaAcelera faz, so que as informacoes e os detalhes estao ajustados pois trata se do Desacelera
jogadaDesacelera::Int -> Jogador->Estado->Estado
jogadaDesacelera njogador (Jogador x y z t (Chao True)) (Estado mapa jogs ) = Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Chao False)) jogs)
jogadaDesacelera j e f = f

-- | l altura, m inclinacao e n é gravidade
-- | para a esquerda +15graus
-- | para a direita -15graus
-- | a inclinacao maxima é 90 e a minima é -90 graus


-- | a  difalturas interpreta duas pecas dadas pelas funcoes identificaPecaAnterior e identificaPecaAtual para fazer a subtracao das alturas destas, que lhe é dada por uma outra funcao
difalturas:: Peca->Peca-> Jogador -> Double

difalturas (Recta piso1 altura1) (Recta piso altura2) (Jogador pista x v c e)=((fi altura2)-(fi altura1))+ (0*(fi c))
                                                where fi=fromIntegral
difalturas (Recta piso altura1) (Rampa piso2 alturai alturaf) (Jogador pista x v c e) =((alturaRampa (Rampa piso2 alturai alturaf) (Jogador pista x v c e) )- (fi altura1))   
                                                where fi=fromIntegral
difalturas (Rampa piso2 alturai alturaf) (Recta piso altura1) (Jogador pista x v c e) =((alturaRampa (Rampa piso2 alturai alturaf) (Jogador pista x v c e) )- (fi altura1))
                                                where fi=fromIntegral
difalturas b@(Rampa piso2 alturai alturaf) a@(Rampa piso1 alturain alturafi) (Jogador pista x v c e) =((alturaRampa a (Jogador pista x v c e))-(alturaRampa b (Jogador pista x v c e)))
                                                where fi=fromIntegral

-- | a altura de retas é bastante intuitiva portanto nao foi necessario criar uma funcao auxiliar para tal, mas no caso das rampas torna se mais dificil, e portanto criei esta funcao,visualizando uma rampa como se fosse uma reta num grafico cartesiano, em que o b é a altura inicial, o a a diferenca entre a alturafinal e a altura inicial e o x, vem dos detalhes do jogador, que é a sua distancia
alturaRampa::Peca-> Jogador-> Double
alturaRampa (Rampa piso alturai alturaf) (Jogador pista x v c e) = ((fi m)*((x)-(fi (floor x)))-(fi b))
                                          where m = alturaf-alturai
                                                b = alturai
                                                fi=fromIntegral
                    
-- | a altura peca serve para determinar a altura de uma determinada peca
alturaPeca:: Peca -> Jogador-> Double
alturaPeca (Rampa piso alturai alturaf) (Jogador pista x v c e) = ((fi m)*((x)-(fi (floor x)))-(fi b))
                                          where m = alturaf-alturai
                                                b = alturai
                                                fi=fromIntegral
alturaPeca (Recta piso alturai) (Jogador pista x v c e) = fi(alturai) * (0*(fi c))
             where fi=fromIntegral

-- | esta funcao serve para determinar a inclinacao de uma dada peca
inclinacaoPeca::Peca  -> Double
inclinacaoPeca (Recta piso altura)= 0
inclinacaoPeca (Rampa piso alturai alturaf) | alturaf>alturai = normalizaAngulo (((atan (fromIntegral alturaf))*180)/pi)
                                            | alturai>alturaf = normalizaAngulo (((atan (fromIntegral alturai))*180)/pi)



-- | esta funcao serve para analisar o comportamento e movimento de um jogador e adequadamente escolhe a consequencia para tal
jogadaMovimenta::Direcao->Int->Jogador->Estado -> Estado
jogadaMovimenta E njogador (Jogador x y z t (Ar l m n)) (Estado mapa jogs)  |(m==90) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (90) n )) jogs))
                                                                            | ((m>=(-90))||(m<=75)) =  (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (am+15) n)) jogs))
                                                                            | ((m>=270)||(m<360)) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l ((m-360)) n )) jogs))
                                                                            |((am<(-90))||(am>75))  =  (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l 90 n )) jogs))
                                                                            |otherwise= (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l 90 n )) jogs))
                                                                                where am = normalizaAngulo m

jogadaMovimenta D njogador (Jogador x y z t (Ar l m n)) (Estado mapa jogs)  | ((m>=(-75))||(m<=90)) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (am-15) n)) jogs))
                                                                            | (m==(-90)) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (-90) n )) jogs))
                                                                            | ((m<(-270)) || (m<360)) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l ((m+360)) n)) jogs))
                                                                            | ((m<(-75))||(m>90)) =  (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (-90) n)) jogs))
                                                                            | otherwise= (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l (-90) n))  jogs))
                                                                                where am = normalizaAngulo m

jogadaMovimenta B njogador f@(Jogador x y z t (Chao v))   (Estado mapa jogs)|  x>=((length mapa )-1) = (Estado mapa jogs)
                                                                            |(y < (fromIntegral (length mapa))) && x==0  && (abs (difalturas pecaatual pecaanterior f))>0.2 && (difalturas pecaatual pecaanterior f)<0 = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Ar l m 0)) jogs) )
                                                                            |(y < (fromIntegral (length mapa))) && x==0  && (abs (difalturas pecaatual pecaanterior f))>0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Morto 1.0)) jogs) )
                                                                            |(y < (fromIntegral (length mapa))) && x==0 && (abs (difalturas pecaatual pecaanterior f))<=0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x+1) y z t (Chao v)) jogs))
                                                                            |(y < (fromIntegral (length mapa))) && (abs(difalturas pecaatual pecaanterior f))<=0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x+1) y z t (Chao v)) jogs))
                                                                            |(y < (fromIntegral (length mapa))) && (abs (difalturas pecaatual pecaanterior f))>0.2 && (difalturas pecaatual pecaanterior f)<0 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x+1) y z t (Ar l m 0)) jogs))
                                                                            |(y < (fromIntegral (length mapa))) && (abs(difalturas pecaatual pecaanterior f))>0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Morto 1.0)) jogs))
                                                                            |(y < (fromIntegral (length mapa))) && x==1 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x+1) y z t (Chao v)) jogs))
                                                                            |otherwise = (Estado mapa jogs)
                                                                                 where pecaatual = identificaPecaAtual njogador (Jogador x y z t (Chao v)) (Estado mapa jogs)
                                                                                       pecaanterior = identificaPecaAnterior njogador (Jogador x y z t (Chao v)) (Estado mapa jogs)
                                                                                       l = alturaPeca pecaatual (Jogador x y z t (Chao v))
                                                                                       m = inclinacaoPeca pecaatual

jogadaMovimenta C njogador f@(Jogador x y z t (Chao v))   (Estado mapa jogs) |(y < (fromIntegral (length mapa))) && x>=1 && (abs(difalturas pecaatual pecaanterior f ))<=0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x-1) y z t (Chao v)) jogs))
                                                                             |(y < (fromIntegral (length mapa))) && x>=1 && (abs (difalturas pecaatual pecaanterior f))>0.2 && (difalturas pecaatual pecaanterior f)<0 = (Estado mapa (atualizaIndiceLista njogador (Jogador (x-1) y z t (Ar l m 0)) jogs ))
                                                                             |(y < (fromIntegral (length mapa))) && (abs(difalturas pecaatual pecaanterior f))>0.2 = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Morto 1.0)) jogs))
                                                                             |(y < (fromIntegral (length mapa))) && x==0 = (Estado mapa jogs)
                                                                             |otherwise = (Estado mapa jogs)
                                                                                 where pecaatual = identificaPecaAtual njogador (Jogador x y z t (Chao v)) (Estado mapa jogs)
                                                                                       pecaanterior = identificaPecaAnterior njogador (Jogador x y z t (Chao v)) (Estado mapa jogs)
                                                                                       l = alturaPeca pecaatual (Jogador x y z t (Chao v))
                                                                                       m = inclinacaoPeca pecaatual

jogadaMovimenta _ njogador (Jogador x y z t (Ar 0 m n)) (Estado mapa jogs) = (Estado mapa jogs)
jogadaMovimenta _ njogador (Jogador x y z t (Ar l m 0)) (Estado mapa jogs) = (Estado mapa (atualizaIndiceLista njogador (Jogador x y z t (Morto 1.0)) jogs))
jogadaMovimenta _ njogador (Jogador x y z t (Morto est)) (Estado mapa jogs)=  (Estado mapa jogs)
jogadaMovimenta _ njogador (Jogador x y z t (Ar l m n))  (Estado mapa jogs) = (Estado mapa jogs)
jogadaMovimenta _ njogador (Jogador x y z t (Chao j)) (Estado mapa jogs) = (Estado mapa jogs)




-- | Relatório da Tarefa 2:
-- | Esta tarefa revelou-se exigente porque continua a dar erros em certos casos (passa os jogadores como Mortos quando estes não o deveriam ser e vice-versa) que nos é díficl perceber e entender.
-- | Assim, perdemos muito tempo no desenvolvimento e correção desta, atrasando assim a tarefa 6 e 5.