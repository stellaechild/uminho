#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#define TAM 65536 // TAMANHO PADRÃO DE CADA BLOCO (64KB)
#define TAM_MIN 1024 // TAMANHO MIN (1KB)

/**
@file functions.h
Este modulo contém funcoes gerais que sao uteis para o funcionamento do progama e as structs necessárias 
*/


/**
\brief Struct que Guarda número de blocos, tamanho de cada bloco, tamanho total e tamanho do ultimo bloco do ficheiro 
 */
typedef struct bloco{
    unsigned int tam_total;     
    unsigned int tam_blocks;
    unsigned int tam_lastBlock;
    int n_blocks;
}*Blocks;

/**
\brief Struct de listas ligadas que Guarda tamanho de cada bloco do ficheiro.rle 
 */
typedef struct rleblock{
    int tam_block;
    struct rleblock *prox;       
}*RleBlock;


/**
\brief Faz o free da struct rleblock
@param *rleb - Apontador para a struct rleblock
 */
void freeRleBlock(RleBlock *rleb);

/**
\brief Guarda o tamanho de um bloco Rle na struct rleblock
@param *rleb - Apontador para a struct rleblock
@param tamB - Tamanho do Bloco Rle
 */
void novo_rleBlock(RleBlock *rleb, int tamB);

/**
\brief Calcula a compressão Rle
@param freq_rle - Frequencia/Tamanho do ficheiro RLE
@param freq_txt - Frequencia/Tamanho do ficheiro original
@returns Compressão RLE
 */
float verifica_compressao(int freq_rle, int freq_txt);

/**
\brief Faz o print no terminal da inferface final após a execução do progama
@param txt - Estrutura Blocks do arquivo original, com seus respectivos tamanhos
@param rle - Estrutura RLeBlock com os tamanhos de cada bloco RLE
@param *name - Nome do arquivo original
@param double execTime - Tempo de execucao do programa ate o momento 
@param tamRle_file - Tamanho total do arquivo .rle
 */
void interface (Blocks txt, RleBlock rle, const char *name, double execTime,int tamRle_file);

/**
\brief Verifica se há algum erro na leitura de um arquivo
@param *name - Nome do arquivo
@returns 0 -> ERRO ; 1 -> SUCESSO
 */
int error_file(const char *name);

/**
\brief Calcula a partir do ficheiro original os tamanhos necessários para a struct Blocks
@param *b - Struct Blocks que irá ser guardado os tamanhos 
@param *name - Nome do arquivo
@param tam - Tamanho do bloco que o usuário solicitou 
 */
void calcula_tamBlock(Blocks *b, const char *name,char tam);

/**
\brief Calcula as frequencias de cada simbolo 
@param *buffer_freq - buffer que irá ser guardado as frequencias
@param *string - buffer que foi guardado o bloco do ficheiro original
@param tam - Tamanho do buffer *string
 */
void get_Simb(int * buffer_freq,unsigned char *string,int tam);


#endif