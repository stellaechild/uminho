/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * API que organiza e lista os vários Businesses. 
 * Esta usa a estrutura "GHashTable" para manter a sua organização.
 */

#ifndef INFOBUSINESS
#define INFOBUSINESS

#include <glib.h>
#include "strBusiness.h"
#include "FuncAux.h"


/**
 * HashTable Completa de Negócios
 */
typedef GHashTable* BusinessInfo;

/**
 * Inicializa a HashTable
 */
BusinessInfo newBusinessInfo();

/**
 * Adiciona membro à HashTable
 */
void addBusiness (BusinessInfo h, char* id, StrBusiness bus);

/**
 * Função de verificação se o business existe na lista
 */
int contains_business (BusinessInfo h, char* businessID);

/**
 * Função de pesquisa de negócios. Devolve o negócio clonado.
 */
StrBusiness findBusiness (char* id, BusinessInfo lista);

/**
 * Função para percorrer a lista, aplicando uma função
 * Esta Função é do gênero:
 * void FUNC (gpointer key,gpointer value,gpointer user_data);
 * Os 3 argumentos devem ser casted e depois utilizados. Não se pode modificar
 * a HashTable em si.
 */
void infoBusiness_foreach (BusinessInfo Rev,GHFunc func,gpointer user_data);

/**
 * Função para libertar a memória
 */
void freeBusinessInfo (BusinessInfo h);

/**
 * Função de leitura dos negócios
 */
BusinessInfo load_BusinessInfo(char* filepath,int* total);

#endif
