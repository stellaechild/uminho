#include "modulo_d.h"
#include "../comum.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

int modulo_d(char * ficheiro,int opcao) {

    fprintf(stdout,"A iniciar a execucao do modulo D...\n\n");

    int rle = 1;
    size_t tamanho_filename = strlen(ficheiro);
    char nomesemshaf[tamanho_filename + 1];
    strcpy(nomesemshaf, ficheiro);
    if(opcao!=2)nomesemshaf[tamanho_filename - 5] = '\0';

    ArvPtr topo, percorre;
    FILE *fic_SHAF;
    FILE *fic_COD;
   if(opcao!=2) {
       fic_SHAF = fopen(ficheiro, "rb");
       fic_COD = abre_ficheiro_correto(nomesemshaf, 2);//nomesemshaf
   }
      if (errno == 0) {

if(opcao!=2) rle = verifica_rle(fic_COD);
          int tamComp, tamDesc, i;
          int num_blocos=0;
          if(opcao!=2)num_blocos = conta_num_blocos(fic_COD);
          int blocos_antes[num_blocos], blocos_depois[num_blocos];
          char nomeFinal[tamanho_filename + 1];
          //Descodificacao SF
          if (opcao != 2) {
              fprintf(stdout,"A iniciar a descompressao SHAF...\n");
              clock_t tempo_inicial_shaf = clock();
              if (fscanf(fic_SHAF, "@%d", &num_blocos)){};
              FILE *fic_DESC = fopen(nomesemshaf, "wb");

              for (i = 0; i < num_blocos; i++) {
                  if(fscanf(fic_COD, "%d@", &tamDesc)){};
                  blocos_depois[i] = tamDesc;
                  topo = monta_arvore(fic_COD);
                  tamComp = conta_num_blocos(fic_SHAF);//rever nome desta funcoo
                  blocos_antes[i] = tamComp;
                  ler_shaf(fic_SHAF, fic_DESC, topo, tamComp, tamDesc);
                  libertaArvore(topo);
                  progresso(i + 1, num_blocos);
              }
              fclose(fic_SHAF);
              fclose(fic_COD);
              fclose(fic_DESC);

              clock_t tempo_final_shaf = clock();
              double tempo_total_shaf = ((double) (tempo_final_shaf - tempo_inicial_shaf) / CLOCKS_PER_SEC) * 1000;

              mensagem_de_sucesso_shaf(num_blocos, blocos_antes, blocos_depois, tempo_total_shaf, nomesemshaf);
          }
          //Descompressao RLE
          if (rle == 1 && opcao != 1) {
              fprintf(stdout,"A iniciar a descompressao RLE...\n");
              clock_t tempo_inicial_rle = clock();
              int comprimido_rle = 0, descomprimido_rle = 0;
              //  char nomeFinal[tamanho_filename + 1];

              FILE *fic_DESCr;

              if (opcao == 0) {
                  fic_DESCr = fopen(nomesemshaf, "rb");
                  strcpy(nomeFinal, nomesemshaf);
                  nomeFinal[tamanho_filename - 9] = '\0';
              } else {
                  fic_DESCr = fopen(ficheiro, "rb");
                  strcpy(nomeFinal, ficheiro);
                  nomeFinal[tamanho_filename - 4] = '\0';
              }

              FILE *fic_FINAL = fopen(nomeFinal, "wb");

              descomprimido_rle = descompRLE(fic_DESCr, fic_FINAL,
                                             &comprimido_rle);
              fclose(fic_DESCr);
              fclose(fic_FINAL);
              if (opcao == 0)remove(nomesemshaf);//so se for opcao==0
              clock_t tempo_final_rle = clock();
              double tempo_total_rle = ((double) (tempo_final_rle - tempo_inicial_rle) / CLOCKS_PER_SEC) * 1000;

              mensagem_de_sucesso_rle(comprimido_rle, descomprimido_rle, tempo_total_rle, nomeFinal);
          }
      } else mensagem_erro_modulo_d();

    return errno;
}

void mensagem_de_sucesso_shaf(int num_blocos, int *blocos_antes, int *blocos_depois, double tempo_de_exec, char *nome_final)
{
    fprintf(stdout,"\n\n----------Projeto Shafa MIEI/CD 20/21 28-Dezembro-2020----------\n");
    fprintf(stdout,"\nSamuel de Almeida Simoes Lira - A94166\nVitor Lelis Noronha Leite - A90707\n");
    fprintf(stdout,"Modulo D - Parte de SHAF (Descodificacao de ficheiro Shaf)\n");
    fprintf(stdout,"Numero de blocos tratados: %d\n",num_blocos);
    for (int i=0; i<num_blocos; i++) {
        fprintf (stdout, "Tamanho antes/depois: %d/%d\n",blocos_antes[i], blocos_depois[i]);
    }
    imprime_tempo_exec(tempo_de_exec);
    fprintf(stdout,"Ficheiro gerado: %s\n",nome_final);

    fprintf(stdout,"\n---------------Execucao terminada com sucesso!-----------------\n");
}

void mensagem_de_sucesso_rle(int comprimido, int descomprimido,double tempo_de_exec, char *nome_final)
{
    fprintf(stdout,"\n\n----------Projeto Shafa MIEI/CD 20/21 28-Dezembro-2020----------\n");
    fprintf(stdout,"\nSamuel de Almeida Simoes Lira - A94166\nVitor Lelis Noronha Leite - A90707\n");
    fprintf(stdout,"Modulo D - Parte RLE (Descompressao de ficheiro RLE)\n");
    fprintf (stdout, "Tamanho antes/depois: %d/%d\n",comprimido, descomprimido);
    imprime_tempo_exec(tempo_de_exec);
    fprintf(stdout,"Ficheiro gerado: %s\n",nome_final);

    fprintf(stdout,"\n---------------Execucao terminada com sucesso!-----------------\n");
}

void mensagem_erro_modulo_d(){
    fprintf(stdout,"Falha na execucao do modulo D :C\n");
    fprintf(stdout,"Erro %d\n",errno);
    fprintf(stdout,"%c%s%c\n",'"',strerror(errno),'"');
}

//Funcao que liberta arvore
void libertaArvore(ArvPtr arv){
if(arv){
if(arv->left)libertaArvore(arv->left);
if(arv->right)libertaArvore(arv->right);
free(arv);
}
}

//funcao que verifica se tem compressao rle e faz os dois primeiros fscanf do ficheiro cod
int verifica_rle(FILE *fp){
    int resp=0;
    char temp;
    if (fscanf(fp,"%c", &temp)){};
    if (fscanf(fp,"%c", &temp)){};
    if(temp!='N') resp=1;
    //caso o ficheiro tenha compressao rle retorna 1
    return resp;
}
//Comentar esta funcao
void preparaBuffer(int valor, int * buffer){
    int i;
    int aux[8]={128,64,32,16,8,4,2,1};
    for(i=0;i<8;i++) buffer[i]=0;

    for(i=0;i<8;i++){
        if(valor/aux[i] == 1){
            buffer[i]=1;
            valor= valor%aux[i];
        }
        }
    }

//funcao que le ficheiro tipo shaf
 int ler_shaf(FILE* fp, FILE* fpo, ArvPtr arvCod,int tamCod,int tamDesc){
    //le ficheiro shaf e percorre arvore (*arcod) e escreve no ficheiro de saida
    int descomprimido=0;
    unsigned char caracter;
    int valor;
    ArvPtr topo = arvCod;
    ArvPtr percorre = topo;

    int buffer[8];
    int contador;
        for(int i=0; i < tamCod; i++) {
           if (fscanf(fp, "%c", &caracter)){};
           valor = (int)caracter;
           preparaBuffer(valor,buffer);
           //percorre o buffer
           for(contador=0;contador<8 && descomprimido != tamDesc; contador++){
               if(buffer[contador]==0)
                   percorre=percorre->left;else percorre=percorre->right;

               if(percorre->right==NULL && percorre->left==NULL){
                   caracter=percorre->caracter;
                   fprintf(fpo,"%c",caracter);descomprimido++;
                   percorre=topo;
                                                                }
                                                }

                               }

    return descomprimido;
}

ArvPtr monta_arvore( FILE *fp){
    //A funcao monta_arvore le o ficheiro cod e cria uma arvore binaria que para facilitar a descodificacao do ficheiro shaf
    int controle,aux;char caracter='0';
    ArvPtr topo = cria_nodo();
    ArvPtr percorre = topo;
    aux = 0;
        do {
            if(fscanf(fp,"%c",&caracter)){};
            controle = converte_char_int(caracter);
            if (controle == 0){
                if (percorre->left == NULL) percorre->left = cria_nodo();
                percorre = percorre->left;
            }

            else if (controle == 1){
                if (percorre->right == NULL) percorre->right = cria_nodo();
                percorre = percorre->right;
            }
            else {
                percorre->caracter = aux;
                aux++;
                percorre = topo;
            }

        } while (caracter != '@');
    return topo;
}


//regitra numero de blocos em um ficheiro do tipo shaf
int conta_num_blocos(FILE * fp) {
    //faz scanf do @ antes do num de blocos e do @ depois tambem
 int resp;
 char temp;
    if(fscanf(fp,"%c",&temp)){};
    if(fscanf(fp, "%d", &resp)){};
    if(fscanf(fp,"%c",&temp)){};
    return resp;
}


//Funcao que descodifica rle
int descompRLE(FILE* fp, FILE *fpo,int *comprimido_rle){
    int descomprimidos=0;
    int erro,vezes_num;//tamanho do ficheiro .rle
    unsigned char c,chr,vezes;
    erro=fscanf(fp,"%c",&c);
    (*comprimido_rle)++;
    while(erro==1){

        if(c == (unsigned char)0) {
            if (fscanf(fp,"%c%c",&chr,&vezes)){};
            vezes_num = (int)vezes;
            for(;vezes_num!=0;vezes_num--){
                fprintf(fpo,"%c",chr);
                descomprimidos++;
            }
        }
        else {
            fprintf(fpo,"%c",c);
            descomprimidos++;
        }

        erro = fscanf(fp,"%c",&c);
        (*comprimido_rle)++;
    }

    return descomprimidos;
}

int converte_char_int(char c){
    int resp;
    //relacao char int: '1'=1 , '0'=0 , ';'=-1 , '@'=-2
   if(c=='1') resp= 1;else if(c=='0') resp= 0; else if(c==';') resp= -1; else resp =-2;
return resp;
}

ArvPtr cria_nodo(){
    ArvPtr novo = (ArvPtr) malloc(sizeof(ArvPtr));
    novo->left = NULL;
    novo->right = NULL;
    novo->caracter = '0';
    return novo;
}
