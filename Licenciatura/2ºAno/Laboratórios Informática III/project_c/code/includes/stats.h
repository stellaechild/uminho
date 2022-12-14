/**
 * Data de Criação: 23/04/2021
 * Autor: Vicente Moreira - A93296
 * Versão: 20210423
 *
 * Esta API organiza as estatísticas, assim como indices necessários
 * Contêm duas estruturas... Stats e DadToSons
 * 
 * DadToSons é um estrutura de uma string pai que irá conter um lista de Strings filhos.
 * Utilizado para Indexar "as reviews de cada User" e "as reviews de cada negócio".
 */

#ifndef STATSINC
#define STATSINC

#include <glib.h>

typedef GSequence* Index; 

typedef struct dadtosons* DadToSons;

typedef struct stats* Stats;

//------------------------------ALL ABOUT DADS AND SONS--------------------------
/**
 * Aloca DadToSons
 */
DadToSons init_DadToSons ();

/**
 * Liberta DadToSons
 */
void free_DadToSons (DadToSons target);

/**
 * Compara DadToSons
 */
gint cmpDadToSons (gconstpointer a,gconstpointer b, gpointer data);

/**
 * Sets FatherID. String é destruida
 */
void set_DadToSons_FatherID(DadToSons target,char* fatherID);

/**
 * Acrescenta um childID a um dado DadToSons. String destruida
 */
void dadToSons_append_son(DadToSons target,char* sonID);

/**
 * Devolve o fatherID
 */
char* get_DadToSons_fatherID(DadToSons target);

/**
 * Devolve o número de childs.
 */
int get_DadToSons_numChilds(DadToSons target);

/**
 * Devolve o childID na posição
 */
char* get_DadToSons_childID(DadToSons target,int pos);

/**
 * Clone DadToSons
 */
DadToSons clone_DadToSons(DadToSons target);
//-------------------------------------END---------------------------------------


/**
 * Aloca a mémoria incial
 */
Stats init_Stats();

/**
 * Liberta a estrutura
 */
void free_Stats(Stats target);

/**
 * Adiciona elemento ao index, se o ID pai já existe faz "append" no elemento.
 * Ambas as strings são destruidas.
 */
void stats_index_addEntry(Index target,char* fatherID,char* childID);

/*
 * Métodos SET
 */
void set_statsnumUsr(Stats target,int num);
void set_statsnumBiz(Stats target,int num);
void set_statsnumRev(Stats target,int num);

/*
 * Métodos GET 
 */
int get_statsnumUsr(Stats target);
int get_statsnumBiz(Stats target);
int get_statsnumRev(Stats target);

/**
 * Devolve o tamanho do index
 */
int get_statsIndexLength(Index target);

/**
 * Devolve o Index a ser referido/utilizado
 */
Index get_Index_pointer(Stats stats,int indexNum);

/**
 * Retorna o elemento do Index numa dada posição
 */
DadToSons get_statsIndexEntry(Index target,int pos);

/**
 * Retorna o elemento do Index pesquisando o FatherID
 */
DadToSons findDadToSonsFromFather (Index target, char* fatherID);

#endif
