#ifndef COMUM
#define COMUM

#include <stdlib.h>
#include <stdio.h>

/**
@file comum.h
Este ficheiro contêm as estruturas e funções utilizadas por vários módulos.
 Data de Criação: 27 de novembro.
 Autores: Vicente Moreira.
*/

/**
\brief Estrutura para armazenar a codificação de símbolos.
            Exemplo: codigo 01011 ficaria |-----|      |-----|      |-----|      |-----|      |-----|
                                          |  0  |      |  1  |      |  0  |      |  1  |      |  1  |
                                          |-----| ---> |-----| ---> |-----| ---> |-----| ---> |-----| NULL
            Sendo cada caixa uma destas "structs" com o seu "digito" e o pointer para a proxima struct
 */
typedef struct dig_binario{
    short int digito;          // Só contêm o valor 0 ou 1;
    struct dig_binario *prox;  //Próximo dígito (lido esquerda para direita, NULL se acabou)
}*Digitos_Binarios;


/**
\brief Função que adiciona um novo nodo (contendo o digito) à lista ligada inicio de tipo Digitos_Binarios.
@param digito Digito a ser adicionado.
@param inicio Lista ligada alvo.
@return Apontador para a estrutura (atualizada).
 */
Digitos_Binarios acrescenta_digito (short int digito, Digitos_Binarios inicio);

/**
\brief Função que avança para o próximo nodo.
@param target - Apontador para a estrutura alvo.
@return Apontador para a próxima estrutura.
 */
Digitos_Binarios proximo_digitos(Digitos_Binarios target);

/**
\brief Função de leitura, lê o digito na struct.
@param target - Apontador para a estrutura alvo.
@return Devolve o digito lido.
 */
short int le_digito(Digitos_Binarios target);

/**
\brief Função de libertação de memória.
@param target - Apontador para a estrutura alvo.
 */
void liberta_digitos(Digitos_Binarios target);



/**
\brief Função para abertura de ficheiros, dado um nome e a extensão pretendida a abrir, tenta abrir o ficheiro, caso
 falhe, abre a versão alternativa .rle do mesmo ficheiro (Exemplo: tenta aaa.txt.cod, se falhar, aaa.txt.rle.cod)
@param nome_original - String contendo o nome original do ficheiro
@param modo - Que extensão tenta abrir. 0-.rle   1-.freq    2-.cod    3-.shaf   4-(original)
@return Pointer para o ficheiro aberto.
 */
FILE *abre_ficheiro_correto(char *nome_original,int modo);

/**
\brief Função auxiliar para abertura de ficheiros, trabalha diretamente coms as strings do nome e o nome_alvo para
 colocar os nomes corretos a abrir. Recebe a extensão pretendida e se contêm .rle
@param nome_original - String contendo o nome original do ficheiro.
@param nome_original - String alvo que irá abrir o ficheiro.
@param modo - Que extensão adiciona ao nome. 0-.rle   1-.freq    2-.cod    3-.shaf   4-(original)
@param rle - Se acrescenta a extensão .rle
 */
void trata_nome_ficheiro(char *nome_original, char *nome_alvo, int modo,int rle);

/**
\brief Função e interface, usada para medir o progresso dos módulos, pode ser usada em qualquer processo que
 tenha um counter e um alvo final. Escreve no terminal a percentagem completa desses valores
@param bloco_atual - Bloco atual a ser trabalhado.
@param blocos_totais - Blocos totais.
 */
void progresso (int bloco_atual,int blocos_totais);

/**
\brief Função de interface. Imprime o tempo de execução adaptando a um formato adequado.
@param tempo_exec - Tempo de execução em milisegundos.
 */
void imprime_tempo_exec(double tempo_exec);
#endif