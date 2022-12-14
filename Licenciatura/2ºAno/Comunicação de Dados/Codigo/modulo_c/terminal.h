#ifndef SHAFT_TERMINAL_H
#define SHAFT_TERMINAL_H

/**
@file terminal.h
Este módulo contém as funções usadas para escrita na consola.
 Data de Criação: 7 de dezembro.
 Autores: Joana Alves e Maria Cunha.
*/

/**
\brief Função que, em caso de erro, identifica na consola o erro ocorrido.
 */
void mensagem_erro_modulo_c ();


/**
\brief Função que, em caso de sucesso, escreve na consola a mensagem requerida pelos docentes.
@param num_blocos Número de blocos do ficheiro.
@param tamanho_blocos_antes Array contendo o tamanho dos blocos pré-compressão.
@param tamanho_blocos_depois Array contendo o tamanho dos blocos pós-compressão.
@param tempo_exec Tempo de execução do módulo C.
@param nome_shaf Nome do ficheiro de saída contendo a extensão .shaf.
 */
void mensagem_sucesso (int num_blocos, int *tamanho_blocos_antes, int *tamanho_blocos_depois, double tempo_exec, char *nome_shaf);


/**
\brief Função que calcula a taxa de compressão global do ficheiro de saída .shaf.
@param tam_bloco_antes Tamanho do bloco antes da compressão.
@param tam_bloco_depois Tamanho do bloco comprimido.
@param num_blocos Número de blocos do ficheiro.
@return Taxa de compressão global.
 */
float taxa_compressao_global (int *tam_bloco_antes, int *tam_bloco_depois, int num_blocos);

#endif
