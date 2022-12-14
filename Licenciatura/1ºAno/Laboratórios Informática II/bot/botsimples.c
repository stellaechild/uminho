#include "botsimples.h"
#include <stdlib.h>
#include "logicaprograma.h"

BOTSIMPSTRUCT *cria_bot_struct_simples(){
    BOTSIMPSTRUCT *tmp = malloc(sizeof(BOTSIMPSTRUCT));
    return tmp;
}

int obter_botsimp_rating(BOTSIMPSTRUCT *tmp){
    return tmp->rating;
}

COORDENADA obter_botsimp_coordenada(BOTSIMPSTRUCT *tmp){
    return tmp->casa;
}

void altera_botsimp_rating(BOTSIMPSTRUCT *tmp,int rat){
    tmp->rating = rat;
}

void altera_botsimp_coordenada(BOTSIMPSTRUCT *tmp,COORDENADA c){
    tmp->casa = c;
}

int jog (ESTADO *e){
    int pos,r;
    CASA teste;
    COORDENADA c;
    LISTA L = cria_lista();
    for (pos = 1;pos < 9;pos++) {
        c = pos_coordenada(obter_ultima_jogada(e), pos);
        teste = obter_estado_casa(e, c);
        if (teste != PRETA) {
            BOTSIMPSTRUCT *bot = cria_bot_struct_simples();
            altera_botsimp_coordenada(bot,c);
            ESTADO *tmp = estado_temporario(e);
            jogar(tmp, c);
            altera_botsimp_rating(bot,avalia_tabuleiro(tmp));
            L = insere_cabeca(L, bot);
            free(tmp);
        }
    }
    c = avalia_melhor_jogada_da_lista(L);
    r = jogar(e,c);
    liberta_lista(L);
    if (r == 1) r=5;
    return r;
}


int avalia_tabuleiro(ESTADO *e){
    int col,lin,rating;
    COORDENADA tmp = obter_ultima_jogada(e);
    col = tmp.coluna;
    lin = tmp.linha * (-1) + 7;
    rating = col+lin;
    if (obter_jogador_atual(e) == 2) rating = 14-rating;
    return rating;
}

COORDENADA avalia_melhor_jogada_da_lista(LISTA L){
    COORDENADA c;
    c.linha = 3; c.coluna = 4;
    int max = 0;
    BOTSIMPSTRUCT *bot;
    for (;lista_esta_vazia(L)!=0;L=proximo(L)){
        bot = devolve_cabeca(L);
        if (obter_botsimp_rating(bot) > max) {
            c = obter_botsimp_coordenada(bot);
            max = obter_botsimp_rating(bot);
        }
    }
    return c;
}