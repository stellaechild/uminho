#include "functions.h"
#include "modulo_f.h"
#include "functions_rle.h"
#include "functions_freq.h"
#include "../comum.h"


void do_txt(Blocks txt, char *name){
   FILE *ftxt, *ffreq;//                                    |
   int atual = 1, i, tam_bloco_atual = txt->tam_blocks;//   | Declaracao de variaveis
   int buffer_freq[256];//                                  |
   unsigned char *buffer;//                                 |

   size_t tamanho_filename = strlen(name);//    |
   char nome_freq[tamanho_filename+6];//        | Fopen dos ficheiros necesarios
   trata_nome_ficheiro(name,nome_freq,1,0);//   |
   ffreq = fopen(nome_freq, "wb");//            |

   if((ftxt = fopen(name,"rb"))!= NULL){
       inicia_freq(ffreq,'N',txt->n_blocks);
       for(;atual <= txt->n_blocks; atual++){
           if (atual == txt->n_blocks) tam_bloco_atual = txt->tam_lastBlock;   
           
           buffer = (unsigned char *) malloc(tam_bloco_atual);//                |
           if (fread(buffer, sizeof(unsigned char), tam_bloco_atual, ftxt)){};//| INICIALIZAÇÃO DOS BUFFERS
           for(i=0;i<256; i++) buffer_freq[i] = 0;//                            |
           
           get_Simb(buffer_freq,buffer, tam_bloco_atual);//|  GUARDA FREQ DOS SIMBS

           if (atual == txt->n_blocks) do_freq(buffer_freq,txt->tam_lastBlock, 1, ffreq);//  | ESCRITA
           else do_freq(buffer_freq,txt->tam_blocks,0, ffreq);//                             |     .FREQ                            
            
           free(buffer);
           progresso(atual,txt->n_blocks);
       }
       fclose(ftxt);
       fclose(ffreq);
    }
   else printf("Arquivo original não lido, tente novamente\n");
}


int do_rle(RleBlock *rle, Blocks txt,char *name){
    fprintf(stdout,"A executar compressao RLE...\n"); // Interface
    FILE *frle,*ftxt, *ffreq;//             |
    int atual = 1,freqTotal_rle = 0,i;//    |
    int buffer_freq[256];//                 | Declaração de variaveis
    unsigned char *buffer;//                |
    int tamBlock_rle = 0;//                 |
    int tam_bloco_atual = txt->tam_blocks;//|
    
    ftxt = fopen( name ,"rb");//                    |
    size_t tamanho_filename = strlen(name);//       |
    char nome_freq_rle[tamanho_filename+10];//      |
    char nome_rle[tamanho_filename+5];//            | Fopen dos ficheiros necesarios
    trata_nome_ficheiro(name,nome_freq_rle,1,1);//  |
    trata_nome_ficheiro(name,nome_rle,0,0);//       |
    frle = fopen(nome_rle,"wb");//                  |
    ffreq = fopen(nome_freq_rle,"wb");//            |
         
    inicia_freq(ffreq,'R',txt->n_blocks); 
    for(;atual <= txt->n_blocks; atual++){
            for(i=0;i<256; i++) buffer_freq[i] = 0; 
            if (atual == txt->n_blocks) tam_bloco_atual = txt->tam_lastBlock;   
           
            tamBlock_rle = escreveRle(frle, ftxt, buffer_freq, tam_bloco_atual);// ESCRITA RLE / 
                                                                               //    GUARDA FREQ DE CADA SIMB 
            
            if (atual == txt->n_blocks) do_freq(buffer_freq,tamBlock_rle, 1, ffreq);//  | ESCRITA
            else do_freq(buffer_freq,tamBlock_rle, 0, ffreq);//                         |   .FREQ
            
            novo_rleBlock(rle,tamBlock_rle); // GUARDA O TAMANHO DO BLOCO RLE ESCRITO
            rle = &((*rle)->prox);
            freqTotal_rle += tamBlock_rle;
            progresso(atual,txt->n_blocks);
    }
    fclose(frle);
    fclose(ftxt);
    fclose(ffreq);

    return freqTotal_rle;
}




// -------------------> FUNÇÃO PRINCIPAL <--------------------- //
int modulo_f(char *nome,int forcar_rle,char tam_bloco) {
    fprintf(stdout,"A iniciar a execucao do modulo F...\n\n");
    clock_t begin = clock();
    int tamRle_file = 0;
    FILE *file;
   
    if (error_file(nome)){
            Blocks txt = malloc(sizeof(struct bloco)); 
            RleBlock rle = NULL; 
            calcula_tamBlock(&txt,nome,tam_bloco);
            file = fopen(nome, "rb");
            if (forcar_rle == 1){
                    tamRle_file = do_rle(&rle,txt,nome); // Faz 
            }
            else{
            //leitura do primerio bloco apenas para verificar compressão rle  
                unsigned char *buffer = (unsigned char *) malloc(txt->tam_blocks);
                int bytes_read = fread(buffer, sizeof(unsigned char), txt->tam_blocks, file); 
                fclose(file);
                int r_or_n = rle_or_not(buffer, txt->tam_blocks); //Verifica compressão rle
                free(buffer);
                if(r_or_n==1) { // Faz o caminho de geração dos ficheiros rle 
                    tamRle_file = do_rle(&rle,txt,nome);
                }
                else{//Faz o caminho normal, geração direta do ficheiro .freq
                    do_txt(txt,nome);
                }
            }
            clock_t end = clock();
            double execTime = ((double)(end - begin)/CLOCKS_PER_SEC)*1000;
            interface(txt,rle,nome,execTime, tamRle_file);
            free(txt);
            if (rle != NULL) freeRleBlock(&rle);
    }
    return 0;
}

