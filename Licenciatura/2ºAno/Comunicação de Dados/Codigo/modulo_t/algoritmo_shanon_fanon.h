#ifndef SHAFT_ALGORITMO_SHANON_FANON_H
#define SHAFT_ALGORITMO_SHANON_FANON_H

#include "../comum.h"
#include "t_estruturas.h"

/**
@file algoritmo_shanon_fanon.h
Este ficheiro contêm as funções para a organização, preparação e codificação de cada símbolo lido.
 Data de Criação: 8 de Dezembro.
 Autores: Vicente Moreira.
*/

/**
\brief Função de inicialização para a codificação Shanon-Fanon de cada símbolo, esta irá por calcular o número
total de símbolos que lhe foram atribuidos e de seguida chama uma função recursiva que irá atribuir a codificação
correta a cada símbolo.
@param topo_de_lista - Apontador para o inicio da lista ligada de Casa de Símbolos.
 */
void shanon_fanon(Casas_de_Simbolos topo_de_lista);

/**
\brief Função recursiva de Shanon-Fanon. Dado uma lista ligada num certo ponto e um "target" de símbolos que lhe
correspõe, vai atribuindo 0's na codificação dos símbolos que percorre, questionando sempre se já chegou o mais perto
possível do meio da lista que lhe foi atribuido (total_de_simbolos / 2). Ao chegar à metade, atribui 1's nos simbolos
restantes e chama-se recursivamente, dando parâmetros novos e controlados.
@param topo_de_lista - Apontador para a lista ligada de Casa de Símbolos onde a função começa a trabalhar.
@param total_simbolos - Número de símbolos que é responsável por "mudar" e verificar. (Evita que escreva 1's em
                       símbolos fora do seu domain.)
 */
void encontra_meio(Casas_de_Simbolos inicio,int total_simbolos);

/**
\brief Função de organização. Esta é responsável por criar a lista ligada de Casa de Símbolos por ordem descrescente
da frequência dos símbolos, usando o array gerado na leitura do ficheiro .FREQ
@param array - Apontador para o array das frequências lidas.
@return Apontador para o ínicio da lista ligada que será gerado.
 */
Casas_de_Simbolos organiza_array_na_struct (int *array);

#endif //SHAFT_ALGORITMO_SHANON_FANON_H
