/**
 * Data de criação: 15/04/2021
 * Maria Cunha - A93264
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão:20210505
 * 
 * API do interpretador.
 * Esta é responsável pelo fluxo do programa.
 */

#ifndef CONTROLADOR_H
#define CONTROLADOR_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <glib.h>
#include "sgr.h"
#include "FuncAux.h"
#include "table.h"
#include "variaveis.h"
#include "visualizador.h"

typedef struct command* Command;

typedef enum OPERATOR{LT, EQ, GT}OPERATOR;

/**
 * Função que escreve uma dada variável num ficheiro.
 */
void toCSV(char* x,char* delim,char* filepath);

/**
 * Função de get de um elemento de table.
 */
TABLE get_element(char* var,int x,int y);

/**
 * Função que lê uma dada variável para memória.
 */
TABLE fromCSV(char* filepath,char delim);

/**
 * Função de filtro.
 */
TABLE filter(char* var,char* colName,char* value,OPERATOR op);

/**
 * Função de projeção. Apresenta uma coluna de uma TABLE.
 */
TABLE proj(char* var,int col);

/**
 * Função checkSGR. Retorna o estado de cada listagem.
 */
void checkSGR ();

/**
 * Função checkStats. Retorna o número de users,businesses e reviews carregados.
 */
void checkStats();

/**
 * Função checkVars. Retorna as variáveis em uso.
 */
void checkVars();

/**
 * Se existir, remove a variável 'var' pedida.
 */
void removeVar(char* var);

/**
 * Remove todas as variáveis.
 */
void removeVarsAll();

/**
 * Função de Show.
 * Mostra a TABLE, se esta tiver "info". Apenas mostra esta e pergunta
 * ao utilizador se pretende ver a TABLE completa.
 */
void show(char* var);

/**
 * Função auxiliar. Apaga espaços vazios à volta da String
 * Ex: "  Isto é um exemplo   " -> "Isto é um exemplo"
 */
char* novodeblank(char* input);

/**
 * Função auxiliar. Troca os '"' numa String por espaços vazios
 */
char* debrack(char* input);

/**
 * Remove todos os espaços numa String.
 * Ex: "  Isto é um exemplo   " -> "Istoéumexemplo"
 */
char* deblank(char* input);

/**
 * Função que interpreta um comando.
 * Esta devolve um Command, contendo o comando lido, a sua validade,
 * argumentos e variáveis(caso aplicável).
 */
Command interpreta_linha(char* linha);

/**
 * Função que traduz o comando lido em String para o seu código
 * String -> int
 */ 
int interpreta_funcao (char* com);

/**
 * Função que indica o número de argumentos que o comando lido precisará
 */ 
int quantos_args (int num);

/**
 * Função que verifica os args carregados para as queries
 */ 
int verifica_args(int comando,StringL args);

/**
 * Função que, depois de o comando ser identificado como "query", executa esta depois de preparar os argumentos
 */ 
TABLE executa_query (Command com);

/**
 * Função que executa o comando, executa comandos diversos ou chama o "executa_query".
 */
void executa_funcao (Command com);

/**
 * Função principal do fluxo do programa.
 */
int run();

#endif
