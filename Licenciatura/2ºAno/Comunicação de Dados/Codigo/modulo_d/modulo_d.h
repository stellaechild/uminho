//rever estes nomes

#ifndef SHAFA_MODULO_D_H
#define SHAFA_MODULO_D_H

#include <stdio.h>


typedef struct node
{
    unsigned char caracter;//simbolo representado pela sequencia de bits
    struct node* left;//0
    struct node* right;//1
} Tree,*ArvPtr;
//
//funcoes
//

int modulo_d(char * ficheiro,int opcao);
void mensagem_de_sucesso_shaf(int num_blocos, int *blocos_antes, int *blocos_depois, double tempo_de_exec, char *nome_final);
void mensagem_de_sucesso_rle(int comprimidos, int descomprimidos,double tempo_de_exec, char *nome_final);
void mensagem_erro_modulo_d();
int ler_shaf(FILE* fp, FILE* fpo, ArvPtr arvCod,int tamCod,int tamDesc);

int converte_char_int(char c);
int verifica_rle(FILE *fp);
ArvPtr monta_arvore(FILE *fp);
int descodSF(FILE *fpSHAF,FILE *fpCOD);
int descompRLE(FILE* fp, FILE *fpo,int *comprimido_rle);
int conta_num_blocos(FILE *fp);
//char descodifica_bin_char(Tree* arv,int x);
//int escreve_fic_original(FILE* fp,char x);
ArvPtr cria_nodo();
void libertaArvore(ArvPtr arv);
#endif //SHAFA_MODULO_D_H
