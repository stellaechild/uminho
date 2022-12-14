/**
 * Data de criação 08/04/2021
 * Autor: 
 * Vicente Moreira - A93296
 * Joana Alves -A93290
 * Versão: 20210417
 * 
 * API do SGR, cabeça do MODELO. Esta contém todas as Queries requeridas.
 */


#ifndef SGRINC
#define SGRINC

#include <glib.h>
#include "strUser.h"
#include "strBusiness.h"
#include "strReview.h"
#include "infoUser.h"
#include "infoBusiness.h"
#include "infoReview.h"
#include "table.h"
#include "stats.h"
#include "paginacao.h"
#include "queryarg.h"

typedef struct sgr *SGR; 

/**
 * Aloca SGR
 */
SGR init_sgr ();

/**
 * Várias funções de leitura. Usadas pelo interpretador para controlo.
 */
int sgr_user_valido (SGR sgr);
int sgr_bus_valido (SGR sgr); 
int sgr_rev_valido (SGR sgr);
int sgr_stats_valido (SGR sgr);
int sgr_stats_numUsr (SGR sgr);
int sgr_stats_numBiz (SGR sgr);
int sgr_stats_numRev (SGR sgr); 

/**
 * Liberta SGR
 */
void free_SGR (SGR sgr);

/** QUERY 1
 * Dado o caminho para os 3 ficheiros (Users, Businesses, e Reviews), ler o seu
 * conteúdo e carregar as estruturas de dados correspondentes. Durante a interação com o
 * utilizador (no menu da aplicação), este poderá ter a opção de introduzir os paths
 * manualmente ou, opcionalmente, assumir um caminho por omissão. Note-se que qualquer
 * nova leitura destes ficheiros reinicia e refaz as estruturas de dados em memória
 * como se de uma reinicialização se tratasse.
 */
SGR load_sgr(char*users,char*businesses,char*reviews);

/** QUERY 2
 * Determinar a lista de nomes de negócios e o número total de negócios cujo nome
 * inicia por uma dada letra. A procura não é case sensitive.
 */ 
TABLE businesses_started_by_letter (SGR sgr,char letter);

/** QUERY 3
 * Dado um id de negócio, determinar a sua informação: nome, cidade, estado, stars,
 * e número total reviews.
 */ 
TABLE business_info (SGR sgr,char*business_id);

/** QUERY 4
 * Dado um id de utilizador, determinar a lista de negócios aos quais fez review. A
 * informação associada a cada negócio é o id e o nome.
 */
TABLE businesses_reviewed (SGR sgr,char*user_id);

/** QUERY 5
 * Dado um número n de stars e uma cidade, determinar a lista de negócios com n ou mais 
 * stars na dada cidade. A informação associada a cada negócio é o seu id e nome.
 */
TABLE businesses_with_stars_and_city (SGR sgr,float stars,char*city);

/** QUERY 6
 * Dado um número inteiro n, determinar a lista dos top n negócios (tendo em conta
 * o número médio de stars) em cada cidade. A informação associada a cada negócio é
 * o seu id, nome, e número de estrelas.
 */
TABLE top_businesses_by_city (SGR sgr,int top);

/** QUERY 7
 * Determinar a lista de ids de utilizadores e o número total de utilizadores que tenham 
 * visitado mais de um estado, i.e. que tenham feito reviews em negócios de diferentes estados.
 */
TABLE international_users (SGR sgr);

/** QUERY 8
 * Dado um número inteiro n e uma categoria, determinar a lista dos top n negócios 
 * (tendo em conta o número médio de stars) que pertencem a uma determinada categoria. A 
 * informação associada a cada negócio é o seu id, nome, e número de estrelas.
 */
TABLE top_businesses_with_category (SGR sgr,int top,char*category);

/** QUERY 9
 * Dada uma palavra, determinar a lista de ids de reviews que a referem no campo text.
 */
TABLE reviews_with_word(SGR sgr,int top,char *word);

#endif
