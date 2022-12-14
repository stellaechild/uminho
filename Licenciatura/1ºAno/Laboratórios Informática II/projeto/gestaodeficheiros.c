#include "gestaodeficheiros.h"
#include "stdio.h"
#include "logicaprograma.h"
#include "camadadados.h"
#include "camadainterface.h"

int gravar (ESTADO *e,char nome[]){
    FILE *jogo;
    jogo = fopen(nome,"w");
    mostrar_tabuleiro(*e,jogo);
    imprime_lista_de_jogadas(e,jogo);
    fclose(jogo);
    return 2;
}

FIM ler (ESTADO *e,char nome[],int *mensagem) {
    FILE *jogo;
    FIM terminado;
    COORDENADA tmp;
    int jog,numero_jogadas;
    jogo = fopen(nome,"r");
    if (jogo == NULL) {*mensagem = 9; return ONGOING;}
    //LÊ TABULEIRO
    le_tabuleiro(e,jogo);
    //LÊ LISTA DE JOGADAS
    jog = le_lista_de_jogadas(e,jogo);
    //ALTERA DADOS FINAIS
    altera_jogador_atual_alvo(e,jog);
    numero_jogadas = obter_numero_de_jogadas(e);
    tmp.linha = 3;
    tmp.coluna = 4;
    if (numero_jogadas != 0) tmp = obter_jogada_alvo(e,numero_jogadas);
    altera_ultima_jogada(e,tmp);
    //FINALIZAÇÃO
    fclose(jogo);
    *mensagem = 3;
    terminado = jogo_terminado(e);
    if (terminado != ONGOING) *mensagem = 10;
    return terminado;
}

void le_tabuleiro (ESTADO *e,FILE *jogo){
    COORDENADA tmp;
    char lrchar;
    int prob;
    for (int lin = 0; lin <= 7;lin++){
        tmp.linha = lin;
        for (int col = 0; col <= 7;col++){
            tmp.coluna = col;
            prob = fscanf(jogo,"%c",&lrchar);
            if (prob == 1) altera_estado_casa(e,tmp,converte_char_para_casa(lrchar));
        }
        if (fscanf(jogo,"\n")){}
    }
    if (fscanf(jogo,"\n")){}
}

int le_lista_de_jogadas(ESTADO *e,FILE *jogo){
    int ficheiro_terminado = 0,jog;
    char lrchar,col1,col2,lin1,lin2;
    if ((fscanf(jogo,"%c",&lrchar)) != 1)ficheiro_terminado = 1;
    jog = 1;
    altera_numero_de_jogadas(e,0);
    while (ficheiro_terminado == 0) {
        incrementa_numero_de_jogadas(e);
        if (fscanf(jogo, "%*s %c%c %c%c\n", &col1, &lin1, &col2, &lin2) == 4) {
            altera_jogada(e,converte_chars_para_coordenada(lin1,col1));
            incrementa_numero_de_jogadas(e);
            altera_jogada(e,converte_chars_para_coordenada(lin2,col2));
        }
        else {
            altera_jogada(e,converte_chars_para_coordenada(lin1,col1));
            ficheiro_terminado = 1;
            jog = 2;
        }
        if ((fscanf(jogo, "%c", &lrchar)) != 1) ficheiro_terminado = 1;
    }
    return jog;
}