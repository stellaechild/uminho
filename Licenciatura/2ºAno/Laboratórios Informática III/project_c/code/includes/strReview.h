/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Versão: 20210417
 * 
 * API responsável pela manipulação da estrutura das reviews
 */

#ifndef STRREVIEW
#define STRREVIEW

#include <glib.h>

/**
 * Estrutura de dados de Review
 */
typedef struct strreview* StrReview;

/**
 * Função de comparação/ordenação. Faz strcmp aos reviewIDs
 */
gint reviewcmp(gconstpointer a,gconstpointer b,gpointer data);

/**
 * Nova review vazia. Apenas aloca memória
 */
StrReview new_review_empty();

/**
 * Nova review com ID dado. Util para gerar keys de comparação
 */
StrReview new_review_reviewID(char* reviewID);

/**
 * Função principal de leitura do ficheiro. Lê uma linha, assumindo que esta respeita
 * a estrutura correta e devolve a review completa.
 */
StrReview new_review_fromLine (char *buff);

/**
 * Devolve a revie formatada numa linha
 */
char* reviewToString (StrReview target,int arr[]);

/**
 * Devolve a reviewID
 */
char* get_reviewID(StrReview target);

/**
 * Devolve o userID autor da review
 */
char* get_reviewUserID(StrReview target);

/**
 * Devolve o businessID da review
 */
char* get_reviewBusinessID(StrReview target);

/**
 * Devolve o texto da review
 */
char* get_reviewTexto(StrReview target);

/**
 * Devolve as "stars" da review
 */
double get_reviewStars(StrReview target);

/**
 * Devolve o rating "Useful" da review
 */
int get_reviewUseful(StrReview target);

/**
 * Devolve o rating "Funny" da review
 */
int get_reviewFunny(StrReview target);

/**
 * Devolve o rating "Cool" da review
 */
int get_reviewCool(StrReview target);

/**
 * Devolve a data
 */
char* get_reviewDate(StrReview target);

/**
 * Liberta a memória
 */
void freeReview(StrReview target);

/**
 * Função Clone
 */
StrReview cloneReview(StrReview target);

#endif
