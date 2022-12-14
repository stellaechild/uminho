#ifndef LI2PL11_GESTAODEFICHEIROS_H
#define LI2PL11_GESTAODEFICHEIROS_H

#include <stdio.h>
#include "camadadados.h"

/**
@file gestaodeficheiros.h
Este módulo é utilizado para as funções de gravação e leitura de ficheiros.
*/

/**
\brief Comando gravar. Grava o estado atual num ficheiro.
@param *e Apontador para o estado a ser gravado.
@param nome[] Nome do ficheiro.
*/
int gravar (ESTADO *e, char nome[]);

/**
\brief Comando ler. Lê de um ficheiro e interpreta para um estado.
@param *e Apontador para o estado onde vai ser gravado a informação do ficheiro.
@param nome[] Nome do ficheiro a ser lido.
@return Indicador de sucesso.
*/
FIM ler (ESTADO *e,char nome[],int *mensagem);

/**
\brief Auxiliar da função ler. Lê de um ficheiro o tabuleiro e interpreta para um estado.
@param *e Apontador para o estado onde vai ser gravado a informação do ficheiro.
@param nome[] Nome do ficheiro a ser lido.
*/
void le_tabuleiro (ESTADO *e,FILE *jogo);

/**
\brief Auxiliar da função ler. Lê de um ficheiro a lista de jogadas e interpreta para um estado.
@param *e Apontador para o estado onde vai ser gravado a informação do ficheiro.
@param nome[] Nome do ficheiro a ser lido.
@return Indicador de sucesso.
*/
int le_lista_de_jogadas(ESTADO *e,FILE *jogo);

#endif //LI2PL11_GESTAODEFICHEIROS_H
