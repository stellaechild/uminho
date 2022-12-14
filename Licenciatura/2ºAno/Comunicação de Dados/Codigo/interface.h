#ifndef SHAFT_INTERFACE_H
#define SHAFT_INTERFACE_H

/**
@file interface.h
Este ficheiro contêm as funções utilizadas para escrever no terminal mensagens de erro e controle do programa.
 Data de Criação: 13 de Dezembro.
 Autores: Vicente Moreira.
*/

/**
\brief Função de leitura de argumentos. Esta usa várias flags como "parâmetro" para indicar que os próximo argumento a ser lido
será interpretado como uma das opções específicas, procura também o nome do ficheiro a ser comprimido e reporta qualquer
erro de input do utilizador.
@param argc - Número de argumentos usados na chamada do programa
@param argv - Apontador para o array das strings onde os argumentos residem.
@param pos_nome - Apontador para o inteiro que indica em q argumento o nome do ficheiro se encontra. Este é 
                  definido como -1 no início e altera comforme a leitura, mais de um nome fornecido é inválido
@param modulo - Apontador para o inteiro utilizado para sinalizar o módulo a ser usado.
@param forcar_rle - Apontador para a flag que se rle será forçado ou não
@param tamanho_rle - Apontador para o indicador to tamanho do bloco.
@return Número de Erro. 0-Sucesso / 1-Parâmetro inválido /2-Opção de parâmetro inválida /3-Dois nomes fornecidos /4-Nome inexistente
 */
int le_argumentos(int argc,char **argv,int *pos_nome, int *modulo,int *forcar_rle,char *tamanho_rle,int *opcao_descodificacao);

/**
\brief Função que reporta o erro.
@param erro - Erro encontrado.
 */
void mensagem_erro(int erro);

/**
\brief Mensagem de introdução e ajuda na utilização do programa Shaf.
 */
void mensagem_ajuda();
 
/**
\brief Função de interface. Quando o programa executa com sucesso, sumariza a utilização que este irá fazer.
@param nome - Nome do ficheiro chamado.
@param modulo - Número do módulo utilizado.
@param forcar_rle - Flag de utilização forçada de rle.
@param tamanho_rle - Flag para o tamanho dos blocos.
@param opcao_desc - Opção de descompressão. (Tudo, Só shaf, Só rle)
 */
void mensagem_execucao_modulo_individual(char *nome,int modulo,int forcar_rle,char tamanho_rle,int opcao_desc);

/**
\brief Função de auxílio, apaga ficheiros intremédios ao fazer a compressão global.
@param nome - Nome do ficheiro original.
 */
void apaga_ficheiros_intermedios (char *nome);

#endif //SHAFT_INTERFACE_H
