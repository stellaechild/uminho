#ifndef LI2PL11_BOTAVANCADO_H
#define LI2PL11_BOTAVANCADO_H

#include "camadadados.h"
#include "listasgenerica.h"

typedef struct {
    int depth;
    COORDENADA jogada;
    int rating;
    ESTADO *e;
    LISTA jogadasPossiveis;
}BOTAVANCADOSTRUCT;

BOTAVANCADOSTRUCT *cria_struct_avancada();

void liberta_bot_struct(BOTAVANCADOSTRUCT *bot);

int obter_rating (BOTAVANCADOSTRUCT *tmp);

COORDENADA obter_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp);

ESTADO *obter_estado (BOTAVANCADOSTRUCT *tmp);

LISTA obter_lista_da_struct(BOTAVANCADOSTRUCT *tmp);

void altera_rating (BOTAVANCADOSTRUCT *tmp,int rating);

void altera_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp,COORDENADA c);

void altera_estado (BOTAVANCADOSTRUCT *tmp,ESTADO *estado);

void altera_lista_na_struct (BOTAVANCADOSTRUCT *tmp,LISTA L);

BOTAVANCADOSTRUCT *cria_struct_pronta(ESTADO *e,COORDENADA c);

int jog2 (ESTADO *e);

void apaga_memoria(LISTA L);

LISTA adiciona_jogadas_a_volta(ESTADO *e,LISTA L);

int avalia_jogada_melhor_inimigo(LISTA L);

int avalia_jogada_maxima(LISTA L,int med);

COORDENADA avalia_jogada_melhor_final(LISTA L);

LISTA apaga_membros_menores(LISTA L,int pos);

int avalia_tabuleiro_avancado(ESTADO *e);

#endif //LI2PL11_BOTAVANCADO_H
