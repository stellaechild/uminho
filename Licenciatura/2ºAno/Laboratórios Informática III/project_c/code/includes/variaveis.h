/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Versão: 20210417
 * 
 * API responsável pela manipulação das variáveis do programa.
 */

#ifndef VARIAVEIS_H
#define VARIAVEIS_H

#include <glib.h>
#include <stdbool.h>
#include "table.h"

typedef GHashTable* Variables;

/**
 * Aloca Variables
 */
Variables init_variables ();

/**
 * Adiciona uma variável. Esta é clonada na inserçao.
 */
void add_variable (Variables h, char* nome, TABLE t);

/**
 * Devolve a list das keys
 */
GList* get_variables(Variables h);

/**
 * Devolve o número de variáveis
 */
int num_variables (Variables h);

/**
 * Procura uma váriavel, se não existir retorna NULL
 * Se sim, clona-a.
 */
TABLE lookup_variable (Variables h, char* nome);

/**
 * Função que verifica se a variável existe
 */
bool contains_variable (Variables h, char* nome);

/**
 * Função que remove variáveis
 */
void remove_variable (Variables h, char* nome);

/**
 * Remove todas as variáveis mas preserva a hashtable
 */
void free_all_content(Variables h);

/**
 * Liberta Variaveis
 */
void free_variables (Variables h);

#endif
