/**
 * Data de Criação: 20/04/2021
 * Autor: Vicente Moreira - A93296
 * Versão: 20210420
 *
 * Este módulo é responsável por "montar" e preparar o tipo de dados "TABLE"
 */

#include <stdlib.h>
#include <string.h>
#include "../../includes/FuncAux.h"
#include "../../includes/table.h"

struct table
{
  int capacitylinhas;
  int lenlinhas;
  int topline;
  int infoline;
  int *sizeCols;
  StringL *content;
};

static int maxColuna(TABLE target,int coluna){
    int max = 0;
    int infoline = target->infoline;
    for(int aux = 0;(!infoline && aux < target->lenlinhas) || (infoline && aux < infoline);aux++){
        if (coluna < get_StringL_size(target->content[aux]) && get_StringL_line_size(target->content[aux],coluna) > max)
        max = get_StringL_line_size(target->content[aux],coluna);
    }
    return max;
}

TABLE new_empty_sizedTable(int linhaSize,int topline){
    TABLE new = malloc(sizeof(struct table));
    new->sizeCols = NULL;
    new->infoline = 0;
    new->lenlinhas = 0;
    new->topline = topline;
    new->capacitylinhas = linhaSize;
    new->content = malloc(sizeof(StringL*)*linhaSize);
    for(int aux = 0;aux < linhaSize;aux++){
        new->content[aux] = NULL;
    }
    return new;
}

TABLE new_empty_table(int topline){
    TABLE new = malloc(sizeof(struct table));
    new->sizeCols = NULL;
    new->topline = topline;
    new->infoline = 0;
    new->lenlinhas = 0;
    new->capacitylinhas = 1;
    new->content = malloc(sizeof(StringL*));
    return new;
}

void table_append_line(TABLE target,char *str){
    if(target->capacitylinhas == target->lenlinhas){  //Se chegou ao limite de capacidade, aumenta
        target->capacitylinhas *= 2;
        target->content = reallocarray(target->content,sizeof(StringL*),target->capacitylinhas);
    }
    target->content[target->lenlinhas] = new_StringL();
    char* holder = str;
    while (holder!=NULL){
        StringL_append_line(target->content[target->lenlinhas],strdup(strsep(&holder,";")));
    }
    target->lenlinhas++;
    free(str);
}

void table_setInfoLine_now(TABLE target){
    target->infoline = target->lenlinhas;
}

void table_setInfoLine_pos(TABLE target,int pos){
    target->infoline = pos;
}

int table_getInfoLine(TABLE target){
    return target->infoline;
}

int* table_getsizeCol(TABLE target){
    return target->sizeCols;
}

char* table_get_element(TABLE target,int x,int y){
    if(x < target->lenlinhas && y < table_getNumColuna(target,x)){
        return get_StringL_line(target->content[x],y);
    }
    return NULL;
}

int table_get_element_size(TABLE target,int x,int y){
    if(x >= 0 && x < target->lenlinhas && y <  table_getNumColuna(target,x)){
        return get_StringL_line_size(target->content[x],y);
    }
    return 0;
}

char* table_get_line_mounted(TABLE target,char* delim,int pos){  //ToCSV
    int cols = get_StringL_size(target->content[pos]);
    int size = get_StringL_totalChrSize(target->content[pos]);
    size += (cols*strlen(delim))+12; //12 for good measure.
    char *res = malloc(sizeof(char)*size);
    res[0] = '\0';
    for(int aux = 0;aux<cols;aux++){
        char *holder = get_StringL_line(target->content[pos],aux);
        strcat(res,holder);
        strcat(res,delim);
        free(holder);
    }
    res[strlen(res)-1] = '\0';
    strcat(res,"\n");
    return res;
}

int table_getNumLinhas(TABLE target){
    return target->lenlinhas;
}

int table_get_colSizePos(TABLE target,int pos){
    if (target->sizeCols == NULL) return 0;
    else return target->sizeCols[pos];
}

int table_getNumColunasTOP(TABLE target){
    return  table_getNumColuna(target,0);
}

int table_getNumColuna(TABLE target,int linha){
    if (linha < target->lenlinhas) return get_StringL_size(target->content[linha]);
    return 0;
}

int table_topline(TABLE target){
    return target->topline;
}

void table_free(TABLE target){
    for(int aux = 0;aux<target->lenlinhas;aux++){
        free_StringL(target->content[aux]);
    }
    if (target->sizeCols != NULL) free(target->sizeCols);
    free (target->content);
    free (target);
}

TABLE table_clone(TABLE target){
    TABLE clone = new_empty_sizedTable(target->capacitylinhas,table_topline(target));
    clone->infoline = target->infoline;
    clone->lenlinhas = target->lenlinhas;
    for(int x=0;x<target->lenlinhas;x++){
        clone->content[x] = clone_StringL(target->content[x]);
    }
    return clone;
}

void table_calculate_colsize(TABLE target){
    if (target->sizeCols == NULL && target->lenlinhas > 0) {
        target->sizeCols = malloc(sizeof(int)* table_getNumColuna(target,0));
        for (int aux = 0;aux<table_getNumColuna(target,0);aux++){
            target->sizeCols[aux] = maxColuna(target,aux);
        }
    }
}

TABLE get_table_invalidSgr(TABLE res){
    table_append_line(res, strdup("Querie impossível de realizar!"));
	table_append_line(res, strdup("Verifique se o SGR e os seus"));
	table_append_line(res, strdup("módulos foram bem carregados."));
    return res;
}
