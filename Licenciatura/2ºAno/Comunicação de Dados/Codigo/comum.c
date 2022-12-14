#include "comum.h"
#include <stdlib.h>
#include <string.h>
#include <errno.h>

Digitos_Binarios acrescenta_digito (short int digito, Digitos_Binarios inicio) {
    Digitos_Binarios novo = (Digitos_Binarios) malloc(sizeof(Digitos_Binarios));
    novo->digito = digito;
    novo->prox = NULL;

    if (inicio == NULL) inicio = novo;  //Caso não codificação exista, cria um novo digito
    else {                              //Caso exista um codificação, percorre-a até ao fim
        Digitos_Binarios tmp = inicio;
        while (tmp->prox != NULL) tmp = proximo_digitos (tmp);
        tmp->prox = novo;
    }
    return inicio;
}

Digitos_Binarios proximo_digitos(Digitos_Binarios target){
    Digitos_Binarios prox;
    prox= target->prox;
    return prox;
}

short int le_digito(Digitos_Binarios target){
    short int r;
    r=target->digito;
    return r;
}

void liberta_digitos(Digitos_Binarios target){
    if (target->prox !=NULL) liberta_digitos(target->prox);
    free (target);
}





FILE *abre_ficheiro_correto(char *nome_original,int modo){ // 0-.rle   1-.freq    2-.cod    3-.shaf  4-(original)
    size_t tamanho_filename;
    FILE *ficheiro_aberto;

    //Calcula o tamanho do nome ficheiro para criar uma target string
    tamanho_filename = strlen(nome_original);         // Tamanho do nome do ficheiro
    char nome_a_abrir [tamanho_filename + 10];        // ficheiro + ".rle.shaf"/".rle.freq" (possivelmente)

    trata_nome_ficheiro(nome_original, nome_a_abrir, modo, 0);

    //Abre o ficheiro básico 
    ficheiro_aberto = fopen(nome_a_abrir, "rb");
    //Caso de erro, abre o ficheiro .rle
    if (errno != 0){
        fprintf(stdout, "Procura pelo ficheiro %c%s%c falhou...\n", '"', nome_a_abrir, '"');
        errno = 0;

        trata_nome_ficheiro(nome_original, nome_a_abrir, modo, 1);

        fprintf(stdout, "A procura do ficheiro %c%s%c\n\n", '"', nome_a_abrir, '"');
        ficheiro_aberto = fopen(nome_a_abrir, "rb");
    }

    return ficheiro_aberto;
}


void trata_nome_ficheiro(char *nome_original, char *nome_alvo, int modo,int rle){
    size_t contador;
    // Strings com cada extensão para fazer string copys...
    char s_rle [5] = ".rle";
    char freq [6] = ".freq";
    char cod [5] = ".cod";
    char shaf [6] = ".shaf";

    //Cópia o nome original na target string
    strcpy(nome_alvo,nome_original);
    contador = strlen(nome_alvo);
    
    // Decide se acrescenta .rle
    if (rle == 1){
        strcpy(nome_alvo+contador,s_rle);
        contador = strlen(nome_alvo);
    }
    
    // Acrescenta a extensão desejada
    switch (modo) {
        case 0:strcpy(nome_alvo+contador,s_rle);
            break;
        case 1:strcpy(nome_alvo+contador,freq);
            break;
        case 2:strcpy(nome_alvo+contador,cod);
            break;
        case 3:strcpy(nome_alvo+contador,shaf);
            break;
        default:
            break;
    }

}

void progresso (int bloco_atual,int blocos_totais){
    float percentagem_completo = ((float) bloco_atual/(float) blocos_totais) * 100; //Cálculo da percentagem
    int percentagem_arredondada = (int) percentagem_completo;//Arredonda
    printf("\r");
    fputc('[',stdout);                                                              //Desenho da barra de progresso
    for (int aux = 0; aux < percentagem_arredondada; aux += 2){
        fputc('#',stdout);
    }
    for (int aux = 0; aux < 99-percentagem_arredondada; aux += 2){
        fputc('.',stdout);
    }
    fputc(']',stdout);
    fprintf(stdout," %.1f%c",percentagem_completo,'%');
    fflush(stdout);
}


void imprime_tempo_exec(double tempo_exec){
    int modo_tempo=0;

    if (tempo_exec > 1000) {
        tempo_exec = tempo_exec / 1000;
        modo_tempo++;
        if (tempo_exec > 300) {
            tempo_exec = tempo_exec/60;
            modo_tempo++;
        }
    }

    if (modo_tempo == 0) fprintf(stdout,"Tempo de execucao: %.3f(ms)\n",tempo_exec);
    else if (modo_tempo == 1) fprintf(stdout,"Tempo de execucao: %.1f(s)\n",tempo_exec);
    else fprintf(stdout,"Tempo de execucao: %.2f(minutos)\n",tempo_exec);

}
