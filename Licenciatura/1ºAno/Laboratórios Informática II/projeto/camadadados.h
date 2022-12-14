#ifndef DADOS
#define DADOS
/**
@file camadadados.h
Este módulo é usada para a declaração e modificação das estruturas de dados utilizadas no projeto.
*/

/**
\brief Tipos de casa possíveis para o tabuleiro.
 */
typedef enum {VAZIO = '.', BRANCA = '*', PRETA = '#',JOG1 = '1',JOG2 = '2'} CASA;

/**
\brief Tipos de dados para a determinação da conclusão atual do jogo.
 */
typedef enum {ONGOING,JOG1GANHA,JOG2GANHA,SEMJOGADAS} FIM;

/**
\brief Tipos de dados para as coordenadas de casas no tabuleiro.
 */
typedef struct {
        int coluna;
        int linha;
} COORDENADA;

/**
\brief Tipos de dados para a jogada.
 */
typedef struct {
        COORDENADA jogador1;
        COORDENADA jogador2;
} JOGADA;

/**
\brief Tipo de dados que regista as várias jogadas ao longo do jogo.
 */
typedef JOGADA JOGADAS[32];

/**
\brief Tipos de dados para o estado. Esta é a estrutura principal.
 */
typedef struct {
        CASA tab[8][8];
        COORDENADA ultima_jogada;
        JOGADAS jogadas;
        int num_jogadas;
        int jogador_atual;
} ESTADO;
/**
\brief Esta função inicializa o estado de jogo para o seu estado inicial.
@return o estado inicializado.
 */
ESTADO *inicializar_estado();

/**
\brief Retorna o jogador atual de um estado.
@param *estado Apontador para o estado alvo.
@return O número do jogador atual.
 */
int obter_jogador_atual(ESTADO *estado);

/**
\brief Retorna o número de jogadas de um estado.
@param *estado Apontador para o estado alvo.
@return O número de jogadas.
 */
int obter_numero_de_jogadas(ESTADO *estado);

/**
\brief Retorna o estado da casa apontada pela coordenada.
@param *e Apontador para o estado alvo.
@param c Coordenada da casa.
@return Estado da casa.
 */
CASA obter_estado_casa(ESTADO *e, COORDENADA c);

/**
\brief Obtem a coordenada da última jogada do Estado.
@param e Apontador para o estado alvo.
@return Última jogada.
 */
COORDENADA obter_ultima_jogada(ESTADO *e);

/**
\brief Obtem a coordenada do elemento especificado da lista de jogadas.
@param e Apontador para o estado alvo.
@param alvo Número da jogada a retirar.
@return Coordenada correspondente.
 */
COORDENADA obter_jogada_alvo(ESTADO *e,int alvo);

/**
\brief Alterna o valor do jogador_atual.
@param e Apontador para o estado Alvo.
*/
void altera_jogador_atual(ESTADO *e);

/**
\brief Altera o valor do jogador_atual para um valor específico.
@param e Apontador para o estado alvo.
@param alvo Número do jogador.
*/
void altera_jogador_atual_alvo(ESTADO *e,int alvo);

/**
\brief Altera o valor do número de jogadas para um valor específico.
@param e Apontador para o estado Alvo.
@param alvo Número da jogada.
*/
void altera_numero_de_jogadas(ESTADO *e,int alvo);

/**
\brief Incrementa o número de jogadas.
@param e Apontador para o estado alvo.
*/
int incrementa_numero_de_jogadas(ESTADO *e);

/**
\brief Decrementa o número de jogadas.
@param e Apontador para o estado alvo.
*/
int decrementa_numero_de_jogadas(ESTADO *e);

/**
\brief Altera o estado de uma casa, dado a coordenada e a casa para a qual vai modificar.
@param *e Apontador para o estado alvo.
@param c Coordenada no tabuleiro a ser alterada.
@param casa Casa para a qual a coordenada vai modificar.
*/
void altera_estado_casa(ESTADO *e,COORDENADA c,CASA casa);

/**
\brief Altera o valor do dado ultima jogada, dado a coordenada.
@param *e Apontador para o estado alvo.
@param c Coordenada a ser alterada.
*/
void altera_ultima_jogada(ESTADO *e,COORDENADA c);

/**
\brief Altera a coordenada atual na lista de jogadas.
@param *e Apontador para o estado alvo.
@param alvo Coordenada a ser alterada.
*/
void altera_jogada(ESTADO *e,COORDENADA alvo);

/**
\brief Converte um char específico para um dado CASA.
@param casa Char a ser convertido.
@return Casa convertida.
*/
CASA converte_char_para_casa(char casa);

/**
\brief Converte um par de chars para uma coordenada.
@param lin Char da linha a ser convertido.
@param col Char da coluna a ser convertido.
@return Coordenada convertida.
*/
COORDENADA converte_chars_para_coordenada(char lin,char col);

/**
\brief Converte uma coordenada para dois chars correspondentes.
@param c Coordenada a ser convertida.
@param lin Char da linha convertido.
@param col Char da coluna convertido.
*/
void converte_coordenada_para_char(COORDENADA c,char *lin,char *col);

/**
\brief Atualiza o estado atual com a coordenada Jogada.
@param *e Apontador para o estado a ser alterado.
@param c A coordenada Jogada.
 */
void atualiza_estado(ESTADO *e,COORDENADA c);

/**
\brief Função utilizada no comando 'pos'. Esta retrocede o jogo para uma jogada específica. 
@param *e Apontador para o estado alvo.
@param n Número da jogada a retroceder.
@return Indicador de sucesso (0 = Sucesso).
*/
int pos(ESTADO *e,int n);

/**
\brief Função auxiliar da função 'pos'. Esta recebe o número de jogadas a ser retrocedido.
@param *e Apontador para o estado alvo.
@param n Número de jogadas retrocedidas.
*/
void retrocede_num_passos(ESTADO *e,int n);

/**
\brief Função utilizada com o comando 'jog'. Esta serve para criar um estado temporário (a ser modificado) igual ao estado alvo.
@param *e Apontador para o estado alvo.
@return Estado temporário.
*/
ESTADO *estado_temporario(ESTADO *e);

#endif
