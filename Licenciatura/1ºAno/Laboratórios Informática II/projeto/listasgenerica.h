#ifndef LI2PL11_LISTASGENERICA_H
#define LI2PL11_LISTASGENERICA_H

/**
@file listagenerica.h
Este módulo é utilizado para a definição e utilização de listas genéricas.
*/

/**
\brief Define LISTA como um apontador para a estrutura da lista.
*/
typedef struct nodo* LISTA;

/**
\brief Estrutura da lista genérica.
*/
struct nodo {
    void *val;
    LISTA proximo;
};

/**
\brief Função que inicializa uma lista vazia.
@return Lista criada.
*/
LISTA cria_lista();

/**
\brief Função para inserir elementos na cabeça da lista.
@param L Lista a ser atualizada.
@param *valor Apontador para o valor a ser adicionado à lista.
@return Lista atualizada.
*/
LISTA insere_cabeca(LISTA L, void *valor);

/**
\brief Função que devolve a cabeça de uma lista.
@param L Lista a ser analisada.
@return Apontador para o valor da cabeça da lista.
*/
void *devolve_cabeca(LISTA L);

/**
\brief Função que avança a lista para o seu elemento seguinte.
@param L Lista a ser modificada.
@return Lista modificada.
*/
LISTA proximo(LISTA L);

/**
\brief Função que remove a cabeça de uma lista devolvendo o elemento seguinte.
@param L Lista a ser modificada.
@return Lista sem o elemento à cabeça.
*/
LISTA remove_cabeca(LISTA L);

/**
\brief Função que avalia se uma lista se encontra vazia (0 = Vazia).
@param L Lista a ser testada.
@return Valor boleano.
*/
int lista_esta_vazia(LISTA L);

/**
\brief Função que liberta todos os elementos da lista, apagando esta da memória.
@param L Lista a ser libertada.
*/
void liberta_lista(LISTA L);

#endif //LI2PL11_LISTASGENERICA_H
