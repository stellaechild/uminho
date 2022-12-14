-- | Este módulo define funções comuns da Tarefa 4 do trabalho prático.
module Tarefa4_2019li1g126 where

import LI11920
import Tarefa0_2019li1g126
import Tarefa2_2019li1g126


-- * Testes
-- | Testes unitários da Tarefa 4.
--
-- Cada teste é um par (/tempo/,/'Mapa'/,/'Jogador'/).
testesT4 :: [(Double,Mapa,Jogador)]
testesT4 = [((0.5),mapap, Jogador 1 0 0.2 0 (Chao True)),
            ((0.6),mapap, Jogador 0 1 0.5 0 (Ar 2 0 0)),
            ((0.5),mapap, Jogador 0 0 0.2 0 (Ar 4.5 50.0 0.0)),
            ((0.6),mapal, Jogador 0 0.2 2 0 (Ar 0.5 (-40) 0)),
            ((0.6),mapap, Jogador 0 0.9 3 1 (Chao True)),
            ((0.6),mapap, Jogador 0 0 0 6 (Chao True)),
            ((0.5),mapap, Jogador 1 0.5 0.2 0 (Chao True))]
             where mapap= [[Recta Terra 0,Recta Terra 0, Rampa Lama 0 2,Recta Lama 2,Rampa Relva 2 0,Recta Relva 0, Rampa Boost 0 1,Rampa Terra 1 0,Recta Terra 0 ,Recta Boost 0],[Recta Terra 0,Rampa Terra 0 1,Recta Boost 1,Recta Terra 1,Recta Terra 1,Recta Relva 1,Recta Terra 1,Rampa Relva 1 0,Recta Boost 0,Recta Terra 0]]
                   mapal= [[Recta Terra 0,Recta Terra 0,Recta Terra 0]]


-- * Funções principais da Tarefa 4.

-- | Avança o estado de um 'Jogador' um 'passo' em frente, durante um determinado período de tempo.

{- passo :: Double -- ^ O tempo decorrido.
  --   -> Mapa    -- ^ O mapa utilizado.
    -- -> Jogador -- ^ O estado anterior do 'Jogador'.
    -- -> Jogador -- ^ O estado do 'Jogador' após um 'passo'.
-}
-- | funcao predefinida por docentes
passo :: Double->Mapa->Jogador->Jogador
passo t m j = move t m (acelera t m j)

-- | Altera a velocidade de um 'Jogador', durante um determinado período de tempo.
--acelera :: Double -- ^ O tempo decorrido.
  --   -> Mapa    -- ^ O mapa utilizado.
    -- -> Jogador -- ^ O estado anterior do 'Jogador'.
     -- -> Jogador -- ^ O estado do 'Jogador' após acelerar.
acelera:: Double -> Mapa -> Jogador -> Jogador
acelera t mapa (Jogador p d v c (Chao e))   = velocidadeChao (Jogador p d v c (Chao e)) t mapa 
acelera t mapa (Jogador p d v c (Ar a i g)) = velocidadeAr (Jogador p d v c (Ar a i g)) t
-- | Altera a posição de 'Jogador', durante um determinado período de tempo.
--move :: Double -- ^ O tempo decorrido.
  --   -> Mapa    -- ^ O mapa utilizado.
    -- -> Jogador -- ^ O estado anterior do 'Jogador'.
     -- -> Jogador -- ^ O estado do 'Jogador' após se movimentar.
move:: Double->Mapa->Jogador->Jogador     
move t mapa (Jogador p d v c (Morto x)) |(x-t)>0   = (Jogador p d v c (Morto (x-t)))
                                        |otherwise = (Jogador p d 0 c (Chao False))

move t mapa ppp@(Jogador p d v c (Chao e))  | d==0 || ((inclinacao proximapeca >= inclinacao peca) && ((limitepeca t ppp)== False)) = (Jogador p dist v c (Chao e))
                                            |((inclinacao proximapeca >= inclinacao peca) || ((limitepeca t ppp)== True))  = (Jogador p (fromIntegral(ceiling(d))) v c (Chao e))
                                            |otherwise = (Jogador p d v c (Ar a i 0))

                                    where peca = identifica (Jogador p d v c (Chao e)) mapa
                                          proximapeca = identificaproxima (Jogador p d v c (Chao e)) mapa
                                          a = alturaPeca peca (Jogador p d v c (Chao e))
                                          i = inclinacao peca
                                          dist= d+v*t


move t mapa jjj@(Jogador p d v c (Ar a i g)) | (((limitepeca t jjj)==True) && (calculaSeInterseta t peca mapa jjj == True)) && (abs(normalizaAngulo(i-fff)))<=45 = (Jogador p distatualizada1 v c (Chao False))
                                             | (((limitepeca t jjj)==True) && (calculaSeInterseta t peca mapa jjj == True)) && (abs(normalizaAngulo(i-fff))) >45 = (Jogador p distatualizada1 0 c (Morto 1.0))
                                             | aatualiza<0 = (Jogador p distatualizada1 v c (Chao False))
                                             | ((limitepeca t jjj)==True) = (Jogador p distatualizada v c (Ar aatualiza i g))
                                             | ((limitepeca t jjj)==False) = (Jogador p distatualizada v c (Ar aatualiza i g))
                                             

                            where proximapeca = identificaproxima (Jogador p d v c (Ar a i g )) mapa
                                  fff= inclinacao proximapeca
                                  distatualizada1 = posx(sabeIntersecao t peca mapa jjj)
                                  distatualizada  = d+v*t*(cos ((i*pi)/180))
                                  aatualiza       = a+(-g)*t+v*t*(sin ((i*pi)/180))
                                  peca = identifica (Jogador p d v c (Ar a i g)) mapa
                                  
--------------------------------------
--quando a mota sai da rampa , tem velocidadeJogador , velocidade que continua a ter no ar mas que é diminuida pelo atrito
--quando rodam a mota, se apontar para o chao mantem na mesma a velocidade e o atrito
--temos que ir ao angulo, a inclinacao, velocidade
--como a gravidade puxa pra baixo, a gravidade , conforme o temppo vai passando, a gravidade aumenta
--pegar no vetor velocidade e soma lo ao vetor gravidade
--multiplicar pelo tempo que passou e calculas a posicao onde ele vai cair 
------------------------------------------
-- | funcao que indica a altura final de uma peca
alturaFinal :: Peca -> Int
alturaFinal (Recta piso altura) = altura
alturaFinal (Rampa piso alturainicial alturaf) | (alturainicial >= 0) && (alturainicial < alturaf) = alturaf--(alturainicial+(peca+1))
                                               | (alturainicial >= 0) && (alturainicial > alturaf) = alturainicial--(alturainicial-(peca-1))
                                               | otherwise = 0

-- | funcao que calcula se a trajetoria de um jogador vai intersetar com o chao num determinado tempo
calculaSeInterseta::Double-> Peca ->Mapa-> Jogador-> Bool
calculaSeInterseta t  (Recta p h ) mapa d@(Jogador x y v c (Ar a i g))  = intersetam (Cartesiano (fromIntegral(floor (y-1))) (fromIntegral h),Cartesiano (fromIntegral(floor (y))) (fromIntegral h)) (Cartesiano y  a, Cartesiano p a2)

     where air = alturaInicial (identificaproxima d mapa)
           afr = alturaFinal (identificaproxima d mapa) 
           af  = alturaFinal (identifica d mapa)
           ai  = alturaInicial (identifica d mapa)
           ip  = inclinacao proximapeca
           ipr = inclinacao peca
           a2  = a+(-g)*t+v*t*(sin ((i*pi)/180))
           p   = y+v*t*(cos ((i*pi)/180))  -- y +(v*t)
           peca = identifica (Jogador x y v c (Ar a i g)) mapa
           proximapeca = identificaproxima (Jogador x y v c (Ar a i g)) mapa

calculaSeInterseta t (Rampa p hi hf) mapa d@(Jogador x y v c (Ar a i g)) = intersetam (Cartesiano (fromIntegral(floor (y-1))) (fromIntegral (hi)),Cartesiano (fromIntegral(floor (y))) (fromIntegral (hf))) (Cartesiano  y  a, Cartesiano p a2)

     where air = alturaInicial (identificaproxima d mapa)
           afr = alturaFinal (identificaproxima d mapa) 
           af  = alturaFinal (identifica d mapa)
           ai  = alturaInicial (identifica d mapa)
           ip  = inclinacao proximapeca
           ipr = inclinacao peca
           a2  = a+(-g)*t+v*t*(sin ((i*pi)/180))
           p   = y+v*t*(cos ((i*pi)/180))  -- y +(v*t)
           peca = identifica (Jogador x y v c (Ar a i g)) mapa
           proximapeca = identificaproxima (Jogador x y v c (Ar a i g)) mapa
           

-- | esta funcao utiliza a funcao calculaIntersecao para saber se tal é verdade e depois, prossegue à determinacao dessas novas coordenadas
sabeIntersecao::Double->Peca->Mapa->Jogador->Ponto
sabeIntersecao t q@(Rampa p hi hf) mapa k@(Jogador x y v c (Ar a i g)) | ((calculaSeInterseta t q mapa k) == True) = intersecao (Cartesiano y g,Cartesiano p a2) (Cartesiano (fromIntegral air) ipr, Cartesiano (fromIntegral afr) ipr)
                                                                       | otherwise = intersecao (Cartesiano y g, Cartesiano p a2) (Cartesiano (fromIntegral hi) (0.0) , Cartesiano (fromIntegral hf) (40.6) )

     where air = alturaInicial (identificaproxima k mapa)
           afr = alturaFinal (identificaproxima k mapa) 
           af  = alturaFinal (identifica k mapa)
           ai  = alturaInicial (identifica k mapa)
           ip  = inclinacao proximapeca
           ipr = inclinacao peca
           a2  = a+(-g)*t+v*t*(sin ((i*pi)/180))
           p   = y+v*t*(cos ((i*pi)/180)) 
           peca = identifica (Jogador x y v c (Ar a i g)) mapa
           proximapeca = identificaproxima (Jogador x y v c (Ar a i g)) mapa

sabeIntersecao t l@(Recta p h) mapa k@(Jogador x y v c (Ar a i g)) | ((calculaSeInterseta t l mapa k) == True) = intersecao (Cartesiano y g,Cartesiano p a2) (Cartesiano (fromIntegral air) ipr, Cartesiano (fromIntegral afr) ipr)
                                                                   | otherwise = intersecao (Cartesiano y g, Cartesiano p a2) (Cartesiano (fromIntegral h) (0.0) , Cartesiano (fromIntegral h) (40.6))
     where air = alturaInicial (identificaproxima k mapa)
           afr = alturaFinal (identificaproxima k mapa) 
           af  = alturaFinal (identifica k mapa)
           ai  = alturaInicial (identifica k mapa)
           ip  = inclinacao proximapeca
           ipr = inclinacao peca
           a2  = a+(-g)*t+v*t*(sin ((i*pi)/180))
           p   = y+v*t*(cos ((i*pi)/180)) 
           peca = identifica (Jogador x y v c (Ar a i g)) mapa
           proximapeca = identificaproxima (Jogador x y v c (Ar a i g)) mapa



-- | esta funcao decide se um jogador ultrapassa , com o tempo dado, se estara na peca seguinte
pecaresultante :: Double -> Jogador -> Bool
pecaresultante t (Jogador p u v c (Ar a i g)) = if ((u+(v*t))>(u+1)) then True else False 


-- | esta funcao esta definida para identificar se no tempo dado, o jogador ultrapassa o limite peca
limitepeca:: Double->Jogador -> Bool
limitepeca t (Jogador p u v c (Chao e))   | (floor(u))==(fromIntegral (ceiling(u))) = True
                                          | (u+v*t)>=(fromIntegral (ceiling(u))) = True
                                          | otherwise = False

limitepeca t (Jogador p u v c (Ar a i g)) | (floor(u))==(fromIntegral (ceiling(u))) = True
                                          | (u+v*t)>=(fromIntegral(ceiling(u))) = True
                                          | otherwise = False


-- | esta funcao defne a altura de uma peca
alturas:: Peca -> Jogador-> Double
alturas (Rampa piso alturai alturaf) (Jogador pista x v c e) = ((fi m)*((x)-(fi (floor x)))-(fi b))
                                          where m = alturaf-alturai
                                                b = alturai
                                                fi=fromIntegral
alturas (Recta piso alturai) (Jogador pista x v c e) = fi(alturai) * (0*(fi c))
             where fi=fromIntegral




--Chao: movimenta-se com velocidade constante na superfície da peça pelo período de tempo t em questão ou até chegar ao limite desta.
-- Se atingir o limite da peça, o jogador passa para a próxima peça e fica no Chao
-- com o mesmo estado de aceleraJogador (se a inclinação da próxima peça for maior ou igual que a inclinação da peça atual) 
-- ou no Ar com inclinacaoJogador igual à direção da peça atual e velocidade gravidadeJogador a zero 
--(se a inclinação da próxima peça for menor que a inclinação da peça atual).
 --Em qualquer dos casos, velocidadeJogador é preservada










-- | A atualização da velocidade do jogador está dividida em dois casos: 1) quando o jogador está no chão e 2) quando o jogador está no ar.
-- | No primeiro caso, a nova velocidade (v') do jogador é calculada através da seguinte fórmula:

{-
data Jogador--   p                     d                         v                               c               e
    = Jogador { pistaJogador :: Int, distanciaJogador :: Double, velocidadeJogador :: Double, colaJogador :: Int, estadoJogador :: EstadoJogador }
  deriving (Read,Show,Eq) -}
-- t é o instante de tempo em questao
velocidadeChao::Jogador -> Double ->Mapa -> Jogador
velocidadeChao (Jogador p d v c (Chao e)) t mapa  | a>=0 = Jogador p d a c (Chao e) 
                                                  | otherwise= Jogador p d 0 c (Chao e)

         where accelMota = if (v<2 && e==True) then 1 else 0
               peca = identifica (Jogador p d v c (Chao e)) mapa
               a= (v  + ((accelMota-((atrito peca)*v))*t))



-- | No segundo caso (i.e., quando o jogador está no ar) além da nova velocidade,
-- | é preciso atualizar a velocidade causada pela gravidade.

-- | A nova velocidade do jogador (v') é calculada da seguinte maneira:
velocidadeAr:: Jogador -> Double-> Jogador
velocidadeAr  (Jogador p d v c (Ar l i g)) t  | a>=0 = Jogador p d a c (Ar l i b)
                                              | a<0 =  Jogador p d 0 c (Ar l i b)

                               where a=v - (resistenciaAr * v * t)
                                     resistenciaAr = 0.125
                                     accelGravidade = 1
                                     b = g + (accelGravidade * t)



--identificaproxima (Jogador 0 0 z t m) mapa  = (encontraPosicaoMatriz (1,2) mapa)
--identificaproxima (Jogador 0 y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (1,2) mapa) else  (encontraPosicaoMatriz (1,(floor y)+1) mapa)
--identificaproxima (Jogador x y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (x,2) mapa) else  (encontraPosicaoMatriz (x,(floor y)+1) mapa)

--identifica::Jogador->Mapa->Peca
--identifica (Jogador 0 0 z t m) mapa  = (encontraPosicaoMatriz (1,1) mapa)
--identifica (Jogador 0 y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (1,1) mapa) else (encontraPosicaoMatriz (1,(floor y)) mapa)
--identifica (Jogador x y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (x,1) mapa) else (encontraPosicaoMatriz (x,(floor y)) mapa) 
-- | esta funcao identifica qual sera a seguinte peca na mesma pista
identificaproxima::Jogador-> Mapa ->Peca
identificaproxima (Jogador 0 0 z t m) mapa = (encontraPosicaoMatriz (0,1) mapa)
identificaproxima (Jogador x 0 z t m) mapa = (encontraPosicaoMatriz (x,1) mapa)
identificaproxima (Jogador 0 y z t m) mapa = if y<=1 then (encontraPosicaoMatriz (0,1) mapa)
                                                                        else (encontraPosicaoMatriz (0,floor (y+1)) mapa)
identificaproxima (Jogador x y z t m) mapa = if y<=1 then (encontraPosicaoMatriz (x,1) mapa)
                                                                        else (encontraPosicaoMatriz (x,floor(y+1)) mapa) 

-- | esta funcao identifica a peca atual (necessaria para o movimenta), e identifica a peca em que este se movimenta de momento
identifica::Jogador->Mapa ->Peca
identifica (Jogador 0 0 z t m) mapa  = (encontraPosicaoMatriz (0,0) mapa)
identifica (Jogador x 0 z t m) mapa  = (encontraPosicaoMatriz (x,0) mapa)
identifica (Jogador 0 y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (0,0) mapa) else (encontraPosicaoMatriz (0,(floor y)) mapa)
identifica (Jogador x y z t m) mapa  = if y<=1 then (encontraPosicaoMatriz (x,0) mapa) else (encontraPosicaoMatriz (x,(floor y)) mapa) 

-- | funcao definida para  definir a altura de uma peca
alturaInicial :: Peca -> Int
alturaInicial (Recta piso altura) = altura
alturaInicial (Rampa piso alturainicial alturaf) | (alturainicial >= 0) && (alturainicial < alturaf) = alturainicial--(alturainicial+(peca+1))
                                                 | (alturainicial >= 0) && (alturainicial > alturaf) = alturaf--(alturainicial-(peca-1))
                                                 | otherwise = 0


-- | funcao definida para identificar a inclinacao de uma dada peca
inclinacao::Peca  -> Double
inclinacao (Recta piso altura)= 0
inclinacao (Rampa piso alturai alturaf) | alturaf>alturai = normalizaAngulo (((atan (fromIntegral alturaf))*180)/pi)
                                        | alturai>alturaf = normalizaAngulo (((atan (fromIntegral alturai))*180)/pi)


-- | esta funcao devolve os valores de atrito associado a cada piso
atrito::Peca->Double
atrito (Rampa Lama _ _)  = 1.50
atrito (Rampa Relva _ _) = 0.75
atrito (Rampa Terra _ _) = 0.25
atrito (Rampa Boost _ _) = (-0.50)
atrito (Rampa _ _ _)     = 3.00
atrito (Recta Lama _ )   = 1.50
atrito (Recta Relva  _)  = 0.75
atrito (Recta Terra _)   = 0.25
atrito (Recta Boost _)   = (-0.50)
atrito (Recta _ _)       = 3.00


--Morto: se a diferencia do timeoutJogador menos o tempo t for maior que 0, apenas o devemos decrementar pelo tempo t em questão. 
--Caso contrario, o estado do jogador é alterado para Chao False mantendo a velocidade a zero.



-- | Relatório relativo à Tarefa 4:
-- | Introdução: A Tarefa 4 é uma tarefa relativa ao movimento de um jogador, tendo em conta um dado tempo e a sua velocidade.

-- | Objetivos: O nosso objetivo era realizar esta tarefa de modo a que todos os parâmetros e regras impostas pelos professores no enunciado tenham sido cumpridas a 100%.

-- | Discussão e Conclusão: A Tarefa 4 revelou-se especialmente difícil nos casos em que o jogador se encontra no Ar e irá intersetar com a peça. Este caso em particular deu-nos bastantes problemas.
-- | Implementamos duas funções diferentes: uma que nos informa se o Jogador interseterá com o mapa ou não e a outra que calcula os pontos e as posições nas quais o jogador ficará caso tal aconteça. 
-- | No entanto,este caso na nossa função não é reconhecido, e lança um erro a dizer que as retas escolhidas não se intersetam, apesar de o fazerem. Assim, como tinhamos perdido bastante tempo a corrigir a Tarefa 2 e como tal também se sucedeu para a Tarefa 4 ,decidimos aplicar-nos às restantes tarefas.



