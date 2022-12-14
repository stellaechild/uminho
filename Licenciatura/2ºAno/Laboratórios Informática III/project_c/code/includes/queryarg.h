/**
 * Data de criação 28/04/2021
 * Autor: Joana Alves - A93296
 * Versão: 20210428
 * 
 * Esta API serve para encapsular temporariamente vários argumentos e informação.
 * O seu objetivo é ser usado no commando "for_each" do glib. 
 * (Pois este apenas aceita um void* como data).
 */

#ifndef QUERYARG
#define QUERYARG

#include "table.h"
#include <glib.h>

typedef struct query * QueryArg;

/**
 * Aloca QueryArg
 */
QueryArg init_queryarg ();

/**
 * Liberta QueryArg
 */
void free_queryarg (QueryArg target);

/**
 * Devolve a GSequence
 */
GSequence* get_query_leaderboard(QueryArg target);

/**
 * Get char
 */
char get_query_letra (QueryArg target);

/**
 * Get String
 */
char* get_query_string (QueryArg target);

/**
 * Get Int
 */
int get_query_int (QueryArg target);

/**
 * Get TABLE
 */
TABLE get_query_table (QueryArg target);

/**
 * Get Double
 */
double get_query_stars (QueryArg target);

/**
 * Set Gsequence
 */
void set_query_leaderboard(QueryArg target,GSequence* x);

/**
 * Set char
 */
void set_query_letra (QueryArg target, char a);

/**
 * Set String
 */
void set_query_string (QueryArg target, char* string);

/**
 * Set Int
 */
void set_query_int (QueryArg target, int num);

/**
 * Incrementa o Int
 */
void inc_query_int (QueryArg target);

/**
 * Adiciona valor
 */
void add_query_stars (QueryArg target, double star);

/**
 * Set TABLE
 */
void set_query_table (QueryArg target, TABLE t);

/**
 * Set Double
 */
void set_query_stars (QueryArg target, double valor);

#endif
