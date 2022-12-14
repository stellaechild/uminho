#include "camadadados.h"
#include "logicaprograma.h"

int jogar(ESTADO *e, COORDENADA c) {
    int r=6;
    if (jogada_valida(e,c) == 1) {
        atualiza_estado(e,c);
        r=1;
    }
    return r;
}

int jogada_valida(ESTADO *e,COORDENADA c){
    int val,difLin,difCol;
    COORDENADA tmp = obter_ultima_jogada(e);
    val = 1;
    if (obter_estado_casa(e,c) == PRETA) val = 0;
    else {
        difLin = tmp.linha - c.linha;
        difCol =  tmp.coluna - c.coluna;
        if ((difCol == 0 && difLin == 0) || (difCol > 1 || difCol < -1) || (difLin > 1 || difLin < -1)) val = 0;
    }
    return val;
}

FIM jogo_terminado (ESTADO  *e){ // 0 - Jogo não acabou.    1 - Jogador1 ganha.      2 - Jogador2 ganha.
    int col,lin;
    FIM terminado;
    COORDENADA tmp = obter_ultima_jogada(e);
    lin = tmp.linha;
    col = tmp.coluna;
    if (lin == 0 && col == 7) terminado = JOG2GANHA;
    else if (lin == 7 && col == 0) terminado = JOG1GANHA;
    else {
        terminado = verifica_a_volta(e);
    }
    if (terminado == SEMJOGADAS){
        if (obter_jogador_atual(e) == 2) terminado = JOG1GANHA;
        else terminado = JOG2GANHA;
    }
    return terminado;
}

FIM verifica_a_volta (ESTADO *e){
    CASA r;
    FIM terminado;
    COORDENADA c;
    terminado = SEMJOGADAS;
    for (int aux = 1; aux <9;aux++){
        c = pos_coordenada(obter_ultima_jogada(e),aux);
        r = obter_estado_casa(e,c);
        if (r != PRETA) terminado = ONGOING;
    }
    return terminado;
}

COORDENADA pos_coordenada(COORDENADA jog,int pos){
    if (pos>=2 && pos <= 4) jog.coluna++;
    else if (pos>=6 && pos <= 8) jog.coluna--;
    if (pos== 1 || pos == 8 || pos ==2) jog.linha--;
    else if (pos>=4 && pos <= 6) jog.linha++;
    return jog;
}