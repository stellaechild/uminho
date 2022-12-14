
#ifndef LI2PL11_BOTSIMPLES_H
#define LI2PL11_BOTSIMPLES_H

#include "camadadados.h"
#include "listasgenerica.h"
#include <stdlib.h>

typedef struct {
    int rating;
    COORDENADA casa;
}BOTSIMPSTRUCT;

BOTSIMPSTRUCT *cria_bot_struct_simples();

int obter_botsimp_rating(BOTSIMPSTRUCT *tmp);

COORDENADA obter_botsimp_coordenada(BOTSIMPSTRUCT *tmp);

void altera_botsimp_rating(BOTSIMPSTRUCT *tmp,int rat);

void altera_botsimp_coordenada(BOTSIMPSTRUCT *tmp,COORDENADA c);

int jog (ESTADO *e);

int avalia_tabuleiro(ESTADO *e);

COORDENADA avalia_melhor_jogada_da_lista(LISTA L);

#endif //LI2PL11_BOTSIMPLES_H
