
#ifndef LI2PL11_BOTSIMPLES_H
#define LI2PL11_BOTSIMPLES_H

#include "camadadados.h"
#include "listasgenerica.h"
#include <stdlib.h>

/**
@file botsimples.h
Este módulo é utilizado para a as várias funções utilizadas pelo bot simples(jog1).
*/

/**
\brief Estrutura de dados usada pelo bot simples
 */
typedef struct {
    int rating;
    COORDENADA casa;
}BOTSIMPSTRUCT;

/**
\brief Função que aloca e cria uma nova estrutura do bot simples.
@return Apontador para a estrutura criada.
 */
BOTSIMPSTRUCT *cria_bot_struct_simples();

/**
\brief Retorna o rating da estrutura do bot simples.
@param *tmp Apontador para a estrutura alvo.
@return O rating.
 */
int obter_botsimp_rating(BOTSIMPSTRUCT *tmp);

/**
\brief Retorna a coordenada da estrutura do bot simples.
@param *tmp Apontador para a estrutura alvo.
@return A coordenada.
 */
COORDENADA obter_botsimp_coordenada(BOTSIMPSTRUCT *tmp);

/**
\brief Altera o rating da estrutura do bot simples.
@param *tmp Apontador para a estrutura alvo.
@param rat Rating a ser implementado.
 */
void altera_botsimp_rating(BOTSIMPSTRUCT *tmp,int rat);

/**
\brief Altera a coordenada da estrutura do bot simples.
@param *tmp Apontador para a estrutura alvo.
@param c Coordenada a ser implementada.
 */
void altera_botsimp_coordenada(BOTSIMPSTRUCT *tmp,COORDENADA c);

/**
\brief Função utilizada no comando 'jog'. Esta avalia as várias casas ao seu redor e escolhe a melhor.
@param *e Apontador para o estado alvo.
*/
int jog (ESTADO *e);

/**
\brief Função utilizada para pontuação de um dado tabuleiro. Esta avalia a proximidade à casa alvo.
@param *e Apontador para o estado alvo.
@return Pontuação do tabuleiro.
*/
int avalia_tabuleiro(ESTADO *e);

/**
\brief Função utilizada para avaliar a melhor jogada numa lista de jogadas possíveis.
@param L Lista alvo.
@return Coordenada da melhor jogada.
*/
COORDENADA avalia_melhor_jogada_da_lista(LISTA L);

#endif //LI2PL11_BOTSIMPLES_H
