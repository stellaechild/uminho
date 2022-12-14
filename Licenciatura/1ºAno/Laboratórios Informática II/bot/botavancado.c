#include "botavancado.h"
#include <stdlib.h>
#include "logicaprograma.h"

BOTAVANCADOSTRUCT *cria_struct_avancada(){
    BOTAVANCADOSTRUCT *tmp = malloc(sizeof(BOTAVANCADOSTRUCT));
    return tmp;
}

void liberta_bot_struct(BOTAVANCADOSTRUCT *bot){
    BOTAVANCADOSTRUCT *n;
    LISTA aux = obter_lista_da_struct(bot);
    LISTA tmp;
    while (lista_esta_vazia(aux) != 0){
        n = devolve_cabeca(aux);
        liberta_bot_struct(n);
        tmp = aux;
        aux = proximo(aux);
        free(tmp);
    }
    free(aux);
    free(obter_estado(bot));
}

int obter_rating (BOTAVANCADOSTRUCT *tmp){
    return tmp->rating;
}
COORDENADA obter_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp){
    return tmp->jogada;
}

ESTADO *obter_estado (BOTAVANCADOSTRUCT *tmp){
    return tmp->e;
}

LISTA obter_lista_da_struct(BOTAVANCADOSTRUCT *tmp){
    return tmp->jogadasPossiveis;
}

void altera_rating (BOTAVANCADOSTRUCT *tmp,int rating){
    tmp->rating = rating;
}
void altera_coordenada_botstruct (BOTAVANCADOSTRUCT *tmp,COORDENADA c){
    tmp->jogada = c;
}

void altera_estado (BOTAVANCADOSTRUCT *tmp,ESTADO *estado){
    tmp->e = estado;
}

void altera_lista_na_struct (BOTAVANCADOSTRUCT *tmp,LISTA L){
    tmp->jogadasPossiveis = L;
}


BOTAVANCADOSTRUCT *cria_struct_pronta(ESTADO *e,COORDENADA c){
    BOTAVANCADOSTRUCT *bot = cria_struct_avancada();
    altera_coordenada_botstruct(bot,c);
    ESTADO *tmp = estado_temporario(e);
    jogar(tmp, c);
    altera_estado(bot,tmp);
    altera_rating(bot,avalia_tabuleiro_avancado(tmp));
    altera_lista_na_struct(bot,cria_lista());
    return bot;
}


int jog2 (ESTADO *e){
    BOTAVANCADOSTRUCT *bot = NULL;
    BOTAVANCADOSTRUCT *temp = NULL;
    COORDENADA c;
    int terminado = 0;
    int pos,rat,r;
    LISTA L = cria_lista();
    L = adiciona_jogadas_a_volta(e,L);
    for (LISTA dopio = L;lista_esta_vazia(dopio)!=0 && terminado == 0;dopio=proximo(dopio)){
        bot = devolve_cabeca(dopio);
        if (obter_rating(bot) != 1000){
            LISTA aux1 = obter_lista_da_struct(bot);
            aux1 = adiciona_jogadas_a_volta(obter_estado(bot),aux1);
            pos = avalia_jogada_melhor_inimigo(aux1);
            aux1 = apaga_membros_menores(aux1,pos);
            if (lista_esta_vazia(aux1) != 0){
                temp = devolve_cabeca(aux1);
                if (obter_rating(temp)==1000) altera_rating(bot,-1000);
                LISTA aux2 = obter_lista_da_struct(temp);
                aux2 = adiciona_jogadas_a_volta(obter_estado(temp),aux2);
                rat = obter_rating(bot);
                if (rat == 1000) terminado = 1;
                altera_rating(bot,avalia_jogada_maxima(aux2,rat));
            }
        }
        else terminado = 1;
    }
    if (terminado == 0) c = avalia_jogada_melhor_final(L);
    else c = obter_coordenada_botstruct(bot);
    r = jogar(e,c);
    if (r == 1) r=5;
    free(bot);
    free(temp);
    apaga_memoria(L);
    return r;
}

void apaga_memoria(LISTA L){
    BOTAVANCADOSTRUCT *bot;
    LISTA tmp;
    while (lista_esta_vazia(L) != 0){
        bot = devolve_cabeca(L);
        liberta_bot_struct(bot);
        tmp = L;
        L = proximo(L);
        free(tmp);
    }
    free(L);
}

LISTA adiciona_jogadas_a_volta(ESTADO *e,LISTA L){
    COORDENADA c;
    CASA teste;
    for (int pos = 1;pos < 9;pos++) {
        c = pos_coordenada(obter_ultima_jogada(e), pos);
        teste = obter_estado_casa(e, c);
        if (teste != PRETA) {
            BOTAVANCADOSTRUCT *bot = cria_struct_pronta(e,c);
            L = insere_cabeca(L,bot);
        }
    }
    return L;
}

int avalia_jogada_melhor_inimigo(LISTA L){
    int max,cont,pos,rat;
    pos = 0;cont = 0;max =-1001;
    BOTAVANCADOSTRUCT *bot;
    for(LISTA dopio = L;lista_esta_vazia(dopio)!=0;dopio=proximo(dopio)){
        bot = devolve_cabeca(dopio);
        rat = obter_rating(bot);
        if (rat > max){
            max = rat;
            pos = cont;
        }
        cont++;
    }
    return pos;
}

int avalia_jogada_maxima(LISTA L,int med){
    int r=med;
    if (r != -1000 && r != 1000) {
        int cont;
        r= 0;
        BOTAVANCADOSTRUCT *bot;
        for (LISTA dopio = L; lista_esta_vazia(dopio) != 0; dopio = proximo(dopio)) {
            bot = devolve_cabeca(dopio);
            cont = obter_rating(bot);
            if (cont > r){
                r=cont;
            }
        }
    }
    return r;
}

COORDENADA avalia_jogada_melhor_final(LISTA L){
    int max,rat;
    COORDENADA c;
    c.linha = -1; c.coluna=-1;
    max = -1001;
    BOTAVANCADOSTRUCT *bot;
    for(LISTA dopio = L;lista_esta_vazia(dopio)!=0;dopio=proximo(dopio)){
        bot = devolve_cabeca(dopio);
        rat = obter_rating(bot);
        if (rat > max){
            max = rat;
            c = obter_coordenada_botstruct(bot);
        }
    }
    return c;
}

LISTA apaga_membros_menores(LISTA L,int pos){
    BOTAVANCADOSTRUCT *bot;
    for (int aux = 0;aux != pos;aux++){
        bot = devolve_cabeca(L);
        liberta_bot_struct(bot);
        L = remove_cabeca(L);
    }
    return L;
}

int avalia_tabuleiro_avancado(ESTADO *e){
    int col,lin,rating;
    FIM terminado;
    COORDENADA tmp = obter_ultima_jogada(e);
    col = tmp.coluna;
    lin = tmp.linha * (-1) + 7;
    rating = col+lin;
    terminado = jogo_terminado(e);
    if (terminado != ONGOING){
        if (terminado == JOG2GANHA) rating = 1000;
        else rating = -1000;
        if (obter_jogador_atual(e) == 2) rating = (-rating);
    }
    else {
        if (obter_jogador_atual(e) == 2) rating = 14-rating;
    }

    return rating;
}