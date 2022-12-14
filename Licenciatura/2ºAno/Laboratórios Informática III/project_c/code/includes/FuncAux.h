/**
 * Data de Criação: 10/04/2021
 * Autor: Vicente Moreira - A93296
 * Versão: 20210417
 * 
 * Header que contem funções auxiliares ao projeto. 
 * Esta contêm a API StringL. Uma struct simples para armazenar várias Strings.
 */

#ifndef FUNCAUX
#define FUNCAUX

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct strList *StringL;

/**
 * Função semelhante à fgets mas não é necessário entregar um apontador char* pre-alocado. 
 * Permite a leitura das linhas dos ficheiros de desconhecidas/grandes dimensões.
 *
 * Quando a função chegar ao fim do ficheiro retorna 1 para o "signal". Senão retorna 0.
 */
char* fgetsDinamico(FILE *file,int *signal);

//----------------------------STRUCT STRINGL----------------------

/**
 * Aloca StringL(aux)
 */
StringL new_StringL_XEmpty();

/**
 * Aloca StringL
 */
StringL new_StringL();

/**
 * Aloca SrtingL com tamanho dado
 */
StringL new_StringL_size(int size);

/**
 * Adiciona um elemento à String List. String é destruida
 */
void StringL_append_line(StringL target,char* line);

/**
 * Remove um elemento da lista numa dada posição
 */
void StringL_remove_line(StringL target,int pos);

/**
 * Devolve um elemento da String List numa posição
 */
char* get_StringL_line(StringL target,int pos);

/**
 * Devolve o size de StringLists.
 */
int get_StringL_size(StringL target);

/**
 * Devolve o tamanho de um elemento da String List
 */
int get_StringL_line_size(StringL target,int pos);

/**
 * Devolve o tamanho dos caracteres de todos os elementos
 */
int get_StringL_totalChrSize(StringL target);

/**
 * Liberta a memória
 */
void free_StringL(StringL target);

/**
 * Clona StringL
 */
StringL clone_StringL(StringL target);

#endif
