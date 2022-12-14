#include "functions.h"
#include "../comum.h"



void interface (Blocks txt, RleBlock rle, const char *name, double execTime, int tamRle_file){
    int atual;
    printf("\nAutores: Pedro Araujo (a90614) / Rafael dos Anjos Areas(a86817) ; MIEI/CD ;\n\n"
           "Modulo: f (Calculo das frequencias dos simbolos)\n");
    printf("Numero de blocos: %d\n", txt->n_blocks);
    printf("Tamanho dos blocos analisados no ficheiro original: ");
    for(atual = 1; atual < txt->n_blocks; atual++) printf("%d/",txt->tam_blocks);
    printf("%d\n",txt->tam_lastBlock);
    
    if(rle!=NULL){
        printf("Compressao RLE: %.2f%c\n", (verifica_compressao(tamRle_file,txt->tam_total)),'%');
        printf("Tamanho dos blocos analisados no ficheiro RLE: ");
        while (rle->prox){
            printf("%d/",rle->tam_block);
            rle = rle->prox;
        }
        printf("%d\n",rle->tam_block);     
        imprime_tempo_exec(execTime);
        printf("Ficheiros gerados: %s.rle, %s.rle.freq\n", name, name);        
    }
    else{
        printf("Compressao RLE: < 5%c\n",'%');
        printf("Tamanho dos blocos analisados no ficheiro RLE: Nao houve compressao suficiente\n");
        imprime_tempo_exec(execTime);
        printf("Ficheiros gerados: %s.freq\n", name);
    }
}


int error_file(const char *name){
    FILE *file = fopen(name, "rb");
    if (file == NULL) {
        printf("Arquivo nao lido, tente novamente\n");
        return 0;
    }
    fseek(file, 0, SEEK_END);
    if (ftell(file) < TAM_MIN){
        printf("Arquivo %s muito pequeno, tente outro arquivo\n",name);
        return 0;
    }
    fclose(file);
    return 1;


}



void calcula_tamBlock(Blocks *b, const char *name,char tam){
    FILE *file = fopen(name, "rb");
    int tamanho_bloco;
    
    fseek(file, 0, SEEK_END);
    (*b)->tam_total = ftell(file);

    if (tam == 'K') tamanho_bloco = 640*1024;//             | Determina o tamanho de cada bloco de acordo com o que o usuário escolher.
    else if (tam == 'm') tamanho_bloco = 8*1024*1024;//     | Caso o usuário não determine, é utilizado o tamanho padrão 64KB
    else if (tam == 'M') tamanho_bloco = 64*1024*1024;//    | 
    else tamanho_bloco = TAM;//                             |

    (*b)->tam_blocks = tamanho_bloco;
    fclose(file);
    
    if(((*b)->tam_total) < ((*b)->tam_blocks)){//               |
        (*b)->n_blocks = 1;//                                   | Caso o tamanho do bloco estipulado seja superior que o tamanho do arquivo,
        (*b)->tam_lastBlock = (*b)->tam_total;//                | Haverá apenas 1 bloco com o tamanho do arquivo
    } 

    else {
        (*b)->n_blocks = ((*b)->tam_total)/((*b)->tam_blocks);
        int resto = ((*b)->tam_total)%((*b)->tam_blocks);
        if (resto != 0){
            // Verifica se o ultimo bloco é menor do que 1KB
            // Se ele for, ele adiciona no penultimo bloco
            if (resto < TAM_MIN){ 
                (*b)->tam_lastBlock = ((*b)->tam_blocks) + resto;
            }
            else{
                (*b)->tam_lastBlock = resto;
                (*b)->n_blocks++;
            }
        }
        else (*b)->tam_lastBlock = (*b)->tam_blocks;
    }
}






void novo_rleBlock(RleBlock *rleb, int tamB){ 
    RleBlock novo = malloc(sizeof(struct rleblock));
    novo->tam_block = tamB;
    novo->prox = NULL;
    *rleb = novo;
}

void freeRleBlock(RleBlock *rleb)  {
    RleBlock current = (*rleb);

    while (current != NULL)
    {
        current = (*rleb) -> prox;
        free(*rleb);
        (*rleb) = current;
    }
}




 
void get_Simb(int * buffer_freq,unsigned char *string, int tam){ 
    int i = 0,j;
    unsigned char atual; 
    int freq;

    // Guarda no buffer_freq a frequencia de cada símbolo 
    while(i<tam){
        atual = string[i];  
        j = (int) atual;    
        buffer_freq[j]++;
        i++;
    }
}

float verifica_compressao(int freq_rle, int freq_txt){
    float compressao;
    compressao = (freq_txt-freq_rle);
    compressao /= freq_txt;
    compressao *= 100;
    return compressao;
}
