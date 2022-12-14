
#ifndef DADOS
#define DADOS

typedef enum {VAZIO = '.', BRANCA = '*', PRETA = '#',JOG1 = '1',JOG2 = '2'} CASA;

typedef enum {ONGOING,JOG1GANHA,JOG2GANHA,SEMJOGADAS} FIM;

typedef struct {
        int coluna;
        int linha;
} COORDENADA;

typedef struct {
        COORDENADA jogador1;
        COORDENADA jogador2;
} JOGADA;

typedef JOGADA JOGADAS[32];

typedef struct {
        CASA tab[8][8];
        COORDENADA ultima_jogada;
        JOGADAS jogadas;
        int num_jogadas;
        int jogador_atual;
} ESTADO;

ESTADO *inicializar_estado();

int obter_jogador_atual(ESTADO *estado);

int obter_numero_de_jogadas(ESTADO *estado);

CASA obter_estado_casa(ESTADO *e, COORDENADA c);

COORDENADA obter_ultima_jogada(ESTADO *e);

COORDENADA obter_jogada_alvo(ESTADO *e,int alvo);

void altera_jogador_atual(ESTADO *e);

void altera_jogador_atual_alvo(ESTADO *e,int alvo);

void altera_numero_de_jogadas(ESTADO *e,int alvo);

int incrementa_numero_de_jogadas(ESTADO *e);

int decrementa_numero_de_jogadas(ESTADO *e);

void altera_estado_casa(ESTADO *e,COORDENADA c,CASA casa);

void altera_ultima_jogada(ESTADO *e,COORDENADA c);

void altera_jogada(ESTADO *e,COORDENADA alvo);

CASA converte_char_para_casa(char casa);

COORDENADA converte_chars_para_coordenada(char lin,char col);

void converte_coordenada_para_char(COORDENADA c,char *lin,char *col);

void atualiza_estado(ESTADO *e,COORDENADA c);

int pos(ESTADO *e,int n);

void retrocede_num_passos(ESTADO *e,int n);

ESTADO *estado_temporario(ESTADO *e);

#endif
