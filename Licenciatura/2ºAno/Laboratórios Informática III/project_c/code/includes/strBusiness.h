/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * API responsável pela manipulação da estrutura dos negócios.
 */

#ifndef STRBUSINESS
#define STRBUSINESS

#include <glib.h>

/**
 * Estrutura de dados de Business
 */
typedef struct strbusiness* StrBusiness;

/**
 * Função de comparação/ordenação. Faz strcmp aos businessIDs
 */
gint busscmp(gconstpointer a,gconstpointer b,gpointer data);

/**
 * Novo business vazio. Apenas aloca memória
 */
StrBusiness new_business_empty();

/**
 * Novo business com ID dado. Util para gerar keys de comparação
 */
StrBusiness new_business_businessID(char* businessID);

/**
 * Função principal de leitura do ficheiro. Lê uma linha, assumindo que esta respeita
 * a estrutura correta e devolve o Business completo.
 */
StrBusiness new_business_fromLine (char *buff);

/**
 * Função que devolve o Business em formato String.
 */
char* businessToString (StrBusiness target, int arr[]);

/**
 * Devolve o businessID
 */
char* get_businessID(StrBusiness target);

/**
 * Devolve o nome do negócio
 */
char* get_businessName(StrBusiness target);

/**
 * Devolve a cidade do negócio
 */
char* get_businessCity(StrBusiness target);

/**
 * Devolve o estado residente do negócio
 */
char* get_businessState(StrBusiness target);

/**
 * Devolve o número de categorias
 */
int get_businessNumCategoria(StrBusiness target);

/**
 * Devolve a lista de categorias do negócio
 */
char* get_businessCategorias(StrBusiness target);

/**
 * Liberta a memória
 */
void freeBusiness(StrBusiness target);

/**
 * Função Clone
 */
StrBusiness cloneBusiness(StrBusiness target);

#endif
