//
// Created by vicshadow on 30/04/20.
//

#ifndef LI2PL11_GESTAODEFICHEIROS_H
#define LI2PL11_GESTAODEFICHEIROS_H

#include <stdio.h>
#include "camadadados.h"

int gravar (ESTADO *e, char nome[]);

FIM ler (ESTADO *e,char nome[],int *mensagem);

void le_tabuleiro (ESTADO *e,FILE *jogo);

int le_lista_de_jogadas(ESTADO *e,FILE *jogo);

#endif //LI2PL11_GESTAODEFICHEIROS_H
