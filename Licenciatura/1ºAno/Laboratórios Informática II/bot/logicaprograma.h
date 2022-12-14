#ifndef LOGICA
#define LOGICA

int jogar(ESTADO *estado, COORDENADA c);

int jogada_valida(ESTADO *e,COORDENADA c);

FIM jogo_terminado(ESTADO *e);

FIM verifica_a_volta (ESTADO *e);

COORDENADA pos_coordenada(COORDENADA jog,int pos);

#endif
