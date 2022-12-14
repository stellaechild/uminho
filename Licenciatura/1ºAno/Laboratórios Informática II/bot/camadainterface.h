#ifndef INTERFACE
#define INTERFACE

#include <stdio.h>

void mostrar_tabuleiro(ESTADO estado,FILE *nome, int modo);

int interpretador(ESTADO *e);

void escreve_mensagem(int mensagem);

int interpreta_comando(char linha[],FIM *terminado,ESTADO *e);

void imprime_lista_de_jogadas(ESTADO *e,FILE *jogo);


#endif
