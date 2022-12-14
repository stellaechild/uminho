/**
 * Data de criação: 15/04/2021
 * Vicente Moreira - A93296
 * Versão: 20210417
 * 
 * Esta API é responsável por "montar" e preparar o tipo de dados "TABLE"
 */

#ifndef TABLEDEF
#define TABLEDEF

typedef struct table *TABLE;

/**
 * Aloca espaço para uma table de dimensão xsize.
 * Parametro topline indica se a table contêm a linha visual.
 */
TABLE new_empty_sizedTable(int linhaSize,int topline);

/**
 * Aloca espaço para uma table.
 * Parametro topline indica se a table contêm a linha visual.
 */
TABLE new_empty_table(int topline);

/**
 * Função de adição dinâmica do TABLE. A linha fornecida é separada
 * entre as várias colunas usando o delimitador ';', no fim esta é destruida.
 */
void table_append_line(TABLE target,char *str);

/**
 * Função que define a posição do "infoline"
 * Isto indica para o "print_table_info" onde começar a ler a TABLE.
 * Útil para imprimir frases soltas ou valores.
 */
void table_setInfoLine_now(TABLE target);

/**
 * Define infoline numa posição
 */
void table_setInfoLine_pos(TABLE target,int pos);

/**
 * Função que devolve a posição do "infoline"
 */
int table_getInfoLine(TABLE target);

/**
 * Função que devolve o array com o tamanho dos caracteres de cada coluna
 */
int* table_getsizeCol(TABLE target);

/**
 * Função de leitura. Devolve o elemento da Table nas coordenadas.
 */
char* table_get_element(TABLE target,int x,int y);

/**
 * Função que devolve o tamanho do elemento a ser lido
 */
int table_get_element_size(TABLE target,int x,int y);

/**
 * Função que devolve uma linha inteira de table, usando um delimitador.
 * Útil para a escrita no CSV
 */
char* table_get_line_mounted(TABLE target,char* delim,int pos);

/**
 * Devolve o número de linhas da Table
 */
int table_getNumLinhas(TABLE target);

/**
 * Devolve o tamanho maximo de caracteres de uma dada coluna
 */
int table_get_colSizePos(TABLE target,int pos);

/**
 * Devolve o número de colunas da linha de topo.
 */
int table_getNumColunasTOP(TABLE target);

/**
 * Devolve o número de colunas de uma dada linha.
 */
int table_getNumColuna(TABLE target,int linha);

/**
 * Devolve o controlo do topline
 */
int table_topline(TABLE target);

/**
 * Liberta a Table
 */
void table_free(TABLE target);

/**
 * Clona a Table
 */
TABLE table_clone(TABLE target);

/**
 * Calcula os tamanhos maximos (a serem apresentados) "permitidos" por cada coluna.
 */
void table_calculate_colsize(TABLE target);

/**
 * Função auxiliar usada quando o sgr é inválido
 */
TABLE get_table_invalidSgr(TABLE res);

#endif
