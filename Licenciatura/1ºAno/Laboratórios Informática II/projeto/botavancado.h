#ifndef LI2PL11_BOTAVANCADO_H
#define LI2PL11_BOTAVANCADO_H

#include "camadadados.h"
#include "listasgenerica.h"

/**
@file botavancado.h
Este módulo é utilizado para a as várias funções utilizadas pelo bot avançado(jog2).
*/

/**
\brief Estrutura de dados usada pelo bot avançado
 */
typedef struct {
    int depth;
    COORDENADA jogada;
    int rating;
    ESTADO *e;
    LISTA jogadasPossiveis;
}BOTAVANCADOSTRUCT;


/**
\brief Função que aloca,cria e prepara ,com uma coordenada e rating, uma nova estrutura para o bot avançado.
@param e Apontador para o estado a ser replicado.
@param c A coordenada jogada.
@return Apontador para a estrutura criada.
 */
BOTAVANCADOSTRUCT *cria_struct_pronta(ESTADO *e,COORDENADA c);

/**
\brief Função que liberta todos os elementos da estrutura do bot avançado.
@param bot Apontador para a estrutura do bot avançado.
 */
void liberta_bot_struct(BOTAVANCADOSTRUCT *bot);

/**
\brief Retorna o rating da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@return O rating.
 */
int obter_rating (BOTAVANCADOSTRUCT *tmp);

/**
\brief Retorna a coordenada da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@return A coordenada.
 */
COORDENADA obter_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp);

/**
\brief Retorna o estado da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@return Pointer para o estado.
 */
ESTADO *obter_estado (BOTAVANCADOSTRUCT *tmp);

/**
\brief Retorna a lista dos elementos seguintes da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@return A lista.
 */
LISTA obter_lista_da_struct(BOTAVANCADOSTRUCT *tmp);

/**
\brief Altera o rating da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@param rat Rating a ser implementado.
 */
void altera_rating (BOTAVANCADOSTRUCT *tmp,int rating);

/**
\brief Altera a coordenada da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@param c Coordenada a ser implementada.
 */
void altera_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp,COORDENADA c);

/**
\brief Altera o estado da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@param estado Apontador para o estado a ser implementado.
 */
void altera_estado (BOTAVANCADOSTRUCT *tmp,ESTADO *estado);

/**
\brief Altera a lista da estrutura do bot avançado.
@param *tmp Apontador para a estrutura alvo.
@param L Lista a ser implementado.
 */
void altera_lista_na_struct (BOTAVANCADOSTRUCT *tmp,LISTA L);

/**
\brief Função utilizada no comando 'jog2'. Esta avalia várias jogadas possíveis e possíveis desfechos.
@param *e Apontador para o estado alvo.
@return Indicador de sucesso.
*/
int jog2 (ESTADO *e);

/**
\brief Função auxiliar do comando 'jog2'. Esta avalia vários possíveis desfechos futuros e escolhe a melhor coordenada.
@param L Lista de jogadas possíveis diretas.
@return Coordenada recomendada.
*/
COORDENADA avalia_jogadas_seguintes (LISTA L);

/**
\brief Apaga todos os dados criados pelo bot avançado.
@param L A lista a ser apagada.
 */
void apaga_memoria(LISTA L);

/**
\brief Função que cria estruturas para as jogadas possíveis ao redor.
@param *e Apontador para o estado alvo.
@param L Lista de gravação.
@return Lista completa.
*/
LISTA adiciona_jogadas_a_volta(ESTADO *e,LISTA L);

/**
\brief Função que avalia a melhor jogada possível para o inimigo.
@param L Lista a ser avaliada.
@return Retorna a posição na lista da melhor jogada.
*/
int avalia_jogada_melhor_inimigo(LISTA L);

/**
\brief Função que avalia a melhor jogada para o bot (tendo sempre em conta uma decisão inteligente do inimigo).
@param L Lista a ser avaliada.
@param med Valor do rating da decisão anterior.
@return Retorna o rating máximo.
*/
int avalia_jogada_maxima(LISTA L,int med);

/**
\brief Depois de avaliadas todas as situações, esta função toma a decisão final para a jogada.
@param L Lista a ser avaliada.
@return A melhor coordenada.
*/
COORDENADA avalia_jogada_melhor_final(LISTA L);

/**
\brief Função que liberta jogadas desnecessárias do inimigo.
@param L Lista a ser avaliada.
@param pos A posição onde está a melhor jogada do inimigo.
@return A lista (com membros apagados).
*/
LISTA apaga_membros_menores(LISTA L,int pos);

/**
\brief Função utilizada para pontuação de um dado tabuleiro. Esta avalia a proximidade à casa alvo. Também tem em conta fim de jogos.
@param *e Apontador do estado alvo.
@return Pontuação do tabuleiro.
*/
int avalia_tabuleiro_avancado(ESTADO *e);

#endif //LI2PL11_BOTAVANCADO_H
