/**
 * Data de criação : 24/04/2021
 * Vicente Moreira - A93296
 * Versão:
 * 20210424
 *
 * Ficheiro responsável pela apresentação das tables
 */

#include "../../includes/paginacao.h"
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define TERMINALWIDTH 230


static int calculate_colsizemax(int cols){
    return (TERMINALWIDTH-cols*3)/cols;
}

void print_table(TABLE target){
    int topdone = 0;
    if(table_getsizeCol(target) == NULL) table_calculate_colsize(target);
    int space = calculate_colsizemax(table_getNumColunasTOP(target));
    int infoline = table_getInfoLine(target);                                                       //Caso a infoline tenha sido set, escreve até a info line.
    for(int x = 0;(!infoline && x < table_getNumLinhas(target)) || (infoline && x < infoline);x++){ //Senão escreve todas as lines.
        if(x == 1 && !topdone && table_topline(target)){    //Se não escreveste o topo e ele esta defenido? Escreve-o
            print_table_top(target);
            x--;
            topdone = 1;
        } 
        else {
            for(int y = 0;y<table_getNumColuna(target,x);y++){  //Para cada elemento na line
                int textlen,remaining;
                textlen = table_get_element_size(target,x,y);   //Vê o tamanho do elemento
                putchar(' ');
                char *holder = table_get_element(target,x,y);
                if(textlen > space){                           //Se ultrapassa o limite adiciona "...\0"
                    holder[space] = '\0';
                    holder[space-1] = '.';
                    holder[space-2] = '.';
                    holder[space-3] = '.';
                    remaining = 0;
                }
                else {                                        //Calcula o tamanho dos espaços depois do elemento
                    if (table_get_colSizePos(target,y) > space) remaining = space-textlen;
                    else remaining = table_get_colSizePos(target,y)-textlen;
                }
                printf("%s",holder);     //Escreve o elemento
                remaining++;
                while(remaining > 0){    //Põe os espaços
                    putchar(' ');
                    remaining--;
                }
                if (y+1 < table_getNumColuna(target,x)) putchar('|'); //Terminador de coluna
                else putchar('\n');
                free(holder);
            }
        }
    }
    if (infoline){  //Se tem info_line... imprime-a
        print_table_top(target);
        putchar('\n');
        print_table_info(target);
    }
}

void print_table_top(TABLE target){  
    int remaining = 0;
    int max = calculate_colsizemax(table_getNumColunasTOP(target));
    for(int aux = 0;aux < table_getNumColunasTOP(target);aux++){ //Calcula o número de linhas necessárias
        int holder = table_get_colSizePos(target,aux)+3;
        if (holder > max) remaining+=max+3;
        else remaining+= table_get_colSizePos(target,aux)+3;
    }
    while(remaining > 0){
    putchar('-');
    remaining--;
    }
    putchar('\n');
}

void print_table_info(TABLE target){
    for(int aux = table_getInfoLine(target);aux < table_getNumLinhas(target);aux++){
        char* holder = table_get_element(target,aux,0);
        printf("%s\n",holder);
        free(holder);
    }
}
