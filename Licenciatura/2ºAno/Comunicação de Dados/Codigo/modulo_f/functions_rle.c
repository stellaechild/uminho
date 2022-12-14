#include "functions_rle.h"
 
int rle_or_not(unsigned char *txt, int tam){
   int i,j,freqRep,freqTxt,freqRle, atual;
   i=0;freqTxt = freqRle = 0;
   while(i<tam){
        atual = txt[i];
        j = i+1;
        freqRep=1;
        while(atual == txt[j] && j<tam){
            freqRep++;
            j++; 
        }
        if(freqRep >= 4 || atual == '0') freqRle+=3;
        else freqRle+=freqRep;
        freqTxt+=freqRep;    
        i = j;
    }
    return (((verifica_compressao(freqRle,freqTxt)) < 5)? 0 : 1);  
    
} 



int escreveRle(FILE *rle, FILE *ftxt, int *buffer_freq, int tam){
    int freqTotal = 0,freq=1;
    unsigned char atualc,anterior;

    if (fscanf(ftxt,"%c",&atualc)){}; // Leitura do primeiro caracter do bloco
    anterior = atualc;

    for (int aux = 1; aux < tam;aux++){
        if (fscanf(ftxt,"%c",&atualc)){}; // Leitura de cada caracter do bloco  
        
        if (atualc != anterior){ // Caso o caracter anterior seja diferente do seguinte 
            
            if (freq>=4 || anterior == (char) 0){
                fprintf(rle,"%c",(char) 0);//                                       |
                fprintf(rle,"%c",anterior);//                                       |Caso tenha mais de 3 caracteres
                fprintf(rle,"%c",(char)freq);//                                     |iguais consecutivos
                buffer_freq[0]++; buffer_freq[anterior]++; buffer_freq[freq]++;//   |
                freqTotal +=3;//
            }

            else{   // Menos de 4 caracteres iguais consecutivos
                if(freq == 0) freq++;
                for (;freq!=0;freq--) {
                    fprintf(rle, "%c", anterior);
                    buffer_freq[anterior]++;
                    freqTotal++;
                }
            }
            freq=1;
        }
        else { // Caracteres anterior e atual iguais
            freq++;
            if (freq == 255){ // Não permite a frequencia ultrapasse 255 
                fprintf(rle,"%c",(char) 0);
                fprintf(rle,"%c",anterior);
                fprintf(rle,"%c",(char)freq);
                buffer_freq[0]++; buffer_freq[anterior]++; buffer_freq[freq]++;
                freqTotal +=3;
                freq=0;
            }
        }
        anterior = atualc;
    }
    
    // Escrita do(s) ultimo(s) caracter(es) do bloco
    if (freq>=4){
        fprintf(rle,"%c",(char) 0);
        fprintf(rle,"%c",anterior);
        fprintf(rle,"%c",(char)freq);
        buffer_freq[0]++; buffer_freq[anterior]++; buffer_freq[freq]++;
        freqTotal +=3;
    }
    else{
        for (;freq!=0;freq--) {
            fprintf(rle, "%c", anterior);
            buffer_freq[anterior]++;
            freqTotal++;
        }
    }

    return freqTotal;
}


