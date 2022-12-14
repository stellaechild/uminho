#include "t_estruturas.h"

Casas_de_Simbolos novo_simbolo(){
    Casas_de_Simbolos novo = (Casas_de_Simbolos) malloc(sizeof(Casas_de_Simbolos));
    novo->prox = NULL;
    novo->codigo = NULL;
    return novo;
}

void adiciona_nova_casa(Casas_de_Simbolos target){
    target->prox = novo_simbolo();
}



void escreve_casa_simbolo(unsigned char simbolo,Casas_de_Simbolos target){
    target->simbolo = simbolo;
}

void escreve_casa_freq(int freq,Casas_de_Simbolos target){
    target->frequencia = freq;
}

Casas_de_Simbolos acrescenta_digito_casa(short int digito, Casas_de_Simbolos casa){
    Digitos_Binarios novo = (Digitos_Binarios) malloc(sizeof(Digitos_Binarios));
    novo->digito = digito;
    novo->prox = NULL;

    if (casa->codigo == NULL) casa->codigo = novo;
    else {
        Digitos_Binarios tmp = casa->codigo;
        while (tmp->prox != NULL) tmp = proximo_digitos (tmp);
        tmp->prox = novo;
    }
    return casa;
}



unsigned char le_casa_simbolo(Casas_de_Simbolos target){
    unsigned char r;
    r = target->simbolo;
    return r;
}

int le_casa_frequencia(Casas_de_Simbolos target){
    int r = target->frequencia;
    return r;
}

Digitos_Binarios le_casa_codigo(Casas_de_Simbolos target){
    Digitos_Binarios r;
    r = target->codigo;
    return r;
}



Casas_de_Simbolos proxima_casa(Casas_de_Simbolos target){
    Casas_de_Simbolos proximo = target->prox;
    return proximo;
}

void liberta_casa(Casas_de_Simbolos target){
    if (target->codigo !=NULL) liberta_digitos(target->codigo);
    if (target->prox != NULL) liberta_casa(target->prox);
    free (target);
}