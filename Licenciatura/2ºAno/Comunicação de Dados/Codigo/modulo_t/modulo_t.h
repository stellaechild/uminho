#ifndef MODULOT
#define MODULOT

#include "../comum.h"

/**
@file modulo_t.h
Este módulo contêm a função principal para a realização da fase da criação da tabela a ser usada.
Também contêm funções de interface.
 Data de Criação: 27 de novembro.
 Autores: Vicente Moreira.
*/

/**
\brief Função principal do módulo que abre os ficheiros responáveis, verifica erros, inicializa e executa o loop de
"leitura, organização, preparação, escrita e limpeza" para cada bloco do ficheiro.
@param nome - String com o nome do ficheiro original.
@retuns Valor de erro.
 */
int modulo_t(char *nome);

/**
\brief Função de interface. Escreve uma mensagem de sucesso do módulo, recebendo as várias informações necessárias.
@param num_blocos - Número de blocos processados.
@param *tamanho_blocos - Pointer para o array que guarda o tamanho de cada bloco.
@param tempo_exec - Valor em milisegundos do tempo de execução do módulo.
@param *nome_cod - String com o nome do ficheiro criado.
 */
void mensagem_final_sucesso_t(int num_blocos, int *tamanho_blocos, double tempo_exec, char *nome_cod);

/**
\brief Função de interface. Escreve a mensagem de erro usando a variavel global "errno"
 */
void mensagem_erro_modulo_t();

#endif