#ifndef LOGICA
#define LOGICA
/**
@file logicaprograma.h
Módulo utilizado para a inteligência do programa. Esta faz jogadas, avalia o estado e é responsável pela inteligência artifical.
*/

/**
\brief Função que executa uma jogada. Avalia se é válida e executa-a.
@param *e Apontador para o estado alvo.
@param c Coordenada a ser jogada.
@return Indicador de sucesso.
*/
int jogar(ESTADO *estado, COORDENADA c);

/**
\brief Esta função testa se uma dada jogada é válida, dado um estado e uma certa coordenada.
@param *e Apontador para o estado alvo.
@return Indicador de sucesso.
*/
int jogada_valida(ESTADO *e,COORDENADA c);

/**
\brief Esta função verifica se o jogo acabou, e se sim, qual o jogador que o ganhou.
@param *e Apontador para o estado alvo.
@return Dado FIM que indica se o jogo acabou.
*/
FIM jogo_terminado(ESTADO *e);

/**
\brief Função que verifica as casas à volta da peça BRANCA (procura por peças não PRETAS).
@param *e Apontador para o estado alvo.
@return Retorna um dado FIM.
*/
FIM verifica_a_volta (ESTADO *e);

/**
\brief Função auxiliar. Recebendo uma coordenada e uma posição relativa, devolve a coordenada da casa alvo.
@param jog Coordenada alvo.
@param pos Posição relativa a ser testada.
@return Coordenada pretendida.
*/
COORDENADA pos_coordenada(COORDENADA jog,int pos);

#endif
