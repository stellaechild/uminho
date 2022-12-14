/**
 * Data de criação: 01/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * API que organiza e lista os vários Users. 
 * Esta usa a estrutura "GHashTable" para manter a sua organização.
 */

#ifndef INFOUSERS
#define INFOUSERS

#include <glib.h> 
#include "strUser.h"
#include "FuncAux.h"

 
/**
 * HashTable Completa de Utilizadores
 */
typedef GHashTable* UserInfo;

/**
 * Inicializa a HashTable
 */
UserInfo newUserInfo();

/**
 * Adiciona membro à HashTable
 */
void addUser (UserInfo h, char* id, StrUser user);

/**
 * Função de verificação se o user existe na lista
 */
int contains_user (UserInfo h, char* userID);

/**
 * Função de pesquisa de utilizadores. Devolve utilizador clonado.
 */
StrUser findUser(char* userID, UserInfo lista);

/**
 * Função para percorrer a lista, aplicando uma função
 * Esta Função é do gênero:
 * void FUNC (gpointer key,gpointer value,gpointer user_data);
 * Os 3 argumentos devem ser casted e depois utilizados. Não se pode modificar
 * a HashTable em si.
 */
void infoReview_foreach (UserInfo Rev,GHFunc func,gpointer user_data);

/**
 * Função para libertar a memória
 */
void freeUserInfo(UserInfo lista);

/**
 * Função de leitura dos utilizadores.
 * O número de users lidos/válido é guardado no total
 * Field friends para ativar a leitura de amigos
 */
UserInfo load_UserInfo(char* filepath,int* total,int friends);

#endif
