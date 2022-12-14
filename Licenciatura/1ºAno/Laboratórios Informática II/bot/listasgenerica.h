
#ifndef LI2PL11_LISTASGENERICA_H
#define LI2PL11_LISTASGENERICA_H

typedef struct nodo* LISTA;

struct nodo {
    void *val;
    LISTA proximo;
};

LISTA cria_lista();

LISTA insere_cabeca(LISTA L, void *valor);

void *devolve_cabeca(LISTA L);

LISTA proximo(LISTA L);

LISTA remove_cabeca(LISTA L);

int lista_esta_vazia(LISTA L);

void liberta_lista(LISTA L);

#endif //LI2PL11_LISTASGENERICA_H
