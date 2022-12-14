/**
 * Data de criação: 15/04/2021
 * Joana Alves - A93290
 * Versão: 20210417
 *
 * Módulo que trata de apresentar as mensagens ao utilizador do programa.
 */

#include "../../includes/visualizador.h"
#include <math.h>


void showBoasVindas () {	
	printf ("-------------------------------------------------------------------------------------------\n");
	printf ("                                     WELCOME TO UMYELP                                     \n");
	printf ("-------------------------------------------------------------------------------------------\n");
	printf ("\n");
}


void showCookies() {
	printf ("We use cookies to ensure you have the best experience on our program. By using our\n");
	printf ("program you acknowledge that you have read and understood our Cookie Policy! :D\n\n");
}


void showMenuSaida () {
	printf ("-------------------------------------------------------------------------------------------\n");
	printf ("                                      SEE YOU LATER!                                       \n");
	printf ("-------------------------------------------------------------------------------------------\n");
}

void showSeparador () {
	printf ("\n-------------------------------------------------------------------------------------------\n");
}

void showPrompt () {
	printf ("\n$ ");
}

void showMessage (char* mensagem) {
	printf ("%s\n", mensagem);
}


void showQueries() {
	printf ("\nQueries disponíveis: \n");
	printf ("1 (load_sgr)                       -> carregar dados para usuários/negócios/reviews.\n");
	printf ("2 (businesses_started_by_letter)   -> lista e número total de negócios cujo nome\n");
	printf ("                                      começa por X letra.\n");
	printf ("3 (business_info)                  -> dado um ID de negócio, determinar a sua \n");
	printf ("                                      informação.\n");
	printf ("4 (businesses_reviewed)            -> dado um ID de usuário, determinar a lista\n");
	printf ("                                      de negócios aos quais fez review.\n");
	printf ("5 (businesses_with_stars_and_city) -> dado um número(n) e uma cidade, determinar a lista\n");
	printf ("                                      de negócios com n ou mais estrelas na dada cidade.\n");
	printf ("6 (top_businesses_by_city)         -> dado um numero inteiro(n), determinar a lista \n");
	printf ("                                      dos top n negócios em cada cidade\n");
	printf ("7 (international_users)            -> determinar a lista e o número total de IDs de\n");
	printf ("                                      usuários que tenham visitado mais de um estado.\n");
	printf ("8 (top_businesses_with_category)   -> dado um número inteiro(n) e uma categoria, determinar\n");
	printf ("                                      a lista dos top n negócios que pertencem à dada categoria.\n");
	printf ("9 (reviews_with_word)              -> dada uma palavra, determinar a lista de IDs de reviews\n");
	printf ("                                      que a referem no campo text.\n");
}


void showQuery (int a, int c, float b, char s, char* string) {
	switch (a) {
		case (1) : 
			printf ("\nQuery 1 - carregar dados para usuários/negócios/reviews.\n");
			printf ("Opções:\n1 - default path;\n2 - atribuir um ou mais paths;\nOpção escolhida: %d\n\n", c);
			break;
		case (2):
			printf ("\nQuery 2 - lista e número total de negócios cujo nome começa por X letra.\n");
			printf ("Procurar letra: %c\n\n", s);
			break;
		case (3):
			printf ("\nQuery 3 - dado um ID de negócio, determinar a sua informação.\n");
			printf ("Procurar ID: %s\n\n", string);
			break;
		case (4):
			printf ("\nQuery 4 - dado um ID de usuário, determinar a lista de negócios aos quais fez \n");
			printf ("review. Procurar ID: %s\n\n", string);
			break;
		case (5):
			printf ("\nQuery 5 - dado um número(n) e uma cidade, determinar a lista de negócios com n \n");
			printf ("ou mais estrelas na dada cidade.\n");
			printf ("Dados a procurar: %.1f | %s\n\n", b, string);
			break;
		case (6):
			printf ("\nQuery 6 - dado um numero inteiro, determinar a lista dos top n negócios \n");
			printf ("em cada cidade. Tamanho dos tops: %d\n\n", c);
			
			break;
		case (7):
			printf ("\nQuery 7 - determinar a lista e o número total de IDs de usuários que tenham\n");
			printf ("visitado mais de um estado.\n\n");
			break;
		case (8):
			printf ("\nQuery 8 - dado um número inteiro e uma categoria, determinar a lista dos top n \n");
			printf ("negócios que pertencem à dada categoria.\n");
			printf ("Dados a procurar: %d | %s\n\n", c, string);
			break;
		case (9):
			printf ("\nQuery 9 - dada uma palavra, determinar a lista de IDs de reviews que a referem\n");
			printf ("no campo text. Procurar palavra: %s\n\n", string);
			break;
		default : printf ("\nQuery inválida!\n"); 
	}	
}


void showHelp () {
	printf ("\nPara executar múltiplos comandos em cadeia use ';'\n");

	printf ("\nQuery 1 (load_sgr) - por favor coloque NULL nos parâmetros onde deseja que sejam\n");
	printf ("carregados os ficheiros default. Por exemplo, se apenas quiser carregar o ficheiro\n");
	printf ("o ficheiro users a partir de um path diferente, o comando será:\n");
	printf ("$ load_sgr(path, NULL, NULL).\n");

	printf ("\nto/fromCSV - se entender utlizar o caracter ';' como delimitador, por favor\n");
	printf ("coloque NULL no seu lugar. Exemplo: $ fromCSV(path, NULL)\n");

	printf ("\nComandos disponíveis no programa:\n");
	printf ("z = queryX      -> atribuir o resultado da query número X à variável z.\n");
	printf ("show (variável) -> visualizar variável requerida.\n");
	printf ("queries         -> visualizar lista de queries disponíveis.\n");
	printf ("toCSV(v,d,p)    -> Imprime uma TABLE(v) para um ficheiro(p) dado um delimitador(d).\n");
	printf ("fromCSV(p,d)    -> Lê um ficheiro(p) e guarda/imprime a TABLE resultante.\n");
	printf ("proj(var,col)   -> guarda/imprime a coluna 'col' da TABLE da variável 'var'.\n");
	printf ("filter          -> imprime TABLE com informação filtrada de acordo com os parâmetros.\n");
	printf ("var[x][y]       -> aceder aos dados na posição (x,y) da TABLE da variável var.\n");
	printf ("check sgr       -> mostra o estado atual do SGR carregado.\n");
	printf ("check stats     -> mostra o estado das Stats do SGR.\n");
	printf ("check vars      -> mostra as variáveis do programa.\n");
	printf ("rm/remove (var) -> elimina a variável 'var' se esta existir no programa.\n");
	printf ("removeALL       -> elimina todas as variáveis do programa.\n");
	printf ("clear           -> limpa a janela da terminal.\n");
	printf ("quit / q        -> sair do programa :(\n");
}


void showInvalido (char* comando) {
	printf("Comando [%s] inválido!\n", comando);
}

void showExecutado (double time) {
	int unit = 0;
	while ((int) floor(time) == 0 && unit < 4) {
		time *= 1000;
		unit++;
	}
	printf ("\nComando executado com sucesso! (%.3f ",time);
	if (unit == 0) printf("segundos)\n");
	else if (unit == 1) printf("milissegundos)\n");
	else if (unit == 2) printf("microsegundos)\n");
	else if (unit == 3) printf("nanosegundos)\n");
	else printf("picosegundos)\n");
}

void showErro() { printf ("ERROR! Something went wrong...\n");}


void showLeituraAll(int def) {
	printf ("Load de negócios, utilizadores e reviews inicializados!\n");
	if (def) printf ("Tempo estimado: 15s.\n");
	printf ("Por favor espere...\n");
}

void showClean () {	printf ("\nCleaning the house...\n");}

void showAllSet () { printf ("All clean!\n\n");}
