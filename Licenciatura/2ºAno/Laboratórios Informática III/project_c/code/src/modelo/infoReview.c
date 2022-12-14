/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * Biblioteca que organiza e lista as várias Reviews. 
 */

#include <stdio.h>
#include <errno.h>
#include "../../includes/infoReview.h"

ReviewInfo newReviewInfo () {
    ReviewInfo new = g_hash_table_new_full (g_str_hash, g_str_equal, (GDestroyNotify)free, (GDestroyNotify)freeReview);
    return new;
}


void addReview (ReviewInfo h, char* id, StrReview review) {
    g_hash_table_insert(h, id, review);
}

int contains_review (ReviewInfo h, char* reviewID) {
    return g_hash_table_contains(h, reviewID);
}

StrReview findReview (char* id, ReviewInfo lista) {
	StrReview holder = (StrReview) g_hash_table_lookup(lista, id);
	if (holder == NULL) return NULL;
	else return  cloneReview(holder);
}

void infoReview_foreach (ReviewInfo Rev,GHFunc func,gpointer user_data){
	g_hash_table_foreach (Rev,func,user_data);
}

void freeReviewInfo (ReviewInfo h) {
    g_hash_table_destroy(h);
}


ReviewInfo load_ReviewInfo(char* filepath,int* total,UserInfo usr,BusinessInfo biz) {
	*total = 0;
	int cont=0;
	FILE *review_file = fopen(filepath,"r");
	if (review_file == NULL) {
		*total=-1;
		return NULL;
	}

	ReviewInfo reviews = newReviewInfo();
	char *linha;
	int done = 0;

	free(fgetsDinamico(review_file,&done));     //avanca a primeira line
	linha = fgetsDinamico(review_file,&done);
	while (!done){
		StrReview r = new_review_fromLine(linha);
		if (r != NULL){                                //Validação de campos vazios
			char* revUsr = get_reviewUserID(r);
			char* revBiz = get_reviewBusinessID(r);
			if(contains_user(usr,revUsr) && contains_business(biz,revBiz)){ //Validação de UserID e BusinessID
				addReview(reviews,get_reviewID(r), r);
				cont++;
			}
			else freeReview(r);
			free(revUsr);free(revBiz);
		}
		free(linha);
		linha = fgetsDinamico(review_file,&done);
	}
	free(linha);
	fclose(review_file);
	*total = cont;
	return reviews;
}
