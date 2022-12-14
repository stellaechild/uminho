#ifndef FUNCFREQ
#define FUNCFREQ  

#include "functions.h"

/**
@file functions_freq.h
 Este módulo contém funções necessárias para geração do ficheiro .freq
*/

/**
\brief Função principal para a escrita do ficheiro .freq
@param *buffer - buffer com frequencias de cada simbolo
@param tamBloco -  Tamanho do Bloco
@param last - Inteiro binario para verificar se já está no ultimo bloco ou não 
@param *f - Ficheiro .freq aberto para a escrita
 */
void do_freq(int* buffer, int tamBloco, int last,FILE *f);

/**
\brief Faz a escrita das frequencias dos simbolos no ficheiro .freq
@param *escreve - Ficheiro .freq aberto para a escrita
@param *buffer -  buffer com frequencias de cada simbolo
 */
void freq_escreve(FILE *escreve,int* buffer);

/**
\brief Faz a escrita inicial do ficheiro .freq
@param *f - Ficheiro .freq aberto para a escrita
@param r_n -  RLE ou Nomal
@param n_blocks - Números de blocos totais do ficheiro
 */
void inicia_freq(FILE *f, unsigned char r_n,int n_blocks);


#endif