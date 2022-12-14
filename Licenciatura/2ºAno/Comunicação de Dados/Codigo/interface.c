#include "interface.h"
#include "comum.h"
#include <string.h>
#include <stdio.h>

int le_argumentos(int argc,char **argv,int *pos_nome, int *modulo,int *forcar_rle,char *tamanho_rle,int *opcao_descodificacao){
    char leitor;
    int parametro = 0; // 0-Nenhum /1-Módulo /2-Forçar rle /3-Tamanho rle /4- Opção para descodificação
    int erro =0;
    *opcao_descodificacao = 0;  // 0-Tudo /1-Apenas shaf /2-Apenas rle

    for (int aux = 1;aux < argc && erro == 0;aux++){   //Loop para ler todos os argumentos
        leitor = argv[aux][0];
        if (parametro == 0) {                          //Se não esta a procura de uma opção de parâmetro, procura algum
            if (leitor != '-') {                       //dos parâmetros possíveis a ser chamado ou regista o nome do ficheiro.
                if (*pos_nome == -1) *pos_nome = aux;  //Regista a posição do nome, se já existia reporta erro.
                else erro = 3;                          //ERRO 3 - Dois nomes dados
            }
            else {
                leitor = argv[aux][1];
                switch (leitor) {
                    case 'm':parametro=1;
                        break;
                    case 'c':parametro=2;
                        break;
                    case 'b':parametro=3;
                        break;
                    case 'd':parametro=4;
                        break;
                    case 'h':erro=-1;                    //ERRO -1 = Comando Help detetado
                        break;
                    default:erro =1;                     //ERRO 1 = Parâmetro Inválido
                        break;
                }
            }
        }
        else{
            if (strlen(argv[aux]) == 1) {                 // Parâmetro 1 - Módulo
                if (parametro == 1) {
                        switch (leitor) {
                            case 'f':
                                *modulo = 1;
                                break;
                            case 't':
                                *modulo = 2;
                                break;
                            case 'c':
                                *modulo = 3;
                                break;
                            case 'd':
                                *modulo = 4;
                                break;
                            default:
                                erro = 2;                    //ERRO 2 = Opção de parâmetro errado
                                break;
                        }
                }
                else if (parametro == 2){                    // Parâmetro 2 - Forçar Rle
                    if (leitor != 'r') erro = 2;
                    else *forcar_rle = 1;
                }
                else if (parametro == 3){                     // Parâmetro 3 - Tamanho do bloco
                    if (leitor == 'K' || leitor == 'm' || leitor == 'M') *tamanho_rle = leitor;
                    else erro = 2;
                }
                else {                                        // Parâmetro 4 - Opção de descompressão
                    if (leitor == 's') *opcao_descodificacao = 1;
                    else if (leitor == 'r') *opcao_descodificacao = 2;
                    else erro = 2;
                }
            }
            else erro = 2;
            parametro = 0;
        }
    }
    if (erro == 0) {  //Verifica se não ficou a meio da procura de alguma opção para um parâmetro ou se não encontrou um nome.
        if (parametro != 0) erro = 2;
        if (*pos_nome == -1) erro = 4;                     //ERRO 4 - Nome Inexistente
    }
    return erro;
}

void mensagem_erro(int erro){ //Lista de erros

    fprintf(stdout,"Erro nos argumentos!!!\nErro numero: %d\n",erro);

    switch (erro) {
        case 1: fprintf(stdout,"%cUtilizacao de Parametros invalidos%c\n",'"','"');
            break;
        case 2:fprintf(stdout,"%cOpcao de Parametro invalida%c\n",'"','"');
            break;
        case 3:fprintf(stdout,"%cDois nomes de ficheiro detetados%c\n",'"','"');
            break;
        case 4:fprintf(stdout,"%cNome do ficheiro inexistente%c\n",'"','"');
            break;
        default:fprintf(stdout,"%cTo be honest, i don't know...%c\n",'"','"');
            break;
    }
    fprintf(stdout,"\nPara mais ajuda na utilizacao do programa escreva -h\n");
}

void mensagem_ajuda(){

    fprintf(stdout,"Bem-vindo ao programa Shafa! O compressor e descompressor de ficheiros desenvolvido no\n"
                   "ambito da cadeira de Comunicacao de Dados 20/21 da Universidade do Minho.\n\n");
    fprintf(stdout,"Utilizacao basica para comprimir: ./shafa <filename>\n"
                   "Utilizacao basica para descomprimir: ./shafa <filename> -m d\n"
                   "Tambem podem ser inseridos varios argumentos como:\n\n"
                   "-m (f|t|c|d) Modulo a ser usado (Frequencias|Tabela|Codificacao|Descodificacao)\n"
                   "-c r         Forçar compressao rle\n"
                   "-b (k|K|m|M) Tamanho dos Blocos (64Kbytes|640Kbytes|8Mbytes|64Mbytes)\n"
                   "-d (s|r)     Tipo de descompressao (Apenas shaf|Apenas Rle)\n");

}


void mensagem_execucao_modulo_individual(char *nome,int modulo,int forcar_rle,char tamanho_rle,int opcao_desc){
    fprintf(stdout,"A executar o modulo ");
    switch (modulo) {
        case 1: fprintf(stdout,"F (Frequencias) ");
            break;
        case 2: fprintf(stdout,"T (Tabela) ");
            break;
        case 3: fprintf(stdout,"C (Codificacao) ");
            break;
        case 4: fprintf(stdout,"D (Descodificacao) ");
            break;
        default: fprintf(stdout, "%d, wtf?",modulo);
            break;
    }
    fprintf(stdout,"para o ficheiro %c%s%c\n",'"',nome,'"');
    if (forcar_rle == 1) fprintf(stdout,"forcando compressao rle ");
    else fprintf(stdout,"sem forcar compressao rle ");
    if (modulo == 1) {
        fprintf(stdout, "e blocos de tamanho ");
        switch (tamanho_rle) {
            case 'k':
                fprintf(stdout, "64 Kbytes (default)");
                break;
            case 'K':
                fprintf(stdout, "640 Kbytes");
                break;
            case 'm':
                fprintf(stdout, "8 Mbytes");
                break;
            case 'M':
                fprintf(stdout, "64 Mbytes");
                break;
            default:
                fprintf(stdout, "%c, outro erro", tamanho_rle);
                break;
        }
    }
    if (opcao_desc != 0) {
        fprintf(stdout, "\nDescodificando apenas a parte");
        if (opcao_desc == 1) fprintf(stdout," shaf do ficheiro");
        else fprintf(stdout," rle do ficheiro");
    }
    fprintf(stdout,"\n\n");
    if ((modulo != 1 && (forcar_rle != 0 || tamanho_rle != 'k')) || (modulo != 4 && opcao_desc != 0))
        fprintf(stdout,"Parametros adicionais foram recebidos, mas o modulo chamado\n"
                       " nao utiliza parte deles! (Parametros serao ignorados)\n");
    fprintf(stdout,"\n");
}


void apaga_ficheiros_intermedios (char *nome){
    size_t tamanho_filename = strlen(nome);
    char nome_apagar[tamanho_filename+10];
    trata_nome_ficheiro(nome,nome_apagar,0,0);
    remove(nome_apagar);
    trata_nome_ficheiro(nome,nome_apagar,1,0);
    remove(nome_apagar);
    trata_nome_ficheiro(nome,nome_apagar,1,1);
    remove(nome_apagar);
}