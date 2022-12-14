#include <stdio.h>
#include <string.h>
#include "camadadados.h"
#include "logicaprograma.h"
#include "camadainterface.h"
#include "botsimples.h"
#include "botavancado.h"
#include "gestaodeficheiros.h"
#define BUF_SIZE 1024

void mostrar_tabuleiro(ESTADO estado,FILE *nome, int modo){ // 1 - consola
  CASA casa;
  int num;
  COORDENADA tmp;
  for (int lin = 0;lin <= 7;lin++){
      tmp.linha = lin;
      if (modo==1) {
          num = 8 + (lin * (-1));
          fprintf(nome, "%d ", num);
      }
    for (int col = 0;col<= 7; col++){
        tmp.coluna = col;
        casa = obter_estado_casa(&estado,tmp);
        fprintf(nome,"%c",casa);
        if (modo == 1) fprintf(nome," ");
    }
    fprintf(nome,"\n");
  }
  if (modo==1) fprintf(nome, "  A B C D E F G H\n");
  fputc('\n',nome);
}

int interpretador(ESTADO *e) {
  FIM terminado;
  char linha[BUF_SIZE];
  char col[2], lin[2];
  int jogador_atual,numero_jogada,mensagem;
  terminado=ONGOING;
  while (terminado == ONGOING) {
      mensagem = 0;
      jogador_atual = obter_jogador_atual(e);
      numero_jogada = obter_numero_de_jogadas(e)/2+1;
      mostrar_tabuleiro(*e,stdout,1);
      printf("(Jogada %d) Jogador %d:",numero_jogada,jogador_atual);
      if (fgets(linha, BUF_SIZE, stdin) == NULL) return 0;
      printf("\x1B[1;1H\x1B[2J");
      //COMANDO JOGADAS
      if (strlen(linha) == 3 && sscanf(linha, "%[a-h]%[1-8]", col, lin) == 2) {
          COORDENADA coord = {*col - 'a', (*lin - '1') * (-1) + 7};
          mensagem = jogar(e, coord);
          terminado = jogo_terminado(e);
      }
   if (mensagem == 0) mensagem = interpreta_comando(linha,&terminado,e);
   escreve_mensagem(mensagem);
  }
  printf("\x1B[1;1H\x1B[2J");
  mostrar_tabuleiro(*e,stdout,1);
  printf("Jogo terminado: ");
  if (terminado == SEMJOGADAS) printf ("Não houve conclusão\n");
  else printf("Jogador %d ganhou!\n",terminado);
  return 1;
}

int interpreta_comando(char linha[],FIM *terminado,ESTADO *e){
    char nome[BUF_SIZE],cmd[BUF_SIZE];
    nome[0] = '\0';
    int n,mensagem = 0;
    sscanf(linha,"%s %s",cmd,nome);
    //COMANDO QUIT
    if (strcmp(cmd,"Q") == 0){
        *terminado = SEMJOGADAS;
    }
    //COMANDO GRAVAR
    if (strcmp(cmd,"gr") == 0){
        if (nome[0] == '\0') mensagem = 7;
        else mensagem = gravar(e,nome);
    }
    //COMANDO LER
    if (strcmp(cmd,"lr") == 0){
        if (nome[0] == '\0') mensagem = 7;
        else {
            *terminado = ler(e,nome,&mensagem);
        }
    }
    //COMANDO MOVS
    if (strcmp(cmd,"movs") == 0){
        imprime_lista_de_jogadas (e,stdout);
        mensagem = -1;
    }
    //COMANDO POS
    if (strcmp(cmd,"pos")==0) {
        if(nome[1]=='\0') n=nome[0]-48;
        else {
            n = (nome[0] - 48) * 10;
            n += nome[1] - 48;
        }
        mensagem = pos(e,n);
    }
    //COMANDO JOG1
    if (strcmp(cmd,"jog1") == 0){
        mensagem = jog(e);
        *terminado=jogo_terminado(e);
    }
    //COMANDO JOG2
    if (strcmp(cmd,"jog2") == 0){
        mensagem = jog2(e);
        *terminado=jogo_terminado(e);
    }
    return mensagem;
}

void escreve_mensagem(int mensagem){
    switch (mensagem) {
        case 0: printf("Operação/comando inválido\n");
            break;
        case 1: printf("Jogada efetuada com sucesso!\n");
            break;
        case 2: printf("Jogo gravado com sucesso\n");
            break;
        case 3: printf("Jogo lido com sucesso\n");
            break;
        case 4: printf("Jogo retrocedido com sucesso.\n");
            break;
        case 5: printf("Jogada efetuada pelo bot com sucesso\n");
            break;
        case 6: printf("Jogada inválida\n");
            break;
        case 7: printf("Nome vazio inválido\n");
            break;
        case 8: printf("Comando efetuado sem sucesso, número de posição inválido.\n");
            break;
        case 9: printf("Jogo a ser lido inexistente.\n");
            break;
        case 10: printf("Jogo lido com sucesso, mas este já esta terminado.\n");
            break;
        default:;
    }
}

void imprime_lista_de_jogadas (ESTADO *e,FILE *nome){
    int aux,jog,numjogadaD,numjogadaU,inteiro,numero_jogadas;
    char lin;char col;
    jog = 1;
    numero_jogadas = obter_numero_de_jogadas(e);
    for (inteiro = 0; inteiro < numero_jogadas; inteiro++) {
        aux=inteiro/2;
        if (jog==1) {
            numjogadaD = (aux+1) / 10;
            numjogadaU = (aux+1) - numjogadaD * 10;
            fprintf(nome, "%d%d: ", numjogadaD, numjogadaU);
        }
        else fprintf(nome," ");
        converte_coordenada_para_char(obter_jogada_alvo(e,inteiro+1),&lin,&col);
        fprintf(nome, "%c%c",col,lin);
        if (jog==1) jog=2;
        else {jog=1;fputc('\n',nome);}
    }
    if (jog==2) fputc('\n',nome);
}