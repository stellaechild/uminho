#include <stdlib.h>
#include "camadadados.h"

ESTADO *inicializar_estado(){
    ESTADO *e = (ESTADO *) malloc(sizeof(ESTADO));
    altera_jogador_atual_alvo(e,1);
    altera_numero_de_jogadas(e,0);
    COORDENADA tmp;
    for (int lin = 0;lin <= 7;lin++){
        tmp.linha = lin;
        for (int col = 0;col <= 7;col++){
            tmp.coluna = col;
            altera_estado_casa(e,tmp,VAZIO);
        }
    }
    tmp.linha=3;tmp.coluna=4;
    altera_ultima_jogada(e,tmp);
    altera_estado_casa(e,tmp,BRANCA);
    tmp.linha=0;tmp.coluna=7;
    altera_estado_casa(e,tmp,JOG2);
    tmp.linha=7;tmp.coluna=0;
    altera_estado_casa(e,tmp,JOG1);
    return e;
}

int obter_jogador_atual(ESTADO *estado){
    int i;
    i= estado->jogador_atual;
    return i;
}

int obter_numero_de_jogadas(ESTADO *estado){
    int i;
    i= estado->num_jogadas;
    return i;
}

CASA obter_estado_casa(ESTADO *e, COORDENADA c) {
    int col, lin;
    CASA casa;
    col = c.coluna;
    lin = c.linha;
    if (col > 7 || col < 0 || lin > 7 || lin < 0) casa = PRETA;
    else casa = e->tab[lin][col];
    return casa;
}

COORDENADA obter_ultima_jogada(ESTADO *e){
    COORDENADA tmp;
    tmp = e->ultima_jogada;
    return tmp;
}

COORDENADA obter_jogada_alvo(ESTADO *e,int alvo){
    COORDENADA tmp;
    int aux = (alvo-1)/2;
    if (alvo % 2 == 0) tmp = e->jogadas[aux].jogador2;
    else tmp = e->jogadas[aux].jogador1;
    return tmp;
}

void altera_jogador_atual(ESTADO *e){
    int jog = e->jogador_atual;
    if (jog == 1) e->jogador_atual = 2;
    else e->jogador_atual = 1;
}

void altera_jogador_atual_alvo(ESTADO *e,int alvo){
    e->jogador_atual = alvo;
}

void altera_numero_de_jogadas(ESTADO *e,int alvo){
    e->num_jogadas = alvo;
}

int incrementa_numero_de_jogadas(ESTADO *e){
    e->num_jogadas++;
    return e->num_jogadas;
}

int decrementa_numero_de_jogadas(ESTADO *e){
    e->num_jogadas--;
    return e->num_jogadas;
}

void altera_estado_casa(ESTADO *e,COORDENADA  c,CASA casa){
    e->tab[c.linha][c.coluna] = casa;
}

void altera_ultima_jogada(ESTADO *e,COORDENADA c){
    e->ultima_jogada = c;
}

void altera_jogada(ESTADO *e,COORDENADA alvo){
    int num_jogadas = obter_numero_de_jogadas(e);
    int aux = (num_jogadas-1)/2;
    if (num_jogadas % 2 == 0) e->jogadas[aux].jogador2 = alvo;
    else e->jogadas[aux].jogador1 = alvo;
}

CASA converte_char_para_casa(char casa){
    CASA r;
    switch (casa) {
        case '#':r=PRETA;
            break;
        case '*':r=BRANCA;
            break;
        case '.':r=VAZIO;
            break;
        case '1':r=JOG1;
            break;
        case '2':r=JOG2;
            break;
        default: r=PRETA;
    }
    return r;
}

COORDENADA converte_chars_para_coordenada(char lin,char col){
    COORDENADA tmp;
    tmp.linha = lin * (-1) + 56;
    tmp.coluna = col - 'a';
    return tmp;
}

void converte_coordenada_para_char(COORDENADA c,char *lin,char *col){
    int li = c.linha * (-1) + 56;
    int co = c.coluna + 'a';
    *lin = (char)li;
    *col = (char)co;
}

void atualiza_estado(ESTADO *e,COORDENADA c){
    //Posição da BRANCA e coloca peça PRETA.
    altera_estado_casa(e,c,BRANCA);
    altera_estado_casa(e,obter_ultima_jogada(e),PRETA);
    //Número de jogadas
    incrementa_numero_de_jogadas(e);
    //Array
    altera_jogada(e,c);
    //Última jogada.
    altera_ultima_jogada(e,c);
    //Número de Jogador
    altera_jogador_atual(e);
}

int pos(ESTADO *e,int n){
    int num_jogs,r,passos;
    r=4;
    num_jogs=obter_numero_de_jogadas(e);
    if (n>=0&&n<num_jogs){
        passos = num_jogs - n*2;
        retrocede_num_passos(e,passos);
        altera_jogador_atual_alvo(e,1);
    }
    else r=8;
    return r;
}

void retrocede_num_passos(ESTADO *e,int n){
    COORDENADA tmp;
    int num_jogada = obter_numero_de_jogadas(e);
    //Ciclo de retrocedimento
    for (;n>0;n--) {
        altera_estado_casa(e,obter_jogada_alvo(e,num_jogada),VAZIO);
        decrementa_numero_de_jogadas(e);
        num_jogada = obter_numero_de_jogadas(e);
    }
    //Finalização
    if (num_jogada == 0){
        tmp.linha = 3;tmp.coluna = 4;
    }
    else tmp = obter_jogada_alvo(e,num_jogada);
    altera_estado_casa(e,tmp,BRANCA);
    altera_ultima_jogada(e,tmp);
}

ESTADO *estado_temporario(ESTADO *e){
    ESTADO *tmp = (ESTADO *) malloc(sizeof(ESTADO));
    altera_jogador_atual_alvo(tmp,obter_jogador_atual(e));
    altera_ultima_jogada(tmp,obter_ultima_jogada(e));
    COORDENADA c;
    for (int lin = 0;lin < 8;lin++) {
        c.linha = lin;
        for (int col = 0;col < 8;col++){
            c.coluna = col;
            altera_estado_casa(tmp,c,obter_estado_casa(e,c));
        }
    }
    return tmp;
}