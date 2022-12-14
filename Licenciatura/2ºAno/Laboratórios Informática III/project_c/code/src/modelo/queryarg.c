/**
 * Data de criação 28/04/2021
 * Autor: Joana Alves - A93296
 * Versão: 20210428
 *
 * Estrutura auxiliar para a resolução de algumas queries do módulo srg
 */

#include <string.h>
#include <stdlib.h>
#include "../../includes/queryarg.h"


struct query {
    char letra;
    char* string;
    int num;
    float stars;
    TABLE table;
    GSequence* leaderboard;
};

QueryArg init_queryarg () {
    QueryArg new = malloc (sizeof (struct query));
    new->string = NULL;
    new->table = NULL;
    return new;
}

void free_queryarg (QueryArg target) {
    if (target->table != NULL) table_free(target->table);
    if (target->string != NULL) free (target->string);
    free (target);
}

GSequence* get_query_leaderboard(QueryArg target){
    return target->leaderboard;
}

char get_query_letra (QueryArg target) {
    return target->letra;
}

char* get_query_string (QueryArg target) {
    return strdup (target->string);
}

int get_query_int (QueryArg target) {
    return target->num;
}

TABLE get_query_table (QueryArg target) {
    return target->table;
}

double get_query_stars (QueryArg target) {
    return target->stars;
}

void set_query_leaderboard(QueryArg target,GSequence* x){
    target->leaderboard = x;
}

void set_query_letra (QueryArg target, char a) {
    target->letra = a;
}

void set_query_string (QueryArg target, char* string) {
    target->string = strdup (string);
}

void set_query_int (QueryArg target, int num) {
    target->num = num;
}

void inc_query_int (QueryArg target) {
    target->num += 1;
}

void add_query_stars (QueryArg target, double star) {
    target->stars += star;
}

void set_query_table (QueryArg target, TABLE t) {
    target->table = t;
}

void set_query_stars (QueryArg target, double valor) {
    target->stars = valor;
}
