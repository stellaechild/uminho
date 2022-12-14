#include <string.h>
#include <time.h>
#include <errno.h>

#include "modulo_t.h"
#include "t_estruturas.h"
#include "algoritmo_shanon_fanon.h"
#include "tratamento_file_cod.h"
#include "tratamento_file_freq.h"

// ---------------------------------------FUNÇÃO PRINCIPAL-------------------------------------
int modulo_t(char *nome){
    clock_t tempo_inicial = clock();

    fprintf(stdout,"A iniciar a execucao do modulo T...\n\n");

    int rle,num_blocos,tam_bloco;            //------------------
    int frequencias[256];
    char chr_rle;                            //  Declaração de
    Casas_de_Simbolos topo_struct = NULL;    //    variáveis
    FILE *ficheiro_freq;
    size_t tamanho_filename = strlen(nome);  //-------------------

    ficheiro_freq = abre_ficheiro_correto(nome,1);

    if (errno == 0) {  //Se o ficheiro .freq for aberto com sucesso, executa.

        //Faz a leitura inicial dos parâmetros (rle e número de blocos)
        if (fscanf(ficheiro_freq, "@%c@%d@", &chr_rle, &num_blocos)){};
        if (chr_rle == 'R') rle = 1;
        else rle = 0;

        int tamanho_dos_blocos[num_blocos]; // Usado para guardar o tamanho dos blocos individuais.
        char nome_cod[tamanho_filename+9];  // ".rle.cod" (nome do ficheiro output, possivelmente)

        trata_nome_ficheiro(nome,nome_cod,2,rle);

        //Abre o ficheiro .COD
        FILE *ficheiro_cod = fopen(nome_cod, "wb");

        //Escreve a formatação inicial
        fprintf(ficheiro_cod, "@");
        if (rle == 1) fprintf(ficheiro_cod, "R");
        else fprintf(ficheiro_cod, "N");
        fprintf(ficheiro_cod, "@%d@", num_blocos);


        //Entrada no loop de "Leitura, Organização, Preparação, Escrita e Limpeza -----------------------
        //(Repete para o número de blocos lido)
        for (int aux = 1; aux <= num_blocos; aux++) {

            tam_bloco = ler_freq(ficheiro_freq, frequencias); //           ---Leitura
            tamanho_dos_blocos[aux-1] = tam_bloco;

            topo_struct = organiza_array_na_struct(frequencias); //        ---Organização

            shanon_fanon(topo_struct); //                                  ---Preparação

            escreve_cod_bloco(ficheiro_cod, topo_struct, tam_bloco); //    ---Escrita

            liberta_casa(topo_struct); //                                  ---Limpeza

            progresso (aux,num_blocos); //                                 ---(Progresso no terminal)
        }
        //Fim do loop --------------------------------------------------------------------------------

        //Finalização
        fprintf(ficheiro_cod, "0");
        fclose(ficheiro_cod);
        fclose(ficheiro_freq);

        clock_t tempo_final = clock();
        double tempo_total = ((double) (tempo_final - tempo_inicial) / CLOCKS_PER_SEC) * 1000;

        //Mensagem final
        mensagem_final_sucesso_t(num_blocos, tamanho_dos_blocos, tempo_total, nome_cod);
    }
    else mensagem_erro_modulo_t(); //Erro na abertura do ficheiro .freq
    fputc('\n',stdout);
    return errno;
}



void mensagem_final_sucesso_t(int num_blocos, int *tamanho_blocos, double tempo_exec, char *nome_cod){

    fprintf(stdout,"\n\n----------Projeto Shafa MIEI/CD 20/21 9-Dezembro-2020----------\n");
    fprintf(stdout,"\nDavid Alexandre Ferreira Duarte - A93253\nVicente Gonçalves Moreira       - A93296\n");
    fprintf(stdout,"Modulo T (Calculo dos codigos dos simbolos)\n");
    fprintf(stdout,"Numero de blocos tratados: %d\n",num_blocos);
    fprintf(stdout,"Tamanho dos blocos: \n");
    for(int aux = 0; aux <= num_blocos/10;aux++){
        for (int aux2 = 0;aux2 < num_blocos-aux*10 && aux2 <10;aux2++){
            fprintf(stdout,"%d",tamanho_blocos[aux*10+aux2]);
            if (aux2 != 9 && aux2+1 < num_blocos-aux*10)  fputc('/',stdout);
        }
        fputc('\n',stdout);
    }
    imprime_tempo_exec(tempo_exec);
    fprintf(stdout,"Ficheiro gerado: %s\n",nome_cod);

    fprintf(stdout,"\n----------Execucao do modulo terminada com sucesso!------------\n");
}

void mensagem_erro_modulo_t(){
    fprintf(stdout,"Falha na execucao do modulo T :C\n");
    fprintf(stdout,"Erro %d\n",errno);
    fprintf(stdout,"%c%s%c\n",'"',strerror(errno),'"');
}