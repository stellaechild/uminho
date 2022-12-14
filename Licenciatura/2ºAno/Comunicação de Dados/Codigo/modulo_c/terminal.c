#include <stdio.h>
#include <errno.h>
#include <string.h>
#include "terminal.h"
#include "../comum.h"


void mensagem_erro_modulo_c () {
    fprintf(stdout,"Falha na execucao do modulo C :C\n");
    fprintf(stdout,"Erro %d\n",errno);
    fprintf(stdout,"%c%s%c\n",'"',strerror(errno),'"');
}

void mensagem_sucesso (int num_blocos, int *tamanho_blocos_antes, int *tamanho_blocos_depois, double tempo_exec, char *nome_shaf) {
    float taxa_global = taxa_compressao_global (tamanho_blocos_antes, tamanho_blocos_depois, num_blocos);

    fprintf(stdout,"\n\n---------------Projeto Shafa MIEI/CD 20/21 22-Dezembro-2020---------------\n");
    fprintf(stdout,"\nJoana Alves - A93290; Maria Cunha - A93264\n");
    fprintf(stdout,"Modulo C (codificacao de um ficheiro de simbolos)\n");
    fprintf(stdout,"Numero de blocos tratados: %d\n",num_blocos);

    for (int i=0; i<num_blocos; i++) {
        float aux = (float)tamanho_blocos_antes[i] - (float)tamanho_blocos_depois[i];
        float taxa_compressao = (aux  / (float)tamanho_blocos_antes[i]) * 100;
        fprintf (stdout, "Tamanho antes/depois & taxa de compressao (bloco %d): %d/%d & %.2f%c\n",
                 (i+1), tamanho_blocos_antes[i], tamanho_blocos_depois[i], taxa_compressao,'%');
    }

    fprintf (stdout, "Taxa de compressao global: %.2f%c\n", taxa_global,'%');
    imprime_tempo_exec(tempo_exec);
    fprintf(stdout,"Ficheiro gerado: %s\n", nome_shaf);
    fprintf(stdout,"\n-------------Execucao do modulo terminada com sucesso!------------------\n");
}

float taxa_compressao_global (int *tam_bloco_antes, int *tam_bloco_depois, int num_blocos) {
    int antes=0, depois=0;
    float r;
    for (int i=0; i<num_blocos; i++) {
        antes += tam_bloco_antes[i];
        depois += tam_bloco_depois[i];
    }
    float aux = antes - depois;
    r = (aux / (float)antes) * 100;
    return r;
}
