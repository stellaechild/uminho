-- | Este módulo define funções comuns da Tarefa 6 do trabalho prático.
module Tarefa6_2019li1g126 where

import LI11920
import Tarefa2_2019li1g126
import Tarefa0_2019li1g126
import Tarefa1_2019li1g126
-- * Funções principais da Tarefa 6.

-- | Define um ro'bot' capaz de jogar autonomamente o jogo.
--bot :: Int          -- ^ O identificador do 'Jogador' associado ao ro'bot'.
--    -> Estado       -- ^ O 'Estado' para o qual o ro'bot' deve tomar uma decisão.
--    -> Maybe Jogada -- ^ Uma possível 'Jogada' a efetuar pelo ro'bot'.
bot :: Int-> Estado -> Maybe Jogada
bot n@njogador e@(Estado mapa jogs) | ((avaliaAr n (Jogador x y z t m) e) == Nothing ) && ((botAcelera n e)==Nothing) && ((botPiso n e)==Nothing) = analisaJogadores n e
                                    | ((avaliaAr n (Jogador x y z t m) e) == Nothing ) && ((botAcelera n e)==Nothing) = botPiso n e
                                    | ((avaliaAr n (Jogador x y z t m) e) == Nothing ) = botAcelera n e
                                    | otherwise = avaliaAr n (Jogador x y z t m) e
            
            where (Jogador x y z t m)= encontraIndiceLista njogador jogs


-- njogador (Estado mapa jogs)
--exemplo = 1 (Estado [[Recta Terra 0,Recta Terra 0, Rampa Lama 0 2,Recta Lama 2,Rampa Relva 2 0,Recta Relva 0, Rampa Boost 0 1,Rampa Terra 1 0,Recta Terra 0 ,Recta Boost 0],[Recta Terra 0,Rampa Terra 0 1,Recta Boost 1,Recta Terra 1,Recta Terra 1,Recta Relva 1,Recta Terra 1,Rampa Relva 1 0,Recta Boost 0,Recta Terra 0]] [Jogador 0 0 0 0 (Ar 4.5 285.0 0),Jogador 0 0 0 5 (Chao False)])


  --               where (Jogador x y z t m)= encontraIndiceLista njogador jogs
-- | esta função decide quando um bot deve acelerar, ou seja quando este tiver Chao False
botAcelera::Int->Estado->Maybe Jogada
botAcelera njogador (Estado mapa jogs) = if (Jogador x y z t m) == (Jogador x y z t (Chao False)) then Just Acelera
                                         else Nothing


          where (Jogador x y z t m)= encontraIndiceLista njogador jogs

{-
botDesacelera::Int->Double->Jogador->Maybe Jogada
botDesacelera t (Jogador x y z o (Chao g)) n@njogador e@(Estado mapa jogs) | ((limitepeca t (Jogador x (y+(z*t)) z o (Chao g))) == True) && (t<0.2) && ((botPiso n e)==Nothing) = Nothing
                                                                           | ((limitepeca t (Jogador x (y+(z*t)) z o (Chao g))) == True) && (t<0.2) = Just Desacelera 
                                                                           | otherwise= Nothing
-}

-- jogo acaba em 40s : ganhando o jogador que percorre maior distancia ou aquele que chegue à meta primeiro
-- executa jogada a cada 0.2s
-- jogada processada atraves das funcoes jogada e passo
-- cada jogador comeca com 4 municoes de cola
-- existe possibilidade de empates

--se prever que em  0.2s entrará em Lama, compare com a outra pista, se tiver relva mude para essa,se tiver cola nao mude
--se esta outra pista tiver lama, entao calcular quantas lamas esta tem em comparacao a outra
--se for menor e nao tiver outra pista que nao tenha menor quantidade de lamas (inclusive nunhma lamas) entao muda-se para essa pista

--se prever que em 0.2s entrará em Relva, compare com a outra pista, se esta tiver lama, permanece na relva, se tiver cola nao mude se tiver relva entao
--mesmas estrategia que fiz para a Lama, qualqur outro piso entao mude

--se prever que em 0.2s entrará em Colam entao compare ,se a sua volta tiver colas, entao calcule qual a pista que tenha menos destes pisos e mude para essa
--se tiver uma relva e outra lama, va para a relva
--se tiver boost ou terra, va para boost
--se tiver terra ou relva, va para terra

--prioridades
--BOOST>TERRA>RELVA>LAMA>COLA



-- | esta funcao é capaz de identificar qual o piso onde o bot se encontra de momento
identificaPiso::Jogador->Mapa ->Piso
identificaPiso (Jogador 0 0 z t m) mapa = segundocomponente (encontraPosicaoMatriz (0,0) mapa)  
identificaPiso (Jogador x 0 z t m) mapa = segundocomponente (encontraPosicaoMatriz (x,0) mapa)    
identificaPiso (Jogador 0 y z t m) mapa = segundocomponente (encontraPosicaoMatriz (0,(floor y)) mapa)
identificaPiso (Jogador x y z t m) mapa = if y<=1 then segundocomponente (encontraPosicaoMatriz (x,0) mapa) 
                                          else segundocomponente (encontraPosicaoMatriz (x,(floor y)) mapa)                                     

-- | esta funcao é capaz de identificar qual o piso onde o bot se encontraria se estivesse na pista diretamente abaixo
identificaPisoAbaixo::Jogador->Mapa->Piso
identificaPisoAbaixo (Jogador 0 0 z t m) mapa = segundocomponente (encontraPosicaoMatriz (1,0) mapa)  
identificaPisoAbaixo (Jogador x 0 z t m) mapa = segundocomponente (encontraPosicaoMatriz (x+1,0) mapa)    
identificaPisoAbaixo (Jogador 0 y z t m) mapa = segundocomponente (encontraPosicaoMatriz (1,(floor y)) mapa)
identificaPisoAbaixo (Jogador x y z t m) mapa = if y<=1 then segundocomponente (encontraPosicaoMatriz (1,0) mapa) 
                                                else segundocomponente (encontraPosicaoMatriz (x+1,(floor y)) mapa) 

-- | esta funcao é capaz de identificar qual o piso onde o bot se encontraria se estivesse na pista imediatamente acima, se tal for possivel , é claro
identificaPisoAcima::Jogador->Mapa->Piso
identificaPisoAcima (Jogador x 0 z t m) mapa = segundocomponente (encontraPosicaoMatriz (x-1,0) mapa)    
identificaPisoAcima (Jogador x y z t m) mapa = if y<=1 then segundocomponente (encontraPosicaoMatriz (0,0) mapa) 
                                                else segundocomponente (encontraPosicaoMatriz (x-1,(floor y)) mapa)

-- | esta funcao pega numa peca e decifra qual o piso nesta
segundocomponente::Peca->Piso
segundocomponente (Rampa i _ _)= i
segundocomponente (Recta j _)  = j 

-- | esta funcao é utilizada na funcao principal, é esta que avalia se o bot deve ou nao mudar de piso
botPiso::Int->Estado->Maybe Jogada
botPiso njogador (Estado mapa jogs) | x>=((length mapa )-1) && (avaliaPisos jog mapa) > x = Just (Movimenta B)
                                    | x>0 && ((avaliaPisos jog mapa) < x) = Just (Movimenta C)
                                    | otherwise = Nothing

                             where jog@(Jogador x y z t m) = encontraIndiceLista njogador jogs

-- | esta funcao avalia quais dos pisos é o mais eficiente, usando as funcoes identifica e a atribuiPontos
avaliaPisos:: Jogador -> Mapa-> Int
avaliaPisos jog@(Jogador x y z t m) mapa |((atribuiPontos (identificaPiso jog mapa)) >= (atribuiPontos (identificaPisoAcima jog mapa))) && ((atribuiPontos (identificaPiso jog mapa)) >= (atribuiPontos (identificaPisoAbaixo jog mapa))) = x
                                         |((atribuiPontos (identificaPiso jog mapa)) <= (atribuiPontos (identificaPisoAcima jog mapa))) && ((atribuiPontos (identificaPisoAcima jog mapa)) >= (atribuiPontos (identificaPisoAbaixo jog mapa))) = x+1
                                         |x>0 && ((atribuiPontos (identificaPiso jog mapa)) <= (atribuiPontos (identificaPisoAbaixo jog mapa))) && ((atribuiPontos (identificaPisoAcima jog mapa)) <= (atribuiPontos (identificaPisoAbaixo jog mapa))) = x-1



-- | esta funcao atribui valores aos pisos, o piso que tiver melhor cotacao, ou seja, maior valor, vai ter sempre prioridade aos outros
atribuiPontos::Piso->Int
atribuiPontos Boost = 5
atribuiPontos Terra = 4
atribuiPontos Relva = 3
atribuiPontos Lama  = 2
atribuiPontos Cola  = 1


-- | esta funcao avalia se o angulo em que o jogador esta no ar esta dentro dos parametros, e reage de maneira adequada para que o jogador nao morra quando atingir o chao, vai ser utilizada na funcao principal
avaliaAr:: Int->Jogador ->Estado->Maybe Jogada
avaliaAr njogador (Jogador x y z t (Ar a i g)) (Estado mapa jogs) | ((abs (normalizaAngulo ((inclinacaoPeca peca) - i ))) <=45) = Nothing
                                                                  |  i<l = Just (Movimenta D)
                                                                  | otherwise = Just (Movimenta E)

           where peca = identificaPecaAtual njogador (Jogador x y z t (Ar a i g)) (Estado mapa jogs)
                 l = (normalizaAngulo((inclinacaoPeca peca)-45))

avaliaAr njogador (Jogador x y z t (Chao v)) (Estado mapa jogs) = Nothing


-- | esta funcao analisa todos os jogadores no mapa, e se estes tiverem a menos de 1.01 distancia do bot,na mesma pista, este dispara cola, esta funcao é utilizada na funcao principal
analisaJogadores:: Int->Estado-> Maybe Jogada
analisaJogadores njogador (Estado mapa []) = Nothing
analisaJogadores njogador (Estado mapa jogs) | t>0 && (((fst a) == (fst b)) && (((snd a)+1.02 )== (snd b))) = Just Dispara
                                             | otherwise = analisaJogadores njogador (Estado mapa (tail(jogs)))
                
               where (Jogador x y z t m) = encontraIndiceLista njogador jogs 
                     a = retiraPistaPiso (head(jogs))
                     b = retiraPistaPiso (Jogador x y z t m)

-- | esta funcao  retira dos dados de um jogador para um par ordenado , respetivamente, os seguintes valores, a pista e a distancia a que esta esta nesta
retiraPistaPiso::Jogador -> (Int,Double)
retiraPistaPiso (Jogador x y z t m) = (x,y)



-- | Relatório (da Tarefa 6)
-- | Introdução: Esta tarefa refere-se à implementação de um bot, aplicando estratégias para que este seja efeciente e ganhe o jogo.

-- | Objetivo: O nosso objetivo é desenvolver um bot que seja o mais rápido e eficiente possível. Este avaliará o estado do mapa e do seu respetivo jogador, fazendo decisões inteligentes (avaliando o atrito oferecido pelos pisos, colocando cola quando for opurtuno,e acelerando o mais possível) de forma a que encontre o caminho mais rápido até à meta.

-- | Decisões e Estratégias: Sabendo da Tarefa 4 que a ordem de pisos com menos a mais atrito é a seguinte : Boost<Terra<Relva<Lama<Cola; concluímos que é do nosso interesse fazer com que o bot circule sempre no piso em que tenha menor atrito.
-- | Decidimos que uma das nossas prioridades era que o bot acelerasse, então este analisa o estado do jogador, no sentido em que se este jogador (Jogador _ _ _ _ (Chao False)), então o bot acelera, mudando o estado do jogador para (Jogador _ _ _ _ (Chao True)).
-- | A prioridade de jogadas que definimos foi a seguinte:Caso o jogador esteja no ar , então este ajusta a sua posicao para que não morra quando aterra no chão;
-- | Caso o jogador estiver no chao, este deve acelerar. Se o já tiver feito, então este verifica os pisos. Se tal já tenha sido feito então, o bot passa a analisar os jogadores para disparar colas, consequentemente atrasando a competição. Este fa-lo-á caso um dos (ou mais) jogadores estiverem no mesmo piso que o bot, a 1.02 distância deste.

-- | Discussão e Conclusão: Era também da nossa intenção aplicar uma funcão botdesacelera, em que este desacelarava caso não tivesse 0.2s para fazer uma jogada e se entrasse num piso indesejado. Esta função foi, de facto, desenvolvida, mas devido a imperfeições e ineficiências, decidimos que seria mais sensato não implementá-la. Não havia tempo suficiente para que esta reagisse de forma desejada e otimizada.
-- | Mesmo assim, sentimo-nos satisfeitos com esta tarefa , tendo em conta os problemas que as restantes nos deram. 

