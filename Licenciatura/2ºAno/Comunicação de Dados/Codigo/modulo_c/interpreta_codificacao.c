#include "interpreta_codificacao.h"


Comprimidos le_caracter (FILE *original, Digitos_Binarios *array_codificado, int max, int tam_bloco, int *tam_bloco_comprimido) {
    Comprimidos inicio, ultimo;
    ultimo = inicio = NULL;
    int buffer[max+7]; //tamanho de codificacao maxima mais os 7 bits (tamanho do pior caso): os 8 primeiros bits ainda não estão preenchidos e passamos para a adição da codificação de tamanho maior no buffer.
    int contador = 0;
    *tam_bloco_comprimido = 0;
    Digitos_Binarios codificacao;
    unsigned char caracter, codificado;
    for (int i = 0; i < tam_bloco; i++) {
        if (fscanf (original, "%c", &caracter)){};
        codificacao = array_codificado [(int) caracter];
        contador = copia_digitos (codificacao, buffer, contador);
        if (contador >= 8) {
            while (contador >=8 ) {
                *tam_bloco_comprimido = (*tam_bloco_comprimido) + 1;
                codificado = codifica_bits(buffer);
                contador = reset_buffer(buffer, contador);
                if (inicio == NULL) {
                    inicio = adiciona_comprimidos(codificado, inicio, ultimo);
                    ultimo = inicio;
                } else {
                    inicio = adiciona_comprimidos(codificado, inicio, ultimo);
                    ultimo = devolve_proximo(ultimo);
                }
            }
        }
    }
    if (contador != 0) {
        *(tam_bloco_comprimido) += 1;
        codificado = codifica_bits (buffer);
        inicio = adiciona_comprimidos (codificado, inicio, ultimo);
    }
    return inicio;
}


unsigned char codifica_bits (int b[]) {
    unsigned char x;
    int valores[8] = {128, 64, 32, 16, 8, 4, 2, 1};
    int r = 0;
    for (int i = 0; i < 8; i++) {
        if (b[i] == 1) r += valores [i];
    }
    x = (unsigned char) r;
    return x;
}

int copia_digitos (Digitos_Binarios indice, int *b, int cont) {
    while (indice != NULL) {
        b[cont] = le_digito (indice);
        cont++;
        indice = proximo_digitos (indice);
    }
    return cont;
}


int reset_buffer (int *c, int cont) {
    for (int i = 8; i < cont; i++) c[i-8] = c[i];
    cont = cont - 8;
    return cont;
}
