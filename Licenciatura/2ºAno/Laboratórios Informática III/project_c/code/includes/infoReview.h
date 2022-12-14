/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * API que organiza e lista as várias Reviews. 
 * Esta usa a estrutura "GHashTable" para manter a sua organização.
 */

#ifndef INFOREVIEW
#define INFOREVIEW

#include <glib.h>
#include "FuncAux.h"
#include "strReview.h"
#include "infoUser.h" //PARA VALIDACAO
#include "infoBusiness.h"

/**
 * HashTable Completa de Reviews
 */
typedef GHashTable* ReviewInfo;

/**
 * Inicializa a HashTable
 */
ReviewInfo newReviewInfo();

/**
 * Adiciona membro à HashTable
 */
void addReview (ReviewInfo h, char* id, StrReview review);

/**
 * Função de verificação se o review existe na lista
 */
int contains_business (ReviewInfo h, char* reviewID);

/**
 * Função de pesquisa de reviews. Devolve a review clonada.
 */
StrReview findReview (char* id, ReviewInfo lista);

/**
 * Função para percorrer a lista, aplicando uma função
 * Esta Função é do gênero:
 * void FUNC (gpointer key,gpointer value,gpointer user_data);
 * Os 3 argumentos devem ser casted e depois utilizados. Não se pode modificar
 * a HashTable em si.
 */
void infoReview_foreach (ReviewInfo Rev,GHFunc func,gpointer user_data);

/**
 * Função para libertar a memória
 */
void freeReviewInfo (ReviewInfo h);

/**
 * Função de leitura das reviews, com verificação dos users e businesses
 * Esta armazena o número de elementos lidos para total
 */
ReviewInfo load_ReviewInfo(char* filepath,int* total,UserInfo usr,BusinessInfo biz);

#endif
