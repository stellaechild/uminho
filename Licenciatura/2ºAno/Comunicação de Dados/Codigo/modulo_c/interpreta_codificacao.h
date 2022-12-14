#ifndef SHAFT_INTERPRETA_CODIFICACAO_H
#define SHAFT_INTERPRETA_CODIFICACAO_H
#include "../comum.h"
#include "estruturas.h"

/**
@file interpreta_codificacao.h
Este módulo contém as funções principais para a execução do módulo_c.
 Data de Criação: 7 de dezembro.
 Autores: Joana Alves e Maria Cunha.
*/

/**
\brief Função que lê o caracter do ficheiro original e procura a codificação correspondente no array, armazenando-a de seguida
 numa lista ligada de tipo Comprimidos. Guarda também o tamanho do bloco depois de comprimido numa variável.
@param original Ficheiro original.
@param array_codificado Array com codificação armazenada.
@param max Tamanho máximo de codificação possível.
@param tam_bloco Tamanho do bloco a ser codificado.
@param tam_bloco_comprimido Variável a ser armazenada o tamanho do bloco comprimido.
@return Lista ligada do tipo Comprimidos contendo toda os caracteres comprimidos do bloco, prontos a serem escritos no ficheiro.
 */
Comprimidos le_caracter (FILE *original, Digitos_Binarios *array_codificado, int max, int tam_bloco, int *tam_bloco_comprimido);


/**
\brief Função que recebe um array de bits e calcula o caracter (byte) resultante (a partir dos 8 primeiros bits).
@param b Array de bits para codificação.
@return Caracter codificado.
 */
unsigned char codifica_bits (int b[]);


/**
\brief Função que copia a codificação do caracter armazenada na lista ligada para o array. A codificação é inserido no array b
 a partir do indice cont.
@param indice Lista ligada contendo a codificação.
@param b Array alvo.
@param cont Inteiro que corresponde ao indice da primeira posição vazia do array b.
@return Parâmetro cont atualizado.
 */
int copia_digitos (Digitos_Binarios indice, int *b, int cont);


/**
\brief Função que faz "reset" ao buffer contendo as codificações. As posições a partir do indice 7 passam a estar na posição (indice-7).
@param c Array alvo.
@param cont Indice da primeira posição vazia do array c.
@return Parâmetro cont = índice da primeira posição vazia do array c (atualizada).
 */
int reset_buffer (int *c, int cont);

#endif