/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Versão:20210417
 *
 * Código responsável pela manipulação da estrutura dos utilizadores
 */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "../../includes/FuncAux.h"
#include "../../includes/strUser.h"

struct struser {
    char *userID;
    char *name;
    int numfriends;
    StringL friends;
};


gint usercmp(gconstpointer a,gconstpointer b,gpointer data){
	(void) data;
	return strcmp(((StrUser)a)->userID,((StrUser)b)->userID);
}

StrUser new_user_empty(){
    StrUser new = malloc(sizeof(struct struser));
    new->friends = NULL;
    new->numfriends = 0;
    return new;
}

StrUser new_user_userID(char* userID){
    StrUser new = new_user_empty();
    new->userID = strdup(userID);
    new->friends = NULL;
    return new;
}

StrUser new_user_fromLine(char *buff,int friends){
    StrUser new = new_user_empty();
    int invalid = 0;

    new->userID = strdup(strsep(&buff,";"));
    if (strlen(new->userID) == 0) invalid = 1;
    new->name = strdup(strsep(&buff,";"));
    if (strlen(new->name) == 0) invalid = 1;

    if(friends){    
        new->friends = new_StringL();
        int aux,len;
        char *holder;
        for(aux = 0;buff != NULL;aux++){ 
	    holder = strdup(strsep(&buff,","));
	    len = strlen(holder);
	    if (holder[len-1] == '\n') holder[len-1] = '\0'; //Verifica se chegou ao fim da linha

	    if(aux == 0 && strcmp(holder,"None") == 0) aux--;  //Verifica se apenas há um "None" 
	    else {
            StringL_append_line(new->friends,holder);  
        }         
        } 
        new->numfriends = aux;
    }

    if (!invalid) return new;
    else {
        freeUser(new);
        return NULL;
    }
}


char* userToString (StrUser target, int arr[]) {
    int size = 1;
    char *id=NULL,*name=NULL,*num=NULL;
    if(arr[0]) {
        id = get_userID(target);
        size += strlen(id)+1;
    }
    if(arr[1]) {
        name = get_userName(target);
        size += strlen(name)+1;
    }
    if(arr[2]) {
        num = malloc(sizeof(char)*8);
        sprintf(num,"%d",get_userNumFriends(target));
        size += strlen(num)+1;
    }
    char * res = malloc(sizeof(char)*size);
    res[0] = '\0';

    if(arr[0]) {
        strcat(res,id);free(id);
        if(arr[1] || arr[2]) strcat(res,";");
    }
    if(arr[1]) {
        strcat(res,name);free(name);
        if(arr[2]) strcat(res,";");
    }
    if(arr[2]) {
        strcat(res,num);free(num);
    }

    return res;
}


char* get_userID(StrUser target){
    return strdup(target->userID);
}

char* get_userName(StrUser target){
    return strdup(target->name);
}

int get_userNumFriends(StrUser target){
    return target->numfriends;
}


void freeUser(StrUser target){ 
    free(target->name);
    free(target->userID);
    free(target);
}

StrUser cloneUser(StrUser target){
    if (target == NULL) return NULL;
    StrUser new = new_user_empty();
    new->userID = get_userID(target);
    new->name = get_userName(target);
    new->numfriends = target->numfriends;
    return new;
}
