#include "estruturas.h"


Comprimidos adiciona_comprimidos (unsigned char c, Comprimidos inicio, Comprimidos ultimo) {
    Comprimidos novo = (Comprimidos) malloc (sizeof (Comprimidos));
    novo->simbolo = c;
    novo->prox = NULL;

    if (inicio == NULL) inicio = novo;
    else {
        ultimo->prox = novo;
    }
    return inicio;
}

int cria_array_codificado (FILE *ficheiro_cod, Digitos_Binarios *array) {
    int ascii = 0, max = 0, counter = 0;
    unsigned char caracter;

    for(int i=0; i<256; i++) array[i] = NULL; //inicializa o array vazio

    for(; ascii < 256; ascii++) {
        if (fscanf(ficheiro_cod, "%c", &caracter)){}
        while (caracter != ';' && caracter != '@') {
            array[ascii] = acrescenta_digito(caracter - 48, array[ascii]);
            counter++;
            if (fscanf(ficheiro_cod, "%c", &caracter)){};
        }
        if (counter > max) max = counter;
        counter = 0;
    }
    return max;
}

unsigned char acesso_simbolos_comprimidos (Comprimidos inicio) {
    return ((unsigned char) inicio->simbolo);
}

Comprimidos devolve_proximo (Comprimidos inicio) {
    return (inicio->prox);
}

