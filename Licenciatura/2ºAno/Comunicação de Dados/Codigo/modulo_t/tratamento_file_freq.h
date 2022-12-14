#ifndef SHAFT_TRATAMENTO_FILE_FREQ_H
#define SHAFT_TRATAMENTO_FILE_FREQ_H

#include "../comum.h"
#include "t_estruturas.h"

/**
@file tratamento_file_freq.h
Este ficheiro contêm a função de leitura do ficheiro .FREQ.
 Data de Criação: 8 de Dezembro.
 Autores: David Duarte.
*/

/**
\brief Função principal de leitura do bloco do ficheiro .FREQ, esta guarda o tamanho do bloco e regista num array as
 várias frequências lidas.
@param ficheiro - Apontador para o ficheiro onde será lido.
@param frequencias - Apontador para o array onde são armazenados as frequências.
@return Tamanho do bloco lido.
 */
int ler_freq (FILE *ficheiro,int *frequencias);

#endif //SHAFT_TRATAMENTO_FILE_FREQ_H
