#include "tratamento_file_cod.h"

void escreve_cod_bloco(FILE *ficheiro, Casas_de_Simbolos topo, int tam_bloco){
    unsigned char comparacao;
    Casas_de_Simbolos percorre;

    //Escreve tamanho do bloco
    fprintf(ficheiro,"%d",tam_bloco);
    fprintf(ficheiro,"@");

    //Loop de procura dos símbolos na lista de Casa de Símbolos por ordem (0 a 255).
    for (int aux = 0; aux < 256;aux++){
        comparacao = (unsigned char) aux;
        percorre = topo;
        while ( percorre != NULL && le_casa_simbolo(percorre) != comparacao){ //Percorre a lista
            percorre = proxima_casa(percorre);
        }
        if (percorre != NULL){
            //Se o símbolo existir na lista escreve a sua codificação
            escreve_codificacao(ficheiro,percorre);
        }
        if (aux != 255) fprintf(ficheiro,";");
    }
    fprintf(ficheiro,"@");
}


void escreve_codificacao(FILE *ficheiro,Casas_de_Simbolos codificacao){
    Digitos_Binarios temporario = le_casa_codigo(codificacao);
    int digito;
    while (temporario != NULL){                 //Escreve a codificação toda do símbolo no ficheiro
        digito = le_digito(temporario);
        fprintf(ficheiro,"%d",digito);
        temporario = proximo_digitos(temporario);
    }
}