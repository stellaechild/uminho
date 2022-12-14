/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 *
 * Biblioteca que organiza e lista os vários Users. 
 */

#include <stdio.h>
#include <errno.h>
#include "../../includes/infoUser.h"


UserInfo newUserInfo () {
    UserInfo new = g_hash_table_new_full (g_str_hash, g_str_equal, (GDestroyNotify)free, (GDestroyNotify)freeUser);
    return new;
}

void addUser (UserInfo h, char* id, StrUser user) {
    g_hash_table_insert(h, id, user);
}

int contains_user (UserInfo h, char* userID) {
    return g_hash_table_contains(h, userID);
}

StrUser findUser (char* id, UserInfo lista) {
	StrUser holder = (StrUser)g_hash_table_lookup(lista, id);
	if (holder == NULL) return NULL;
	else return  cloneUser (holder);
}

void infoUser_foreach (UserInfo Rev,GHFunc func,gpointer user_data){
	g_hash_table_foreach (Rev,func,user_data);
}

void freeUserInfo (UserInfo h) {
    g_hash_table_destroy(h); 
}

UserInfo load_UserInfo(char* filepath,int* total,int friends) {
	*total = 0;
	int cont=0;
	FILE *users_file=fopen(filepath,"r");
	if (users_file == NULL) {
		*total=-1;
		return NULL;
	}

	UserInfo users = newUserInfo();
	char *linha;
	int done = 0;

	free(linha = fgetsDinamico(users_file,&done)); //avanca a primeira line
	linha = fgetsDinamico(users_file,&done);
	while (!done){                                
		StrUser s = new_user_fromLine(linha,friends);
		if(s != NULL){                             //Validação de campos vazios
			addUser(users, get_userID(s), s);
			cont++;
		}
		free(linha);
		linha = fgetsDinamico(users_file,&done);
	}

	free(linha);
	fclose(users_file);
	*total = cont;
	return users;
}
