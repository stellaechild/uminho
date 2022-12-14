/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * API responsável pela manipulação da estrutura dos utilizadores
 */

#ifndef STRUSER
#define STRUSER

#include <glib.h>
 
/**
 * Estrutura de dados de Users
 */
typedef struct struser* StrUser;

/**
 * Função de comparação/ordenação. Faz strcmp aos userIDs
 */
gint usercmp(gconstpointer a,gconstpointer b,gpointer data);

/**
 * Novo user vazio. Apenas aloca memória
 */
StrUser new_user_empty();

/**
 * Novo user com ID dado. Util para gerar keys de comparação
 */
StrUser new_user_userID(char* userID);

/**
 * Função principal de leitura do ficheiro. Lê uma linha, assumindo que esta respeita
 * a estrutura correta e devolve o User completo.
 * Field friends se for preciso a leitura de amigos.
 */
StrUser new_user_fromLine(char *buff,int friends);

/**
 *Função que devolve o User em formato String.
 */
char* userToString (StrUser target, int arr[]);

/**
 * Devolve o userID
 */
char* get_userID(StrUser target);

/**
 * Devolve o nome do utilizador
 */
char* get_userName(StrUser target);

/**
 * Devolve o número de amigos
 */
int get_userNumFriends(StrUser target);

/**
 * Devolve a lista de userID amigos do user target
 */
char** get_userFriendsIDs(StrUser target);

/**
 * Liberta a memória
 */
void freeUser(StrUser target);

/**
 * Função Clone
 */
StrUser cloneUser(StrUser target);

#endif
