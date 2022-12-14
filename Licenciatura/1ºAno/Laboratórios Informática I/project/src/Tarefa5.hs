 module Main where

import LI11920
import Graphics.Gloss
import Graphics.Gloss.Data.Color
import Graphics.Gloss.Juicy 
import Graphics.Gloss.Interface.Pure.Game   
import Tarefa1_2019li1g126
import Tarefa2_2019li1g126
import Tarefa4_2019li1g126
import Tarefa6_2019li1g126
import System.Random 

data Ui = Menu Int
data Npistas = Npista Int
data Cpistas = Cpista Int
type EstadoGloss = (Ui, Npistas,Cpistas,Estado, [Picture])

-- O objetivo desta tarefa era implementar as mecanicas da tarefa 2 e tarefa 4 num executavel, ou seja, fazer a parte grafica e interativa do jogo.
-- Inicialmente comecamos por definir a funcao que constroi um poligono de acordo com a peca.
-- Nessa mesma funcao chamamos outra funcao que de acordo com o piso da peca da a cor ao poligono criado.
-- Apos isso criamos uma funcao que dada uma pista chama a funcao que cria o poligono de acordo com a peca.
-- Por fim criamos uma funcao que dada um mapa ([Pista]) chama a funcao que faz a parte grafica das pistas recursivamente sobre cada pista do mapa, e de acordo com o numero da pista faz 
-- a translacao dela para baixo, para que assim as pistas nao estejam sobrepostas.
-- Entretanto decidimos que ficaria mais visualmente apelativo se criassemos umas "paredes" para o caso que uma peca tenha altura superior a 0, tenha algo que lhe que origine a sensacao de profundidade.
-- A funcao que dada uma pista chamava recursivamete a funcao que crias os graficos das pecas, agora tambem chama a funcao que cria as "paredes".
-- Criamos o EstadoGloss e os data types Ui Npistas e Cpistas, para conseguirmos fazer um menu interativo, ou seja, de acordo com as teclas que o utilizador escolhesse pressionar, este ou o levava para um mapa predefinido,
-- ou o levava para um menu onde lhe aparecia as teclas que ele deveria pressionar para gerar um mapa com a seed ja predefinida, ou seja, escolher o numero e o comprimento de pistas que quisessem entre os disponiveis.


mapa0 = gera 2 12 (-2)
mapa1 = gera 4 10   3
mapa2 = gera 3 15   5
mapa3 = gera 5 14   6
mapa4 = gera 3 13   2

---------------------------------------------
--Estas Funcoes Servem para desenhar o Mapa--
---------------------------------------------
-- | Jogo = 2

-- * Serve para dar a cor a's Pecas


colorir :: Piso -> Int -> Color
colorir piso npista | piso == Terra = (makeColorI 151 101 51   opacidade)
                    | piso == Relva = (makeColorI  0  102  0   opacidade)
                    | piso == Lama  = (makeColorI 102 51   0   opacidade)
                    | piso == Boost = (makeColorI 115 115 115  opacidade)
                    | piso == Cola  = (makeColorI 255 255 255  opacidade)
                          where opacidade = 255 -(15*npista)-- floor ((255*(1/(fromIntegral (npista+1))))+(255*(1/(fromIntegral (npista+2)))))


-- * Serve para dar a cor a's Paredes quando a altura da peca e' superior a 1
colorirParede :: Piso -> Int -> Color
colorirParede piso npista | piso == Terra = (makeColorI 133 68  34  opacidade)
                          | piso == Relva = (makeColorI 0   81   0  opacidade)
                          | piso == Lama  = (makeColorI 84  26   0  opacidade)
                          | piso == Boost = (makeColorI 89  80  89  opacidade)
                          | piso == Cola  = (makeColorI 230 230 230 opacidade)
                                where opacidade = 175 - (5*npista) -- floor (255*(1/(fromIntegral (npista+1))))
 
-- | Serve Para desenhar uma Peca 
drawPeca :: Peca -> Float -> Int -> Picture
drawPeca (Recta piso altura) i npista          = (translate i 0 (Pictures ([(drawRecta altura piso npista)]++(paredeReta altura piso npista))))
drawPeca (Rampa piso alturai alturaf) i npista | alturaf > alturai = if (alturai>0) then (translate (-60) 0 rampa) else rampa 
                                               | otherwise         = if (alturaf>0) then (translate (-60) 0 rampaD) else rampaD 
                                                   where rampa   = (translate i 0 (Color (colorir piso npista) (Pictures ([(translate 0 (60*(alti)) (drawRampa alturai alturaf))]++(paredeRampa alturai alturaf piso npista)))))
                                                         rampaD  = (translate i 0 (Color (colorir piso npista) (Pictures ([(translate 0 (60*(altf)) (rampaDescer (drawRampa alturaf alturai)))]++(paredeRampaDescer (paredeRampa alturaf alturai piso npista))))))                                               
                                                         alturasi = if alturai > 0 then alturai-1 else alturai
                                                         alti = fromIntegral alturasi
                                                         alturasf = if alturaf > 0 then alturaf-1 else alturaf
                                                         altf = fromIntegral alturasf

-- * Desenha a peca que tem como construtor Recta
drawRecta :: Int -> Piso -> Int -> Picture
drawRecta altura piso npista  = if (altura > 0) then (translate 0 60 (drawRecta (altura-1) piso npista)) else Pictures [reta,frame]
                                           where comprimento = (60*(fromIntegral altura))
                                                 largura     = (60+comprimento)
                                                 coordenadas = [(comprimento,comprimento),(largura,comprimento),(largura,largura),(comprimento,largura)]
                                                 reta        = Color (colorir piso npista) (Polygon coordenadas)
                                                 frame       = Color black (Line (coordenadas++[(comprimento,comprimento)]))

-- * Desenha a parede das Rectas
paredeReta :: Int -> Piso -> Int -> [Picture]
paredeReta altura piso npista = if altura == 0 then [Blank] else [wall,frame]
            where comprimento = (60*(fromIntegral altura))
                  largura     = (60+comprimento)
                  teste       = fromIntegral (60*altura)
                  coordenadas = [(comprimento-teste,comprimento-teste),(largura-teste,comprimento-teste),(largura-teste,largura-60),(comprimento-teste,largura-60)]
                  wall        = Color (colorirParede piso npista) (Polygon coordenadas)
                  frame       = Color black (Line coordenadas)


-- * Troca as coordenadas da Rampa, espelhando assim a Rampa que tinha sido desenhada
rampaDescer :: Picture -> Picture
rampaDescer (Pictures ((Polygon [(a,b),(c,d),(e,f),(g,h)]):(Color black (Line [(i,j),(k,l),(m,n),(o,p),(q,r)])):[])) = (Pictures [rampaD,frame])
                                                                                                   where x           = 60 + a 
                                                                                                         y           = c - 60
                                                                                                         coordenadas = [(y,d),(x,b),(x,h),(y,f)]
                                                                                                         rampaD      = Polygon coordenadas
                                                                                                         frame       = Color black (Line (coordenadas++[(y,r)]))  

-- * Desenha a peca que tem como construtor Rampa
drawRampa :: Int -> Int -> Picture 
drawRampa alturai alturaf = if (alturai > 1) then {-[(translate 0 60 (Pictures -} (drawRampa (alturai-1) (alturaf-1)){-))]-} else Pictures [rampa,frame]
                                                where comprimentoi = (60*(fromIntegral alturai))
                                                      comprimentof = (60*(fromIntegral alturaf))
                                                      largurai     = (60+comprimentoi)
                                                      larguraf     = (60+comprimentof)
                                                      coordenadas  = [(comprimentoi,comprimentoi),(largurai,comprimentof),(largurai,larguraf),(comprimentoi,largurai)]
                                                      rampa        = Polygon coordenadas
                                                      frame        = Color black (Line (coordenadas++[(comprimentoi,comprimentoi)]))

paredeRampaDescer :: [Picture] -> [Picture]
paredeRampaDescer (((Color cor (Polygon [(a,b),(c,d),(e,f),(g,h)]))):(Color black (Line [(i,j),(k,l),(m,n),(o,p)])):[]) = [rampaD,frame]
                                                                                                   where x           = 60 + a 
                                                                                                         y           = c - 60
                                                                                                         coordenadas = [(y,d),(x,b),(x,h),(y,f)]
                                                                                                         rampaD      = Color cor (Polygon coordenadas)
                                                                                                         frame       = Color black (Line ((coordenadas)++[(y,j)]))  
paredeRampaDescer ((Translate x y (Color cor (Polygon [(a,b),(c,d),(e,f),(g,h)]))):(Translate z w (Color corl (Line [(i,j),(k,l),(m,n),(o,p)]))):[]) = [rampaD,frame]
                                                                                                   where x           = 60 + a 
                                                                                                         y           = c - 60
                                                                                                         coordenadas = [(y,d),(x,b),(x,h),(y,f)]
                                                                                                         rampaD      = translate z w (Color cor (Polygon coordenadas))
                                                                                                         frame       = translate z w (Color black (Line ((coordenadas)++[(y,j)])))



-- * Desenha a parede das Rectas
paredeRampa :: Int -> Int -> Piso -> Int -> [Picture]
paredeRampa alturai alturaf piso npista = if (alturai < 1) then [wall,frame] else [translate ((-60)*(alturaiF-1)) 0 wallS, translate ((-60)*(alturaiF-1)) 0 frameS]
            where alturaiF      = fromIntegral alturai
                  alturafF      = fromIntegral alturaf
                  comprimentoi  = (60*(fromIntegral alturai))
                  comprimentof  = (60*(fromIntegral alturaf))
                  largurai      = (60+comprimentoi)
                  larguraf      = (60+comprimentof)
                  teste         = fromIntegral (60*alturai)
                  testef        = fromIntegral (60*alturaf)
                  coordenadas   = [(comprimentoi-teste,comprimentoi-teste),(largurai-teste,comprimentof-testef),(largurai-teste,larguraf-60),(comprimentoi-teste,largurai-60)]
                  wall          = Color (colorirParede piso npista) (Polygon coordenadas)
                  frame         = Color black (Line coordenadas)
                  testeS        = fromIntegral (60*(alturai-(alturai-1)))
                  testefS       = fromIntegral (60*(alturaf-(alturaf-1)))
                  coordenadasS  = [(comprimentoi,comprimentoi-(alturaiF*testeS)),(largurai,comprimentof-(alturafF*testefS)),(largurai,larguraf-testefS),(comprimentoi,largurai-testefS)]
                  wallS         = Color (colorirParede piso npista) (Polygon coordenadasS)
                  frameS        = Color black (Line coordenadasS)


-- * Desenha uma Pista com o auxilio da funcao 'drawPeca'
drawPista :: Pista -> Float -> Int -> [Picture]
drawPista [] _ npista = [] 
drawPista (h:t) i npista = if t/=[] then (drawPeca h i npista):(drawPista t (i+60) npista) else [(drawPeca h i npista)] 
                                          

-- Desenha um mapa com a ajuda recursiva da funcao 'drawPeca'
drawMapa :: Mapa -> Int -> [Picture]
drawMapa []      _    = [] 

drawMapa [h]   npista =  [(Pictures (map (translate 0 (-60*numeropista)) (drawPista h 0 npista)))]
                               where numeropista = fromIntegral npista
drawMapa (h:t) npista = (translate 0 (-60*numeropista) (Pictures (drawPista h 0 npista))) : (drawMapa t (npista+1))
                               where numeropista = fromIntegral npista

-- * Transforma uma [Picture] gerada pelo 'drawMapa' no Tipo Picture                      
transformaPic :: [Picture] -> Picture
transformaPic (h:t) = Pictures (h:t)


colocarMapa :: [Picture] -> Picture
colocarMapa mapa = (translate (-675) (-60) (transformaPic mapa))


-------------------------------------



estadoInicialGloss :: [Picture] -> EstadoGloss
estadoInicialGloss (menup:menug:jogadora:[]) = (Menu 0, Npista 3, Cpista 10, Estado mapa1 [jogador1],(menup:menug:jogadora:[]))

desenhaEstadoGloss :: EstadoGloss -> Picture
desenhaEstadoGloss (Menu ui,Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = case ui of 
                                              0 -> mp
                                              1 -> mg
                                              2 -> colocarMapa (estadogerado++[ja])
                                              3 -> colocarMapa (estado++[ja])
                                    where estado = (drawMapa mapa1 0)
                                          estadogerado = (drawMapa mapa 0)


reageTeclasJogo :: Event -> Estado -> Estado
reageTeclasJogo (EventKey (SpecialKey KeyUp)    Down _ _) e = jogada 0 (Movimenta C) e
reageTeclasJogo (EventKey (SpecialKey KeyDown)  Down _ _) e = jogada 0 (Movimenta B) e
reageTeclasJogo (EventKey (SpecialKey KeyLeft)  Down _ _) e = jogada 0 (Movimenta D) e
reageTeclasJogo (EventKey (SpecialKey KeyRight) Down _ _) e = jogada 0 (Movimenta E) e 
reageTeclasJogo (EventKey (Char 'w') Down _ _) e            = jogada 0 Acelera e 
reageTeclasJogo (EventKey (Char 's') Down _ _) e            = jogada 0 Desacelera e 
reageTeclasJogo (EventKey (Char 'a') Down _ _) e            = jogada 0 Dispara e  



reageEventoGloss :: Event -> EstadoGloss -> EstadoGloss
reageEventoGloss (EventKey (Char 'g') Down _ _) (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu 1,Npista n, Cpista c,Estado mapa listajogadores,(mp:mg:ja:[])) 
reageEventoGloss (EventKey (Char 'u') Down _ _) (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu 0,Npista n, Cpista c,Estado mapa listajogadores, (mp:mg:ja:[])) -- coloca tudo como estava originalmente
reageEventoGloss (EventKey (Char '1') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista n,Cpista 10, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '6') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista n,Cpista 12, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '7') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista n,Cpista 14, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '8') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista n,Cpista 15, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '2') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista 2,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '3') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista 3,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '4') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista 4,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char '5') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu ui, Npista 5,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (SpecialKey KeyEnter) Down _ _)     (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu 2,Npista n, Cpista c,Estado (gera n c (-100)) listajogadores,(mp:mg:ja:[]))--gerarSeed)))
reageEventoGloss (EventKey (Char 'j') Down _ _)                (Menu ui, Npista n,Cpista c, Estado mapa listajogadores, (mp:mg:ja:[])) = (Menu 3,Npista n, Cpista c,Estado mapa listajogadores,(mp:mg:ja:[]))
reageEventoGloss (EventKey (SpecialKey KeyUp)    Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[])) = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (SpecialKey KeyUp)    Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                       where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores , (mp:mg:ja:[]))
reageEventoGloss (EventKey (SpecialKey KeyDown)  Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[])) = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (SpecialKey KeyDown)  Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                        where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (SpecialKey KeyLeft)  Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[])) = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (SpecialKey KeyLeft)  Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                         where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (SpecialKey KeyRight) Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[])) = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (SpecialKey KeyRight) Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                            where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char 'w') Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[]))            = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (Char 'w') Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                           where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char 's') Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[]))            = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (Char 's') Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                            where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss (EventKey (Char 'a') Down x y) (Menu ui,Npista n,Cpista c,(Estado mapa listajogadores),(mp:mg:ja:[]))            = (Menu ui,Npista n,Cpista c,reageTeclasJogo (EventKey (Char 'a') Down x y) (Estado mapa listajogadores),(mp:mg:ja:[]))
                                                            where listaplayers = drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa listajogadores, (mp:mg:ja:[]))
reageEventoGloss _ estadogloss = estadogloss 




drawPlayers :: EstadoGloss -> [Picture]
drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa (h:t),(mp:mg:ja:[])) = (drawPlayer h (identifica h mapa) (Menu ui,Npista n, Cpista c,Estado mapa (h:t),(mp:mg:ja:[]))):(drawPlayers (Menu ui,Npista n,Cpista c,Estado mapa t,(mp:mg:ja:[])))
drawPlayers (_,_,_,Estado mapa _,_)    = []

jogadorA :: EstadoGloss -> Picture
jogadorA (Menu ui,Npista n,Cpista c,estado, (mp:mg:ja:[])) = ja



-- diz qual e a altura do jogador naquela peca ....usar a distancia do limite da peca com as alturas inicial e final pra descobrir a altura atual
alturaDoJogador :: Int -> Int -> Jogador -> Double 
alturaDoJogador ai af (Jogador x y v c (Chao e)) = ((fromIntegral (af-ai)) * (l)) 
                     where l = (distanciaLimPeca (Jogador x y v c (Chao e)))




-- Desenha um jogador 
drawPlayer :: Jogador -> Peca -> EstadoGloss -> Picture
drawPlayer (Jogador pista distancia _ _ _) (Recta _ altura) (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[])) = (Translate distanciaF alturaF (jogadorA (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[]))))
                                                                    where distanciaF  = (-685) + 60 * (realToFrac distancia)
                                                                          alturaF     = 60 * (realToFrac altura) + (-60) * (realToFrac pista)
drawPlayer j@(Jogador pista distancia velocidade cola (Chao e)) r@(Rampa piso alturai alturaf) (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[])) = Rotate inclinacaoF  (Translate distanciaF alturaF (jogadorA (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[]))))
                                                                        where distanciaF  = (-685) + 60 * (realToFrac distancia) -- alterar 
                                                                              alturaF     = 60 * (realToFrac (alturaDoJogador alturai alturaf (Jogador pista distancia velocidade cola (Chao e)))) + (-60) * (realToFrac pista) -- fazer o que diz na linha 241
                                                                              inclinacaoF = realToFrac (inclinacaoPeca (Rampa piso alturai alturaf)) -- alterar
drawPlayer (Jogador pista distancia _ _ (Ar altura inclinacao _)) _ (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[])) = Rotate inclinacaoF (Translate distanciaF alturaF (jogadorA (Menu ui,Npista n,Cpista c, estado ,(mp:mg:ja:[]))))
                                                                    where inclinacaoF = realToFrac inclinacao 
                                                                          distanciaF  = (-685) + 60 * (realToFrac distancia)
                                                                          alturaF     = 60 * (realToFrac altura) + (-60) * (realToFrac pista)
jogador1 = (Jogador 1 0 0 0 (Chao True)) 


distanciaLimPeca::Jogador -> Double 
distanciaLimPeca (Jogador x y v c (Chao e)) | (floor(y))==(fromIntegral (ceiling(y))) = 0
                                            | otherwise = ((fromIntegral a) - y)
                                           where a = ceiling y 


reageAoTempo :: Float -> Estado -> Estado
reageAoTempo t (Estado mapa (h:tail)) = (Estado mapa (map (passo tempo mapa) (h:tail)))
                            where tempo = realToFrac t 
reageAoTempo _ (Estado mapa []) = (Estado mapa [])                            



reageAoTempoGloss :: Float -> EstadoGloss -> EstadoGloss
reageAoTempoGloss t (Menu ui, Npista n, Cpista c, Estado mapa (h:tail), (mp:mg:ja:[])) = (Menu ui, Npista n, Cpista c, reageAoTempo t (Estado mapa (h:tail)), (mp:mg:ja:[]))  

fr = 20 

backgroundColor :: Color
backgroundColor = white  


-- | Função principal da Tarefa 5.
--
-- __NB:__ Esta Tarefa é completamente livre. Deve utilizar a biblioteca <http://hackage.haskell.org/package/gloss gloss> para animar o jogo, e reutilizar __de forma completa__ as funções das tarefas anteriores.
main :: IO ()
main = do 
       Just jogadora <- loadJuicy "images/Moto.png"
       Just menup <- loadJuicy "images/Menus/MenuPrincipal.png"
       Just menug <- loadJuicy "images/Menus/MenuGerar.png"

       play FullScreen                                   -- janela do jogo 
            backgroundColor                              -- cor do fundo
            fr                                           -- frame rate
            (estadoInicialGloss [menup,menug,jogadora])  -- estado inicial
            desenhaEstadoGloss                           -- desenha o estado do jogo
            reageEventoGloss                             -- reage a um evento
            reageAoTempoGloss                            -- reagea ao passar do tempo
     
   --  display FullScreen backgroundColor (colocarMapa (drawMapa mapa2 0))  
-- | Relatório da Tarefa 5:
-- | Introdução: A Tarefa 5 refere-se ao desenvolvimento gráfico do jogo com o Gloss.

-- | Objetivos: O nosso objetivo é desenhar o mapa, implementar os jogadores e os bots de modo a que o jogo consiga ser jogado de maneira correta.

-- | Discussão e Conclusão: