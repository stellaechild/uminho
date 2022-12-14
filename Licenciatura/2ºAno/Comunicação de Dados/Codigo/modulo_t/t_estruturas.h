#ifndef SHAFT_T_ESTRUTURAS_H
#define SHAFT_T_ESTRUTURAS_H

#include "../comum.h"

/**
@file t_estruturas.h
Este ficheiro contêm as funções usadas para criar, escrever e ler nas estruturas especificas a este módulo.
 Data de Criação: 16 de Dezembro.
 Autores: Vicente Moreira.
*/

/**
\brief Estrutura de lista ligada usada para armazenar os vários símbolos que aparecem no ficheiro e as suas
frequências, assim como a sua codificação final (começa vazia) depois de codificados.
 */
typedef struct nodo{
    unsigned char simbolo;     // Símbolo correspondente (0 a 255)
    int frequencia;            // Número de vezes encontrado no bloco
    Digitos_Binarios codigo;   // Sequência de codificação
    struct nodo *prox;         // Próximo símbolo na sequência
}*Casas_de_Simbolos;

/**
\brief Função de criação. Cria nova casa de Símbolos.
@return Apontador para a estrutura criada.
 */
Casas_de_Simbolos novo_simbolo();

/**
\brief Função de criação. Cria nova casa de Símbolos no indicador "próximo" de um dado Casa de Símbolos.
@param target - Apontador para a estrutura onde será criado o novo nodo.
 */
void adiciona_nova_casa(Casas_de_Simbolos target);


/**
\brief Função de escrita. Adiciona um novo símbolo à Casa de Símbolos.
@param simbolo - Símbolo a ser registado.
@param target - Apontador para a estrutura alvo.
 */
void escreve_casa_simbolo(unsigned char simbolo,Casas_de_Simbolos target);

/**
\brief Função de escrita. Adiciona uma frequência à Casa de Símbolos.
@param simbolo - Frequência a ser registado.
@param target - Apontador para a estrutura alvo.
 */
void escreve_casa_freq(int freq,Casas_de_Simbolos target);

/**
\brief Função de escrita. Adiciona um digito à codificação de uma Casa de Simbolos.
@param simbolo - Digito a ser adicionado.
@param target - Apontador para a estrutura alvo.
@returns Apontador para a estrutura atualizada.
 */
Casas_de_Simbolos acrescenta_digito_casa(short int digito, Casas_de_Simbolos casa);


/**
\brief Função de leitura. Lê o símbolo presente de uma Casa de Simbolos.
@param target - Apontador para a estrutura alvo.
@return Caracter lido.
 */
unsigned char le_casa_simbolo(Casas_de_Simbolos target);

/**
\brief Função de leitura. Lê a frequência presente de uma Casa de Simbolos.
@param target - Apontador para a estrutura alvo.
@return Frequência lida.
 */
int le_casa_frequencia(Casas_de_Simbolos target);

/**
\brief Função de leitura. Lê o início da codificação presente de uma Casa de Simbolos.
@param target - Apontador para a estrutura alvo.
@return Inicio da codificaão.
 */
Digitos_Binarios le_casa_codigo(Casas_de_Simbolos target);


/**
\brief Função de estrutura geral. Avança para a próxima Casa de Simbolos na lista.
@param target - Apontador para a estrutura alvo.
@return Próxima Casa de Símbolos.
 */
Casas_de_Simbolos proxima_casa(Casas_de_Simbolos target);

/**
\brief Função de estrutura geral. Liberta a lista ligada de Casa de Símbolos.
@param target - Apontador para a estrutura alvo.
 */
void liberta_casa(Casas_de_Simbolos target);

#endif //SHAFT_T_ESTRUTURAS_H
