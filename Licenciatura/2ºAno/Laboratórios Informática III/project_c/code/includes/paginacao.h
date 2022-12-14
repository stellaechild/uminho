/**
 * Data de criação: 24/04/2021
 * Autor : Vicente Moreira
 * Versão 20210424
 *
 * Esta API trata se apresentar o tipo de dados TABLE
 */
 
#ifndef PAGINACAO
#define PAGINACAO

#include "table.h"

/**
 * Função principal, imprime a TABLE formatada.
 */
void print_table(TABLE target);

/**
 * Função que imprime a linha visual
 */
void print_table_top(TABLE target);

/**
 * Função que imprime os n ultimos elemetos da Table, sem restrição de tamanho.
 * É definido previamente por "infoline".
 */
void print_table_info(TABLE target);

#endif
