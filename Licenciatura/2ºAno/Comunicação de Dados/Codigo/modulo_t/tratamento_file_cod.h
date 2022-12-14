#ifndef SHAFT_TRATAMENTO_FILE_COD_H
#define SHAFT_TRATAMENTO_FILE_COD_H

#include "../comum.h"
#include "t_estruturas.h"

/**
@file tratamento_file_cod.h
Este ficheiro contêm as funções de escrita no ficheiro .COD.
 Data de Criação: 8 de Dezembro.
 Autores: David Duarte.
*/


/**
\brief Função principal de escrita do bloco do ficheiro .COD, esta recebe a lista de Casa de Simbolos preparada
 assim como o tamanho do bloco em questão e escreve por completo um bloco no ficheiro.
@param ficheiro - Apontador para o ficheiro onde será escrito.
@param topo - Apontador para o inicio da lista ligada de Casa de Simbolos.
@param tam_bloco - Tamanho do bloco.
 */
void escreve_cod_bloco(FILE *ficheiro, Casas_de_Simbolos topo, int tam_bloco);

/**
\brief Função auxiliar de escrita, esta escreve a codificação no seu lugar correspondente, quando existe.
@param ficheiro - Apontador para o ficheiro onde será escrito.
@param topo - Apontador para o inicio da codificação do símbolo.
 */
void escreve_codificacao(FILE *ficheiro,Casas_de_Simbolos codificacao);

#endif //SHAFT_TRATAMENTO_FILE_COD_H
