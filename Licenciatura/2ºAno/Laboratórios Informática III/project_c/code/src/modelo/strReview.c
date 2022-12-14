/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Versão: 20210417
 *
 * Código responsável pela manipulação da estrutura das reviews
 */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "../../includes/strReview.h"

struct strreview {
    char *reviewID;
    char *userID;
    char *businessID;
    float stars;
    int useful;
    int funny;
    int cool;
    char* date;
    char* texto;
};

gint reviewcmp(gconstpointer a,gconstpointer b,gpointer data){
    (void) data;
	return strcmp(((StrReview)a)->reviewID,
			((StrReview)b)->reviewID);
}

StrReview new_review_empty(){
    StrReview new = malloc(sizeof(struct strreview));
    return new;
}

StrReview new_review_reviewID(char* reviewID){
    StrReview new = new_review_empty();
    new->reviewID = strdup(reviewID);
    return new;
}

StrReview new_review_fromLine (char *buff){
    int invalid = 0;char *holder;
    StrReview new = new_review_empty();

    new->reviewID = strdup(strsep(&buff,";"));
    if(strlen(new->reviewID) == 0) invalid = 1;

    new->userID = strdup(strsep(&buff,";"));
    if(strlen(new->userID) == 0) invalid = 1;

    new->businessID = strdup(strsep(&buff,";"));
    if(strlen(new->businessID) == 0) invalid = 1; 

    holder = strdup(strsep(&buff,";"));
    if(strlen(holder) == 0) invalid = 1;
    new->stars = atof(holder);
    free(holder);
    
    holder = strdup(strsep(&buff,";"));
    if(strlen(holder) == 0) invalid = 1;
    new->useful = atoi(holder);
    free(holder);
    
    holder = strdup(strsep(&buff,";"));
    if(strlen(holder) == 0) invalid = 1;
    new->funny = atoi(holder);
    free(holder);
    
    holder = strdup(strsep(&buff,";"));
    if(strlen(holder) == 0) invalid = 1;
    new->cool = atoi(holder);
    free(holder);
    
    new->date = strdup(strsep(&buff,";"));
    if(strlen(new->date) == 0) invalid = 1;


    new->texto = strdup(strsep(&buff,"\n"));

    if (!invalid) return new;
    else {
        freeReview(new);
        return NULL;
    }
}

char* reviewToString (StrReview target,int arr[]){ 
    int size = 1;
    char *rid=NULL,*uid=NULL,*bid=NULL,*stars=NULL,*useful=NULL;
    char *funny=NULL,*cool=NULL,*date=NULL,*text=NULL;
    if(arr[0]) {
        rid = get_reviewID(target);
        size += strlen(rid)+1;}
    if(arr[1]) {
        uid = get_reviewUserID(target);
        size += strlen(uid)+1;}
    if(arr[2]) {
        bid = get_reviewBusinessID(target);
        size += strlen(bid)+1;}
    if(arr[3]) {
        stars = malloc(sizeof(char) * 10);
        sprintf(stars,"%.1f",get_reviewStars(target));
        size += strlen(stars)+1;}
    if(arr[4]) {
        useful = malloc(sizeof(char) * 10);
        sprintf(useful,"%d",get_reviewUseful(target));
        size += strlen(useful)+1;}
    if(arr[5]) {
        funny = malloc(sizeof(char) * 10);
        sprintf(funny,"%d",get_reviewFunny(target));
        size += strlen(funny)+1;}
    if(arr[6]) {
        cool = malloc(sizeof(char) * 10);
        sprintf(cool,"%d",get_reviewCool(target));
        size += strlen(cool)+1;}
    if(arr[7]) {
        date = get_reviewDate(target);
        size += strlen(date)+1;}
    if(arr[8]) {
        text = get_reviewTexto(target);
        size += strlen(text)+1;
        }
    char * res = malloc(sizeof(char)*size);
    res[0] = '\0';

    if(arr[0]) {
        strcat(res,rid);free(rid);
        if(arr[1] || arr[2] || arr[3] || arr[4] || arr[5] || arr[6] || arr[7] || arr[8]) strcat(res,";");}
    if(arr[1]) {
        strcat(res,uid);free(uid);
        if(arr[2] || arr[3] || arr[4] || arr[5] || arr[6] || arr[7] || arr[8]) strcat(res,";");}
    if(arr[2]) {
        strcat(res,bid);free(bid);
        if(arr[3] || arr[4] || arr[5] || arr[6] || arr[7] || arr[8]) strcat(res,";");}
    if(arr[3]) {
        strcat(res,stars);free(stars);
        if(arr[4] || arr[5] || arr[6] || arr[7] || arr[8]) strcat(res,";");}
    if(arr[4]) {
        strcat(res,useful);free(useful);
        if(arr[5] || arr[6] || arr[7] || arr[8]) strcat(res,";");}
    if(arr[5]) {
        strcat(res,funny);free(funny);
        if(arr[5] || arr[6] || arr[7]) strcat(res,";");}
    if(arr[6]) {
        strcat(res,cool);free(cool);
        if(arr[5] || arr[6]) strcat(res,";");}
    if(arr[7]) {
        strcat(res,date);free(date);
        if(arr[5]) strcat(res,";");}
    if(arr[8]) {
        strcat(res,text);free(text);}
    
    return res;
}

char* get_reviewID(StrReview target){
    return strdup(target->reviewID);
}

char* get_reviewUserID(StrReview target){
    return strdup(target->userID);
}

char* get_reviewBusinessID(StrReview target){
    return strdup(target->businessID);
}

char* get_reviewTexto(StrReview target){
    return strdup(target->texto);
}

double get_reviewStars(StrReview target){
    return target->stars;
}

int get_reviewUseful(StrReview target){
    return target->useful;
}

int get_reviewFunny(StrReview target){
    return target->funny;
}

int get_reviewCool(StrReview target){
    return target->cool;
}

char* get_reviewDate(StrReview target){
    return strdup(target->date);
}

void freeReview(StrReview target){
    free(target->reviewID);
    free(target->userID);
    free(target->businessID);
    free(target->date);
    free(target->texto);
    free(target);
}

StrReview cloneReview(StrReview target){
    if (target == NULL) return NULL;
    StrReview new = new_review_empty();
    new->reviewID = get_reviewID (target);
    new->userID = get_reviewUserID(target);
    new->businessID = get_reviewBusinessID(target);
    new->stars = get_reviewStars(target);
    new->useful = get_reviewUseful(target);
    new->funny = get_reviewFunny(target);
    new->cool = get_reviewCool(target);
    new->date = get_reviewDate(target);
    new->texto = get_reviewTexto(target);
    return new;
}
