/**
 * Data de Criação: 23/04/2021
 * Autor: Vicente Moreira - A93296
 * Versão: 20210423
 *
 * Este Módulo cria e armazena as estatísticas
 * Esta contêm uma estrutura interna que serve para indexar certos elementos
 */

#include <stdlib.h>
#include "../../includes/stats.h"

struct dadtosons{
    char* fatherID;
    int numChilds;
    int capacity;
    char** childIDs;
};

struct stats {
    int numUsr;
    int numBiz;
    int numRev;
    int numBusWCategoria;
    Index usrToRev;
    Index busToRev;
};

//------------------------------ALL ABOUT DADS AND SONS--------------------------
DadToSons init_DadToSons (){
    DadToSons new = malloc(sizeof(struct dadtosons));
    new->capacity = 1;
    new->numChilds = 0;
    return new;
}

void free_DadToSons (DadToSons target){
    if(target->numChilds > 0){
           for (int aux=0;aux<target->numChilds;aux++){
        free(target->childIDs[aux]);
    }
    free (target->childIDs); 
    }
    if(target->fatherID !=NULL) free (target->fatherID);
    free (target);
}

gint cmpDadToSons (gconstpointer a,gconstpointer b, gpointer data){
    (void) data;
	return strcmp(((DadToSons)a)->fatherID,((DadToSons)b)->fatherID);
}

void set_DadToSons_FatherID(DadToSons target,char* fatherID){
    target->fatherID = strdup(fatherID);
    free(fatherID);
}

void dadToSons_append_son(DadToSons target,char* sonID){    
    if(target->numChilds == 0 && target->capacity == 1){
        target->childIDs = malloc(sizeof(char*));    //Se é a primeira inserção
    }
    else if(target->numChilds == target->capacity){  //Aumenta a capacity
        target->capacity *= 2;
        char** holder = (char**) reallocarray(target->childIDs,sizeof(char*),target->capacity);
        if(holder != NULL) target->childIDs = holder;
        else return;
    }
    target->childIDs[target->numChilds] = strdup(sonID);
    target->numChilds++;
    free(sonID);
}

char* get_DadToSons_fatherID(DadToSons target){
    return strdup(target->fatherID);
}

int get_DadToSons_numChilds(DadToSons target){
    return target->numChilds;
}

char* get_DadToSons_childID(DadToSons target,int pos){
    return strdup(target->childIDs[pos]);
}

DadToSons clone_DadToSons(DadToSons target){  //Não muito eficiente
    DadToSons clone = init_DadToSons();
    set_DadToSons_FatherID(clone,get_DadToSons_fatherID(target));
    for(int aux = 0;aux < target->numChilds;aux++){
        dadToSons_append_son(clone,get_DadToSons_childID(target,aux));
    }
    return clone;
}
//-------------------------------------END---------------------------------------


Stats init_Stats() {
    Stats new = malloc(sizeof(struct stats));
    new->usrToRev = g_sequence_new((GDestroyNotify) free_DadToSons);
    new->busToRev = g_sequence_new((GDestroyNotify) free_DadToSons);
    return new;
}

void free_Stats(Stats target){
    g_sequence_free(target->busToRev);
    g_sequence_free(target->usrToRev);
    free (target);
}

void stats_index_addEntry(Index target,char* fatherID,char* childID){
    DadToSons key = init_DadToSons();
    set_DadToSons_FatherID(key,fatherID);
    GSequenceIter* iter = g_sequence_lookup(target,key,cmpDadToSons,NULL);
    if (iter == NULL){
        dadToSons_append_son(key,childID);
        g_sequence_insert_sorted(target,(gpointer) key,cmpDadToSons,NULL);
    }
    else {
        dadToSons_append_son((DadToSons) g_sequence_get(iter),childID);
        free_DadToSons(key);
    }
}


void set_statsnumUsr(Stats target,int num) {
    target->numUsr = num;
}
void set_statsnumBiz(Stats target,int num) {
    target->numBiz = num;
}
void set_statsnumRev(Stats target,int num) {
    target->numRev = num;
}

int get_statsnumUsr(Stats target) {
    return target->numUsr;
}
int get_statsnumBiz(Stats target) {
    return target->numBiz;
}
int get_statsnumRev(Stats target) {
    return target->numRev;
}
int get_statsIndexLength(Index target){
    return g_sequence_get_length(target);
}

Index get_Index_pointer(Stats stats,int indexNum){
    if(indexNum == 0) return stats->usrToRev;
    if(indexNum == 1) return stats->busToRev;
    else return NULL;
}

DadToSons get_statsIndexEntry(Index target,int pos){
    GSequenceIter* iter = g_sequence_get_iter_at_pos (target,pos);
    return clone_DadToSons((DadToSons) g_sequence_get(iter));
}

DadToSons findDadToSonsFromFather (Index target, char* userID) {
    DadToSons key = init_DadToSons();
    set_DadToSons_FatherID (key, userID);

    GSequenceIter * iter = g_sequence_lookup (target, key, cmpDadToSons, NULL);

    free_DadToSons (key);

    if (iter != NULL) return clone_DadToSons ((DadToSons) g_sequence_get(iter));
    else return NULL;
}
