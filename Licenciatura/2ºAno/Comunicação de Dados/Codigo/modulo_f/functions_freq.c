#include "functions_freq.h"




void do_freq(int* buffer, int tamBloco, int last, FILE *f){
    fprintf(f,"%d@",tamBloco);
    freq_escreve(f,buffer);
    fprintf(f,"@");
    if(last) fprintf(f,"0");
}



void freq_escreve(FILE *escreve,int* buffer){
    int count=0,simb_prox= -1, simb_ant,acabou = 0;
    while(count<=255){ 
        simb_ant = simb_prox;
        simb_prox = (buffer[count])? buffer[count] : 0;
        if (simb_ant >= 0){
            if (simb_ant == simb_prox){
                fprintf(escreve,"%d;",simb_ant);
                while(simb_ant == simb_prox && count < 255){
                    fprintf(escreve,";");
                    simb_ant = simb_prox;
                    count++;
                    simb_prox = (buffer[count])? buffer[count] : 0;

                }
                if (simb_ant == simb_prox){
                    acabou = 1;
                }
            
            }
            else{
                fprintf(escreve,"%d;",simb_ant);
            }
        }
        count++;
    }
    if (acabou == 0){
        simb_ant = simb_prox;
        fprintf(escreve,"%d",simb_ant);
    }
}


void inicia_freq(FILE *f, unsigned char r_n,int n_blocks){
    fprintf(f,"@%c@%d@",r_n,n_blocks);
}