/**
 * Data de criação: 22/04/2021
 * Joana Alves - A93290
 * Versão: 20210422
 *
 * Ficheiro de regulação das variáveis.
 */
#include "../../includes/variaveis.h"

Variables init_variables () {
    Variables new = g_hash_table_new_full (g_str_hash, 
                                           g_str_equal,
                                           (GDestroyNotify)free,
                                           (GDestroyNotify)table_free);
    return new;
}

void add_variable (Variables h, char* nome, TABLE t) {
    g_hash_table_insert(h, nome, table_clone(t));
}

GList* get_variables(Variables h){
    return g_hash_table_get_keys(h);
}


int num_variables (Variables h) {
    return g_hash_table_size(h);
}

TABLE lookup_variable (Variables h, char* nome) {
    TABLE holder = (TABLE)g_hash_table_lookup(h, nome);
    if (holder == NULL) return NULL;
    else return table_clone(holder);
}

bool contains_variable (Variables h, char* nome) {
    return g_hash_table_contains(h, nome);
}

void remove_variable (Variables h, char* nome) {
    g_hash_table_remove(h, nome);
}

void free_variables (Variables h) {
    g_hash_table_remove_all(h);
    g_hash_table_unref(h);
}

void free_all_content(Variables h){
    g_hash_table_remove_all (h);
}
