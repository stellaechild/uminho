-- | Este módulo define os tipos de dados comuns a todos os alunos, tal como descrito e utilizado no enunciado do trabalho prático. 
module LI11920 where

-- * Tipos de dados auxiliares.
-- | um mapa corresponde a uma lista de pistas 
type Mapa = [Pista]
-- | uma pista corresponde a uma lista de pecas
type Pista = [Peca]
-- | uma peca
data Peca
    = Recta Piso Int
    | Rampa Piso Int Int
  deriving (Read,Show,Eq)
-- | Um piso
data Piso = Terra | Relva | Lama | Boost | Cola
  deriving (Read,Show,Eq)
-- | Jogador
data Jogador
    = Jogador { pistaJogador :: Int, distanciaJogador :: Double, velocidadeJogador :: Double, colaJogador :: Int, estadoJogador :: EstadoJogador }
  deriving (Read,Show,Eq)
-- | Estado do Jogador
data EstadoJogador
    = Chao { aceleraJogador :: Bool }
    | Morto { timeoutJogador :: Double }
    | Ar { alturaJogador :: Double, inclinacaoJogador :: Double, gravidadeJogador :: Double }
  deriving (Read,Show,Eq)

-- | Estado do jogo.
data Estado = Estado
    { mapaEstado      :: Mapa
    , jogadoresEstado :: [Jogador] -- ^ lista de jogadores com identificador igual ao índice na lista
    }
  deriving (Read,Show,Eq)

-- | Uma direção.
data Direcao
    = C -- ^ Cima
    | D -- ^ Direita
    | B -- ^ Baixo
    | E -- ^ Esquerda
  deriving (Read,Show,Eq,Enum,Bounded)
-- | Uma Jogada
data Jogada
    = Movimenta Direcao
    | Acelera
    | Desacelera
    | Dispara -- ^ cola
  deriving (Read,Show,Eq)
 
-- | Instrucoes sao listas de intrucao   
type Instrucoes = [Instrucao]

-- | Uma Instrucao
data Instrucao
    = Anda [Int] Piso
    | Sobe [Int] Piso Int
    | Desce [Int] Piso Int
    | Teleporta [Int] Int
    | Repete Int Instrucoes
  deriving (Read,Show,Eq)

-- | funcao que define o tamanho de dadas instrucoes
tamanhoInstrucoes :: Instrucoes -> Int
tamanhoInstrucoes is = sum (map tamanhoInstrucao is)

-- | funcao que define o tamanho de uma intrucao
tamanhoInstrucao :: Instrucao -> Int
tamanhoInstrucao (Repete _ is) = succ $ tamanhoInstrucoes is
tamanhoInstrucao _ = 1
