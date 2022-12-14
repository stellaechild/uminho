#ifndef FUNCRLE
#define FUNCRLE 

#include "functions.h"
#include "functions_freq.h"

/**
@file functions_rle.h
 Este módulo contém funções relacionadas à geração do RLE
*/

/**
\brief Faz o calculo do primerio bloco das frequencias do ficheiro original e do rle,
 e verifica se compensa a compressao rle
@param *txt - buffer do primeiro bloco do ficheiro original
@param tam -  Tamanho do buffer
@returns 1 - RLE / 0 - NÃO COMPENSA A COMPRESSÃO
 */
int rle_or_not(unsigned char *txt, int tam);

/**
\brief Faz a escrita do file .rle, e guarda as frequencias dos simbolos no buffer
@param *rle - File .rle aberto , para ser escrito
@param *ftxt - File original aberto , para ser lido
@param *buffer_freq - buffer que guarda as frequencias dos simbolos
@param tam - Tamanho do bloco a ser lido
@returns Frequencia/tamanho total do bloco escrito no .rle
 */
int escreveRle(FILE *rle, FILE *ftxt,int *buffer_freq, int tam);



#endif