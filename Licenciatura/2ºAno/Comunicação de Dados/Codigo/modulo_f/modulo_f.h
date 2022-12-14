#ifndef MODULO_F
#define MODULO_F

#include "functions.h"
#include "functions_rle.h"
#include "functions_freq.h"

/**
@file modulo_f.h
 Este é módulo principal do programa da parte - A; nele contém as principais funções 
*/

/**
\brief Função que faz a leitura do ficheiro original, e escrita do ficheiro .freq
@param txt - Estrutura Blocks que contém os tamanhos dos blocos do ficheiro original
@param *name - Nome do arquivo original
 */
void do_txt(Blocks txt,char *name);

/**
\brief Função que faz a leitura do ficheiro original, escrita do ficheiro .rle. e .rle.freq
@param *rle - Estrutura RleBlock que irá ser guardado os tamanhos de cada bloco do rle
@param txt -  Estrutura Blocks que contém os tamanhos dos blocos do ficheiro original
@param *name - Nome do arquivo original
@returns Frequencia total do ficheiro .rle
 */
int do_rle(RleBlock *rle, Blocks txt,char *name);

/**
\brief Função principal do programa, que verifica erros de leitura, faz cálculo dos tamanhos dos blocos, 
verifica a compressao rle, distribui para funções do_rle ou do_txt para escrita de ficheiros, printa interface final. 
@param *nome - Nome do arquivo original
@param forcar_rle - Inteiro binario , para verificar se o usuario quer rle forçado ou não
@param tam_bloco - Char lido pelo usuario, para verificar qual tamanho do bloco que o usuário quer
@returns 0
 */
int modulo_f(char *nome,int forcar_rle,char tam_bloco);

#endif
