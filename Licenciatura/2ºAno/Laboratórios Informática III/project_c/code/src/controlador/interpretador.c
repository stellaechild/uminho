/**
 * Data de criação : 15/04/2021
 * Maria Cunha - A93264
 * Vicente Moreira - A93296
 * Joana Alves - A93290
 * Versão:20210505
 *
 * Ficheiro responsável pelo interpretador, lê o comando que recebe do cliente e 
 * executa a query/o comando associados a tal.
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <ctype.h>
#include "../../includes/interpretador.h"

#define MAX_SIZE 256

Variables variaveis;
SGR sgr;

char user_path[30] = "input_files/users_full.csv";
char bus_path[30] = "input_files/business_full.csv";
char rev_path[30] = "input_files/reviews_1M.csv";


struct command{ //Estrutura privada, apenas utilizada pelo interpretador.
	int valido;
	int comando; 
	char* variavel;
	StringL argumentos; 
};

void free_Command(Command target){
	free_StringL(target->argumentos);
	if(target->variavel != NULL) free(target->variavel);
	free(target);
}

//-----------------------------------------------COMANDOS-------------------------------------------------------


void toCSV(char* x,char* delim,char* filepath){ 

	TABLE target = lookup_variable(variaveis,x);        //Depende de como o "variables" é defenido no interpretador
	if(target==NULL) {
		showMessage("A variável não existe!");
		return;
	}

	FILE *fp = fopen(filepath,"w+");

	if (fp == NULL) {
		showMessage("Erro na abertura do ficheiro");
		return;
	}
	
	fseek(fp, 0,SEEK_END); 

	int lin = table_getInfoLine(target);
	if (!lin) lin = table_getNumLinhas(target); 

    for(int i=0;i<lin;i++){
		char* line = table_get_line_mounted(target,delim,i); 
		fwrite(line,sizeof(char),strlen(line),fp);
		free(line);
    }

	fclose(fp);
}

TABLE fromCSV(char* filepath,char delim) {

    FILE *fp = fopen(filepath,"r");

	if (fp == NULL) {
		showMessage("Erro na abertura do ficheiro");
		return NULL;
	}
	TABLE res = new_empty_table(0);
	int done= 0;
	char* linha;
	linha = fgetsDinamico(fp, &done); 
	while (!done){
		int len = strlen(linha);
		for(int aux = 0;aux < len;aux++){
			if(linha[aux] == delim) linha[aux] = ';';
		}
		linha[len-1] = '\0';
		table_append_line(res,linha);
		linha = fgetsDinamico(fp,&done);
	}
	fclose(fp);

	return res;
}

TABLE get_element(char* var,int x,int y){
	TABLE copy = lookup_variable(variaveis,var);
	char* elem =table_get_element(copy,x,y);
	TABLE res = new_empty_table(0);
	if(elem) table_append_line(res,elem);
	else table_append_line(res,strdup("Posição inválida!"));
	return res;
}

TABLE filter(char* var,char* colName,char* value,OPERATOR op){
	TABLE copy = lookup_variable(variaveis,var);
	int len = table_getInfoLine(copy);
	if(!len) len = table_getNumLinhas(copy);
	TABLE res = new_empty_sizedTable(len,table_topline(copy));
	int done = 0,stays = 0,x,col = -1;
	for(int aux = 0;aux < table_getNumColuna(copy,0) && !done;aux++){
		char *test = table_get_element(copy,0,aux);
		if(strcasecmp(colName,test) == 0) {
			col = aux;
			done=1;
		}
		free(test);
	}
	char* line = table_get_line_mounted(copy,";",0);
	line[strlen(line)-1] = '\0';
	table_append_line(res,line);
	for(x = 1;x < len;x++){
		stays = 0;
		char *full = table_get_line_mounted(copy,";",x);
		char *hold = full;
		char *cut = malloc(sizeof(char) * (strlen(full)+24));
		cut[0] = '\0';
		for(int aux = 0;full != NULL;aux++){
			char* elem = strdup(strsep(&full,";"));
			if(col == aux){
				int cmp = strcmp(elem,value);
				if(op == EQ && cmp == 0) stays=1;
				else if (op == LT && cmp < 0) stays = 1;
				else if (op == GT && cmp > 0) stays = 1;
			}
			strcat(cut,elem);
			strcat(cut,";");
			free(elem);
		}
		if(stays) {
			cut[strlen(cut)-2] = '\0';
			table_append_line(res,cut);
		}
		else free(cut);
		free(hold);
	}
	if (table_getInfoLine(copy)) {
		table_setInfoLine_now(res);
		len = table_getNumLinhas(copy);
		for(;x < len;x++){
			char* elem = table_get_element(copy,x,0);
			table_append_line(res,elem);
		}
	}
	return res;
}

TABLE proj(char* var,int col){
	TABLE copy = lookup_variable(variaveis,var);
	int len,x;
	if(!table_getInfoLine(copy)) len = table_getNumLinhas(copy);
	else len = table_getInfoLine(copy);
	TABLE res = new_empty_sizedTable(len,table_topline(copy));
	for(x = 0;x < len;x++){
		char *full = table_get_line_mounted(copy,";",x);
		char *hold = full;
		full[strlen(full)-1]= '\0';
		char *cut = malloc(sizeof(char) * strlen(full));
		cut[0] = '\0';
		for(int aux = 0;full != NULL;aux++){
			char* elem = strdup(strsep(&full,";"));
			if (aux == col) {
				strcat(cut,elem);
			}
			free(elem);
		}
		table_append_line(res,cut);
		free(hold);
	}
	table_setInfoLine_pos(res,table_getInfoLine(copy));
	len = table_getNumLinhas(copy);
	for(;x < len;x++){
		char* elem = table_get_element(copy,x,0);
		table_append_line(res,elem);
	}
	return res;
}

void checkSGR () {
	if (sgr_user_valido(sgr)) showMessage("InfoUser: válido");
	else showMessage("InfoUser: inválido");

	if (sgr_bus_valido(sgr)) showMessage("InfoBusiness: válido");
	else showMessage("InfoBusiness: inválido");

	if (sgr_rev_valido(sgr)) showMessage("InfoReview: válido");
	else showMessage("InfoReview: inválido");

	if (sgr_stats_valido(sgr)) showMessage("Stats: válido");
	else showMessage("Stats: inválido");
} 

void checkStats(){
	if (sgr_stats_valido(sgr)) {
		char tmp[128];
		sprintf(tmp,"Número de Users    carregados: %d\nNúmero de Negócios carregados: %d\nNúmero de Reviews  carregados: %d",sgr_stats_numUsr(sgr),sgr_stats_numBiz(sgr),sgr_stats_numRev(sgr));
		showMessage(tmp);
	}
	else showMessage("Stats não foram carregadas. Verifique o SGR com 'sgrcheck'");
}

void checkVars(){
	showMessage("Variáveis carregadas:");
	GList* l = get_variables(variaveis);
	GList* freable = l;
	for (; l != NULL; l = l->next){
		char* holder = strdup((char*) l->data);
		showMessage(holder);
		free(holder);
  	}
	g_list_free(freable);
}

void removeVar(char* var){
	remove_variable(variaveis,var);
	showMessage("Variável removida com sucesso!");
}

void removeVarsAll(){
	free_all_content(variaveis);
	showMessage("Todas as variáveis removidas!");
}

void show(char* var){
	TABLE t = lookup_variable(variaveis, var);
	int sim = 0;
	putchar('\n');
	if(table_getInfoLine(t)){
		print_table_info(t);
		printf("\nDeseja ver a tabela completa? (Y/N) ");
		char c = getchar();
		if (c == 'y' || c=='Y') sim = 1;
		putchar('\n');
	}
	if (!table_getInfoLine(t) || sim) print_table (t);
	table_free(t);
	free (var);
}

//--------------------------------------AUXILIARES------------------------------------------------------------
char* novodeblank(char* input) {
    int l = strlen(input)-1;
	if (l >= 0){
		int i=0;
		int j=0;

		while (isspace(input[l])) input[l--] = '\0';
		while (isspace(input[i])) i++;
		for (; input[i]!='\0'; j++,i++) input[j] = input[i];

		input[j] = '\0';
	}
	return input;
}

char* debrack(char* input) {
	int tam = strlen(input);
	if ((input[0] == 39 || input[0] == 34) && (input[tam-1] == 39 || input[tam-1] == 34)) {
		input[0] = ' ';
		input[tam-1] = ' ';
	}
	return input;
}

char* deblank(char* input) {
    char *output = input;
    int j=0;
    for (int i = 0; i<(int)strlen(input); i++,j++) {
        if (input[i]!=' ') output[j]=input[i];
        else j--;
    }
    output[j] = '\0';
    return output;
}
//------------------------------------------FLUXO E EXECUÇÃO-----------------------------------------------------

Command interpreta_linha(char* arg){ 
	char* linha = arg;                             //para o free purpose
	Command res = malloc (sizeof(struct command));
	int moreNeeded = 0;
	res->valido = 1;
	res->variavel = NULL;

	if (strstr(linha,"=")){                        //É UMA ATRIBUICAO 
		char* var = strdup(strsep(&linha,"="));
		var = deblank(var);
		res->variavel = strdup(var);               //Guarda nome da variável
		moreNeeded = 1;                            //Precisa mais um arg
		free(var);
	}
	if (linha){                                   //COMANDO 
		char *com = strdup(strsep(&linha,"(["));   //Separa o comando
		com = deblank(com);
		res->comando = interpreta_funcao (com);   //Atribui código do comando
		moreNeeded = quantos_args (res->comando); //Calcula quantos args precisa
		res->argumentos = new_StringL();
		if(res->comando == 17) StringL_append_line(res->argumentos,com); // Se é o comando de leitura, guarda o "comando" como argumento
		else free(com);       
		while (linha) {
			char* arg = strdup(strsep(&linha,",)]"));      //Separa argumento
			arg = novodeblank(arg);
			if (strlen(arg) > 0 && arg[0] != '\n'){       //Verifica se não é vazio
				StringL_append_line(res->argumentos,arg); //Adiciona
				moreNeeded--;
			}
			else free(arg);
		}
 	}

	if(moreNeeded) res->valido = 0;	                          //VERIFICA FALTA DE ARGUMENTOS
	if(res->valido && res->comando > 1 && res->comando < 10){ //VERIFICA O PARAMETRO "sgr" NAS QUERIES
		char* tmparg = get_StringL_line(res->argumentos,0);
		if(strcmp(tmparg,"sgr") != 0) res->valido = 0;
		free(tmparg);
	}
	if(res->valido) res->valido = verifica_args(res->comando,res->argumentos);
	free(arg);
	return res;
}

//--------------------AUXILIARES DE INTERPRETA LINHA--------------

int interpreta_funcao (char* com) { //VARIOS CODIGOS
	int r = -1;

	if(strcmp(com,"quit") == 0 || strcmp(com,"q") == 0) r=0;
	else if(strcmp(com,"help") == 0) r=20;
	
	else if(strcmp(com,"load_sgr") == 0) r=1; 
	else if(strcmp(com,"businesses_started_by_letter") == 0) r=2;
	else if(strcmp(com,"business_info") == 0) r=3;
	else if(strcmp(com,"businesses_reviewed") == 0) r=4;
	else if(strcmp(com,"businesses_with_stars_and_city") == 0) r=5;
	else if(strcmp(com,"top_businesses_by_city") == 0) r=6;
	else if(strcmp(com,"international_users") == 0) r=7;
	else if(strcmp(com,"top_businesses_with_category") == 0) r=8;
	else if(strcmp(com,"reviews_with_word") == 0) r=9;

	else if(strcmp(com,"toCSV") == 0) r=10; 
	else if(strcmp(com,"fromCSV") == 0) r=11; 
	else if(strcmp(com,"proj") == 0) r=15; 
	else if(strcmp(com,"filter") == 0) r=16;
	
	else if(strcmp(com, "checksgr") == 0) r=21;
	else if(strcmp(com, "checkstats") == 0) r=22;
	else if(strcmp(com, "checkvars") == 0) r=23;
	else if(strcmp(com, "clear") == 0) r=24;
	else if(strcmp(com, "rm") == 0 || strcmp(com, "remove") == 0) r=25;
	else if(strcmp(com, "removeALL") == 0) r=26;
	
	else if(strcmp(com,"show") == 0) r=12; 
	else if(strcmp(com,"queries") == 0) r=13; 


	else if(contains_variable(variaveis,com)) r=17; //COMANDO DE x[1][0]

	return r;
}

int quantos_args (int num) { //VÁRIOS NUMEROS DE ARGUMENTOS DE CADA COMANDO
	int r=0;

	if (num == -1) r = -1;
	else if (num==7 || num==12|| num==25) r=1;
	else if (num==2 || num==3 || num==4 || num==6 || num==11 || num==15 || num==17) r=2;
	else if (num==1 || num==5 || num==8 || num==9 || num==10) r=3;
	else if (num==16) r=4;
		
	return r;
}

int verifica_args(int comando,StringL args){
	if(comando == 2){
		char *holder = get_StringL_line(args,1);
		if(strlen(holder) == 1 || strlen(holder) == 3){
			free(holder);
			return 1;
		}
		else {
			free(holder);
			return 0;
		}
	}
	else if (comando ==  15 || comando == 16 || comando == 12 || comando == 17 || 
	          comando == 10 || comando == 25){                      //Verifica se a variavel no arg existe
		char* var = get_StringL_line(args,0);
		if (!contains_variable(variaveis,var)){
			char tmp[256];
			sprintf(tmp,"Váriavel '%s' não existe!",var);
			showMessage(tmp);
			free(var);
			return 0;
		}
		else{
		free(var);
		return 1;
		}	
	}
	
	else return 1;
}

//----------------------------FIM AUXILIARES-------------------------


TABLE executa_query (Command com) {  //PREPARA ARGS E EXECUTA QUERY
	TABLE t = NULL;

	if (com->comando == 2) {
		char* s = get_StringL_line (com->argumentos, 1);
		s = deblank(s);
		char c;
		if ((strlen(s) == 3) && (s[0] == 39 || s[0] == 34) && (s[2] == 39 || s[2] == 34)) c = s[1];
		else if (strlen(s) == 1) c = s[0];
		else c = ' ';
		showQuery (2, 0, 0, c, NULL);
		free(s);
		return businesses_started_by_letter(sgr, c);
	}

	else if (com->comando == 3) {
		char* busID = get_StringL_line (com->argumentos, 1);
		busID = deblank(busID);
		busID = debrack(busID);
		busID = deblank(busID);
		showQuery (3, 0, 0, ' ', busID);
		return business_info (sgr, busID);
	}

	else if (com->comando == 4) {
		char* userID = get_StringL_line (com->argumentos, 1);
		userID = deblank(userID);
		userID = debrack(userID);
		userID = deblank(userID);
		showQuery (4, 0, 0, ' ', userID);
		return businesses_reviewed (sgr, userID);
	}

	else if (com->comando == 5) {
		char* star = deblank(get_StringL_line (com->argumentos, 1));
		float stars = strtof (star, NULL);
		free(star);
		char* city = get_StringL_line (com->argumentos, 2);
		city = debrack(city);
		city = novodeblank(city);
		showQuery (5, 0, stars, ' ', city);
		return businesses_with_stars_and_city (sgr, stars, city);
	}

	else if (com->comando == 6) {
		char* number = get_StringL_line (com->argumentos, 1);
		int top = atoi (deblank(deblank(number)));
		showQuery (6, top, 0, ' ', NULL); free(number);
		return top_businesses_by_city (sgr, top);
	}

	else if (com->comando == 7) {
		showQuery (7, 0, 0, ' ', NULL);
		return international_users (sgr);
	}

	else if (com->comando == 8) {
		char* number = get_StringL_line (com->argumentos, 1);
		int top = atoi (deblank(number));
		char* cat = get_StringL_line (com->argumentos, 2);
		cat = debrack(cat);
		cat = novodeblank(cat);
		showQuery (8, top, 0, ' ', cat);
		free(number);
		return top_businesses_with_category (sgr, top, cat);
	}
	
	else if (com->comando == 9) {
		char* number = get_StringL_line (com->argumentos, 1);
		int top = atoi (deblank(number));
		char* word = get_StringL_line (com->argumentos, 2);
		word = deblank(word);
		word = debrack(word);
		word = deblank(word);
		showQuery (9, top, 0, ' ', word);
		TABLE res = reviews_with_word(sgr, top, word);
		free(word);free(number);
		return res;
	}

	return t;
}


void executa_funcao (Command com) {    //EXECUTA COMANDO
	
	if (com->comando == 1) { //LOAD
		showMessage("Limpando a estrutura SGR...");
		free_SGR (sgr); 
		char *usr,*biz,*rev;
		int mudado = 0;
		char *lidousr,*lidobiz,*lidorev;

		lidousr = get_StringL_line(com->argumentos,0);
		lidobiz = get_StringL_line(com->argumentos,1);
		lidorev = get_StringL_line(com->argumentos,2);

		lidousr = debrack(lidousr); lidousr = novodeblank(lidousr);
		lidobiz = debrack(lidobiz); lidobiz = novodeblank(lidobiz);
		lidorev = debrack(lidorev); lidorev = novodeblank(lidorev);

		if(strcmp(lidousr,"NULL")==0) usr = strdup(user_path); //Avalia PATH para usr
		else {
			usr = strdup(lidousr);
			mudado = 1;
		}
		if(strcmp(lidobiz,"NULL")==0) biz = strdup(bus_path); //Avalia PATH para biz
		else {
			biz = strdup(lidobiz);
			mudado = 1;
		}
		if(strcmp(lidorev,"NULL")==0) rev = strdup(rev_path); //Avalia PATH para rev
		else {
			rev = strdup(lidorev);
			mudado = 1;
		}
		free(lidousr);free(lidobiz);free(lidorev);

		if (!mudado) showQuery (1,1,0,' ', NULL);
		else showQuery (1,2,0,' ', NULL);

		showLeituraAll(mudado);
			
		sgr = load_sgr (usr, biz, rev);
		if(sgr_user_valido(sgr) && sgr_rev_valido(sgr) && sgr_bus_valido(sgr) && sgr_stats_valido(sgr))
			showMessage("Todos os ficheiros lidos com sucesso!");
		else showMessage("UhOh.. Um ou mais ficheiros não foram carregados.\nVerifique o estado do SGR com 'checksgr' e tente fazer load novamente.");
		free(usr);free(biz);free(rev);
		return;
	}

	else if (com->comando >= 2 && com->comando <= 9) { 
		TABLE t = executa_query(com);
		if(t != NULL){
			if (com->variavel != NULL) add_variable (variaveis, strdup(com->variavel), t);
			else print_table(t);
			table_free(t);
		}
		else showMessage("Ups... Isto não era esperado... Verifique os argumentos");
		return;	
	}


	else if (com->comando == 12) { //SHOW
		char* var = get_StringL_line (com->argumentos, 0);
		var=debrack(var);var=novodeblank(var);
		show(var);
		return;
	}

	else if (com->comando == 13) { //comando "queries"
		showQueries();
		return;
	}

	else if (com->comando == 10) { //toCSV
		char* x = get_StringL_line (com->argumentos, 0);
		char* delim = get_StringL_line (com->argumentos, 1);
		char* path = get_StringL_line (com->argumentos, 2);
		if (strcmp(delim,"NULL") == 0){
			free(delim);
			delim = strdup(";");
		}
		path = debrack(path);
		path = novodeblank(path);
		toCSV (x, delim, path);
		free(x);free(delim);free(path);
		return;
	}

	else if (com->comando == 11) { //fromCSV 
		char* s = get_StringL_line (com->argumentos, 1);
		s = deblank(s);
		char c;
		if(strcmp(s,"NULL") == 0) c = ';';
		else{
			if ((strlen(s) == 3) && (s[0] == 39 || s[0] == 34) && (s[2] == 39 || s[2] == 34)) c = s[1];
			else if (strlen(s) == 1) c = s[0];
			else c = ' ';
		}

		char* path = get_StringL_line (com->argumentos, 0);
		path = debrack(path);
		path = novodeblank(path);

		TABLE res = fromCSV (path, c);
		if(res != NULL) {
			if(com->variavel) add_variable(variaveis,strdup(com->variavel),res);
			else print_table(res);
			table_free(res);
		}
		free(path);free(s);
		return;
	}

	else if (com->comando == 20) showHelp();            //help

	else if (com->comando == 21) checkSGR();            //checksgr

	else if (com->comando == 22) checkStats();          //checkstats

	else if (com->comando == 23) checkVars();           //checkvars

	else if (com->comando == 25) {                      //remove
		char* var =get_StringL_line(com->argumentos,0);
		var = debrack(var);
		var = deblank(var);
		removeVar(var); free(var);
	}           
	else if (com->comando == 26) removeVarsAll();      //removeALL

	else if (com->comando == 24) {
		if(system("clear")){};                          //clear
	} 

	else if (com->comando == 15){                       //proj
		char* var = get_StringL_line(com->argumentos,0);
		var = debrack(var);
		var = novodeblank(var);
		char* num = get_StringL_line(com->argumentos,1);
		int col = atoi(num);
		TABLE new = proj(var,col);
		if (new != NULL){
			if(com->variavel) add_variable(variaveis,strdup(com->variavel),new);
			else print_table(new);
			table_free(new);
		} 
		free(num);free (var);
		return;
	}

	else if (com->comando == 16){                       //filter
		char* var = get_StringL_line(com->argumentos,0);
		char* name = get_StringL_line(com->argumentos,1);
		char* value = get_StringL_line(com->argumentos,2);
		char* op = get_StringL_line(com->argumentos,3);
		OPERATOR opn = 1;
		if (strcmp(op,"LT") == 0) opn = 0;
		else if (strcmp(op,"EQ") == 0) opn = 1;
		else if (strcmp(op,"GT") == 0) opn = 2;
		TABLE new = filter(var,name,value,opn);
		if (new != NULL) {
			if(com->variavel) add_variable(variaveis,strdup(com->variavel),new);
			else print_table(new);
			table_free(new);
		}
		free(name);free(var);free(value);free(op);
		return;
	}

	else if (com->comando == 17){                     //GET_ELEM
		char* var = get_StringL_line(com->argumentos,0);
		char* sx = get_StringL_line(com->argumentos,1);
		char* sy = get_StringL_line(com->argumentos,2);
		sy[0] = ' ';
		sy = deblank(sy);
		int x = atoi(sx);
		int y = atoi(sy);
		TABLE res = get_element(var,x,y);
		if (res != NULL){
			if(com->variavel) add_variable(variaveis,strdup(com->variavel),res);
			else print_table(res);
			table_free(res);
		} 
		free(var);free(sx);free(sy);
		return;
	}
	
}


int run() {
	int quit = 0;
	char* linha = malloc(sizeof(char) * MAX_SIZE);

	variaveis = init_variables();	
	showMessage("Starting UMYELP... Loading default SGR\nIt won't take long :)\n");
	sgr = load_sgr(user_path,bus_path,rev_path);

	
	showBoasVindas();
	showCookies();
	showMessage("Qual o comando que deseja executar? Caso precise de ajuda, escreva help.");
	showSeparador();

	while(!quit) {
		showPrompt();
		linha = fgets(linha,MAX_SIZE,stdin);
		Command novo;
		if (strlen(linha) > MAX_SIZE-2){
			showMessage("Tamanho do comando inválido (Max 256)");
		}
		else{
			char *comando = linha;
			char *tmp;
			while (comando != NULL && (strcmp(comando, "\n")!=0) && !quit) {
				char *sep = strdup (strsep (&comando, ";\n"));
				tmp = strdup (sep);
				novo = interpreta_linha (sep);
				if (!novo->valido) {
					showInvalido (tmp);
					break;
				}
				if (novo->comando == 0) {
					quit = 1;
					free_Command(novo);
					free (tmp);
					break;
				}

				else {
					clock_t begin = clock();
					executa_funcao (novo);
					clock_t end = clock();
					double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
					if (novo->comando != 20 && novo->comando != 24) showExecutado (time_spent);
				}

				
				if (strcmp (comando, "") == 0) comando = NULL;
				free (tmp);
				free_Command(novo);

				showSeparador();
			}
			
		}

		for(int aux = 0;aux < MAX_SIZE;aux++)linha[aux] = '\0';
	}

	showClean();
	free_SGR(sgr);
	free_variables (variaveis);
	free(linha);

	showAllSet();
	showMenuSaida();
	return 0;
}
