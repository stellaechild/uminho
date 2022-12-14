/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 *
 * Código responsável pela manipulação da estrutura dos negócios
 */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "../../includes/FuncAux.h"
#include "../../includes/strBusiness.h"

struct strbusiness {
    char *businessID;
    char *name;
    char *city;
    char *state;
    int numCategorias;
    StringL categorias;
};

gint busscmp(gconstpointer a,gconstpointer b,gpointer data){
	(void) data;
	return strcmp(((StrBusiness)a)->businessID,
			((StrBusiness)b)->businessID);
}

StrBusiness new_business_empty(){
    StrBusiness new = malloc(sizeof(struct strbusiness));
    
    return new;
}

StrBusiness new_business_businessID(char* businessID){
    StrBusiness new = new_business_empty();
    new->businessID = strdup(businessID);
    new->categorias = new_StringL();
    return new;
}

StrBusiness new_business_fromLine (char *buff){
    StrBusiness new = new_business_empty();
    int invalid = 0;

    new->businessID = strdup(strsep(&buff,";"));
    if(strlen(new->businessID) == 0) invalid = 1;
    new->name = strdup(strsep(&buff,";"));
    if(strlen(new->name) == 0) invalid = 1;
    new->city = strdup(strsep(&buff,";"));
    if(strlen(new->city) == 0) invalid = 1;
    new->state = strdup(strsep(&buff,";"));
    if(strlen(new->state) == 0) invalid = 1;

    char *holder;
    int len,aux; 
    new->categorias = new_StringL();
    for(aux = 0;buff != NULL ;aux++){
	   holder = strdup(strsep(&buff,","));
	   len = strlen(holder);
	   if (holder[len-1] == '\n') holder[len-1] = '\0'; //Verifica se chegou ao fim da linha

	   StringL_append_line(new->categorias,holder);
    }
    new->numCategorias = aux;

    if (!invalid) return new;
    else {
        freeBusiness(new);
        return NULL;
    }
}


char* businessToString (StrBusiness target, int arr[]) {
    int size = 1;
    char *id=NULL,*name=NULL,*city=NULL,*state=NULL,*ncat=NULL,*scat=NULL;
    if(arr[0]) {
        id = get_businessID(target);
        size += strlen(id)+1;
    }
    if(arr[1]) {
        name = get_businessName(target);
        size += strlen(name)+1;
    }
    if(arr[2]) {
        city = get_businessCity(target);
        size += strlen(city)+1;
    }
    if(arr[3]) {
        state = get_businessState(target);
        size += strlen(state)+1;
    }
    if(arr[4]) {
        ncat = malloc(sizeof(char) * 10);
        sprintf(ncat,"%d",get_businessNumCategoria(target));
        size += strlen(ncat)+1;
    }
    if(arr[5]) {
        scat = get_businessCategorias(target);
        size += strlen(scat)+1;
    }
    char * res = malloc(sizeof(char)*size+12);
    res[0] = '\0';

    if(arr[0]) {
        strcat(res,id);free(id);
        if(arr[1] || arr[2] || arr[3] || arr[4] || arr[5]) strcat(res,";");
    }
    if(arr[1]) {
        strcat(res,name);free(name);
        if(arr[2] || arr[3] || arr[4] || arr[5]) strcat(res,";");
    }
    if(arr[2]) {
        strcat(res,city);free(city);
        if(arr[3] || arr[4] || arr[5]) strcat(res,";");
    }
    if(arr[3]) {
        strcat(res,state);free(state);
        if(arr[4] || arr[5]) strcat(res,";");
    }
    if(arr[4]) {
        strcat(res,ncat);free(ncat);
        if(arr[5]) strcat(res,";");
    }
    if(arr[5]) {
        strcat(res,scat);free(scat);
    }
    
    return res;
}


char* get_businessID(StrBusiness target){
    return strdup(target->businessID);
}

char* get_businessName(StrBusiness target){
    return strdup(target->name);
}

char* get_businessCity(StrBusiness target){
    return strdup(target->city);
}

char* get_businessState(StrBusiness target){
    return strdup(target->state);
}

int get_businessNumCategoria(StrBusiness target){
    return target->numCategorias;
}

char* get_businessCategorias(StrBusiness target){
    char *res = malloc(sizeof(char)*get_businessNumCategoria(target)*32);
    char *holder;
    res[0] = '\0';
    for(int aux = 0;aux < get_businessNumCategoria(target);aux++){
        holder = get_StringL_line(target->categorias,aux);
        strcat(res,holder);
        if (aux+1 < get_businessNumCategoria(target)) strcat(res,"/");
        free(holder);
    }
    return res;
}

void freeBusiness(StrBusiness target){
    free_StringL(target->categorias);
    free(target->businessID);
    free(target->name);
    free(target->city);
    free(target->state);
    free(target);
}

StrBusiness cloneBusiness(StrBusiness target){
    if (target == NULL) return NULL;
    StrBusiness new = new_business_empty();
    new->businessID = get_businessID(target);
    new->name = get_businessName(target);
    new->city = get_businessCity(target);
    new->state = get_businessState(target);
    new->numCategorias = get_businessNumCategoria(target);
    new->categorias = clone_StringL(target->categorias);
    return new;
}
