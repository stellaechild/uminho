#ifndef INTERFACE
#define INTERFACE

#include <stdio.h>
/**
@file camadainterface.h
Este módulo contém as funções utilizadas para os vários comandos do jogo. 
*/

/**
\brief Esta função desenha, dependendo do nome do ficheiro, na consola ou num ficheiro o tabuleiro atual.
@param estado O estado alvo.
@param *nome Nome do ficheiro onde é gravado.
*/
void mostrar_tabuleiro(ESTADO estado,FILE *nome);

/**
\brief Função principal do projeto. Esta está num ciclo constante de interpretação de comandos e execução dos mesmos.
@param *e Apontador para o estado alvo previamente inicializado.
@return Retorna 1 se terminado.
*/
int interpretador(ESTADO *e);

/**
\brief Função para escrita de mensagens de estado do jogo para o user.
@param mensagem Número da mensagem a ser escrito.
*/
void escreve_mensagem(int mensagem);

/**
\brief Função usada na interpretação de comandos. Caso o comando não seja uma jogada esta função é chamada para interpretar.
@param linha[] String de input (comando).
@param *terminado Tipo de dado FIM para este ser alterado.
@param *e Apontador para o estado do jogo.
*/
int interpreta_comando(char linha[],FIM *terminado,ESTADO *e);

/**
\brief Desenha num ficheiro ou na consola a lista de jogadas do estado atual.
@param *e Apontador para o estado alvo.
@param *jogo Nome do ficheiro.
*/
void imprime_lista_de_jogadas(ESTADO *e,FILE *jogo);


#endif
