#ifndef SHAFT_ESTRUTURAS_H
#define SHAFT_ESTRUTURAS_H
#include "../comum.h"

/**
@file estruturas.h
Este módulo contém funções que manipulam as estruturas criadas.
 Data de Criação: 7 de dezembro.
 Autores: Joana Alves e Maria Cunha.
*/

/**
\brief Estrutura de dados usada para armazenar os caracteres depois de comprimidos.
 */
typedef struct byte {
    unsigned char simbolo;
    struct byte *prox;
} *Comprimidos;


/**
\brief Função que adiciona um novo nodo (contendo o caracter c) à lista ligada inicio de tipo Comprimidos (sendo ultimo o apontador
 para o último nodo da mesma). Aquando da inserção do último caracter comprimido, a lista ligada contém todos os caracters comprimidos
 prontos para escrita no ficheiro .shaf.
@param c Caracter comprimido a ser adicionado.
@param inicio Primeiro nodo da lista ligada alvo.
@param ultimo Último nodo da lista ligada alvo.
@return Apontador para a estrutura criada (atualizada).
 */
Comprimidos adiciona_comprimidos (unsigned char c, Comprimidos inicio, Comprimidos ultimo);


/**
\brief Função que cria um array de tipo Digitos_Binarios contendo em cada indice a codificação criada no módulo anterior correspondente ao
 caracter. Por exemplo, se o ficheiro .cod contiver codificação para o caracter '0', a mesma estará armazenada no índice 48
 do array, pois, pela tabela ASCII, o caracter '0' corresponde ao número 48.
@param ficheiro_cod Ficheiro .cod proveniente do módulo anterior, alvo da extração.
@param array Array para armazenamento da codificação.
@return Tamanho máximo de codificação.
 */
int cria_array_codificado (FILE *ficheiro_cod, Digitos_Binarios *array);


/**
\brief Função que dada uma estrutura do tipo Comprimidos, retorna o caracter armazenado na mesma.
@param inicio Estrutura de dados alvo.
@return "Símbolo" da estrutura.
 */
unsigned char acesso_simbolos_comprimidos (Comprimidos inicio);


/**
\brief Função que dada uma estrutura do tipo Comprimidos, retorna a próxima estrutura ligada à mesma.
@param inicio Estrutura alvo.
@return Apontador para a próxima estrutura.
 */
Comprimidos devolve_proximo (Comprimidos inicio);

#endif
