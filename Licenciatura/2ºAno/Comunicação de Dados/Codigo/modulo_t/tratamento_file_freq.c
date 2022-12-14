#include "tratamento_file_freq.h"


int ler_freq (FILE *ficheiro,int *frequencias) {
    int tam_bloco, i,controlo,num_anterior,numero;
    num_anterior = 0;

    if (fscanf(ficheiro,"%d@",&tam_bloco)){}; //Lê tamanho do bloco

    for (i = 0; i < 256; i++) {

        controlo = fscanf(ficheiro, "%d",&numero);  //controlo. Devolve 1 se leu um número 0 se encontrou um ';'

        if (controlo != 1) {
            frequencias[i] = num_anterior;  //Se leu ';' utiliza a frequência anterior e guarda no array
        }
        else {
            frequencias[i] = numero;        //Se leu um número, guarda-o no array e atualiza a "frequência anterior"
            num_anterior = numero;
        }
        if (fscanf(ficheiro,";")){};

    }
    if(fscanf(ficheiro,"@")){};
    return tam_bloco;
}