#include <stdlib.h>
#include "listasgenerica.h"

LISTA cria_lista(){
    LISTA tmp;
    tmp = malloc(sizeof (struct nodo));
    tmp->val=NULL;
    tmp->proximo=NULL;
    return tmp;
}

LISTA insere_cabeca(LISTA L, void *valor){
LISTA tmp = cria_lista();
 tmp->val = valor;
 tmp->proximo = L;
 return tmp;
}

void *devolve_cabeca(LISTA L){
    void* tmp = L->val;
    return tmp;
}

LISTA proximo(LISTA L){
    return L->proximo;
}

LISTA remove_cabeca(LISTA L){
    LISTA tmp = L->proximo;
    free(L->val);
    free(L);
    return tmp;
}

int lista_esta_vazia(LISTA L){
    if (L->val == NULL) return 0;
    else return 1;
}

void liberta_lista(LISTA L){
    while (lista_esta_vazia(L) != 0){
        L = remove_cabeca(L);
    }
    free(L);
}