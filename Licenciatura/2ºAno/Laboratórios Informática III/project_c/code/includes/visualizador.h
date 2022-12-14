/**
 * Data de criação: 15/04/2021
 * Joana Alves - A93290
 * Versão: 20210415
 * 
 * API das funções do visualizador.
 */

#ifndef VISUALIZADOR_H
#define VISUALIZADOR_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "variaveis.h"

/**
 * Mensagem (1) de inicialização do programa.
 */
void showBoasVindas();

/**
 * Mensagem (2) de inicialização do programa.
 */
void showCookies();

/**
 * Mensagem de exit do programa.
 */
void showMenuSaida();

/**
 * Linha de separação
 */
void showSeparador ();

/**
 * Mostra o símbolo '$'
 */
void showPrompt ();

/**
 * Função que recebe uma string como parâmetro e faz a impressão da mesma no stdout.
 * @param mensagem Mensagem alvo.
 */
void showMessage(char* mensagem);

/**
 * Imprime as queries disponíveis no programa.
 */
void showQueries();

/**
 * Recebe um inteiro como argumento e imprime a query correspondente. Caso não exista, imprime mensagem de erro.
 * O primeiro argumento define a query. De seguida, os outros argumentos servem para dinamizar a mensagem
 * com os valores previamente inseridos pelo utilizador.
  * @param a Query alvo.
 */
void showQuery (int a, int c, float b, char s, char* string);

/**
 * Mensagem de ajuda do programa.Contém os vários comandos disponíveis no programa.
 */
void showHelp();

/**
 * Imprime uma mensagem de erro com o comando recebido como argumento.
  * @param comando Comando alvo.
 */
void showInvalido (char* comando);

/**
 * Mostra mensagem de sucesso do comando, com o tempo de execução do mesmo.
 */
void showExecutado (double tempo);

/**
 * Mensagem de erro do programa.
 */
void showErro();

/**
 * Mensagem de leitura dos vários tipos de dados para o programa.
 */
void showLeituraAll(int def);

/**
 * Mensagem de limpeza no encerramento do programa.
 */
void showClean ();
void showAllSet ();

#endif
