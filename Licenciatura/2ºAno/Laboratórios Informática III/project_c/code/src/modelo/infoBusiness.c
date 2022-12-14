/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 *
 * Biblioteca que organiza e lista os vários Businesses. 
 */

#include <stdio.h>
#include <errno.h>
#include "../../includes/infoBusiness.h"

BusinessInfo newBusinessInfo () {
    BusinessInfo new = g_hash_table_new_full (g_str_hash, g_str_equal, (GDestroyNotify)free, (GDestroyNotify)freeBusiness);
    return new;
}

void addBusiness (BusinessInfo h, char* id, StrBusiness bus) {
    g_hash_table_insert(h, id, bus);
}

int contains_business (BusinessInfo h, char* businessID) {
    return g_hash_table_contains(h, businessID);
}

StrBusiness findBusiness (char* id, BusinessInfo lista) {
	StrBusiness holder = (StrBusiness) g_hash_table_lookup(lista, id);
	if (holder == NULL) return NULL;
	else return  cloneBusiness(holder);
}

void infoBusiness_foreach (BusinessInfo Rev,GHFunc func,gpointer user_data){
	g_hash_table_foreach (Rev,func,user_data);
}

void freeBusinessInfo (BusinessInfo h) {
    g_hash_table_destroy(h);
}


BusinessInfo load_BusinessInfo(char* filepath,int* total) {
	*total = 0;
	int cont=0;
	FILE *business_file = fopen(filepath,"r");
	if (business_file == NULL) {
		*total=-1;
		return NULL;
    }

	BusinessInfo businesses = newBusinessInfo();
	char *linha;
	int done = 0;

	free(linha = fgetsDinamico(business_file,&done)); //avanca a primeira line
	linha = fgetsDinamico(business_file,&done);
	while (!done){
		StrBusiness b = new_business_fromLine(linha);
		if (b != NULL){                               //Validação de campos vazios
			addBusiness(businesses, get_businessID(b), b);
			cont++;
		}
		free(linha);
		linha = fgetsDinamico(business_file,&done);
	}

	free(linha);
	fclose(business_file);
	*total = cont;
	return businesses;
}
