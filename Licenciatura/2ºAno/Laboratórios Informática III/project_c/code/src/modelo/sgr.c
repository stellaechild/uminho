/**
 * Data de criação: 08/04/2021
 * Autores: 
 * Vicente Moreira - A93296 
 * Joana Alves - A93290
 * Versão: 20210417
 * 
 * Módulo do Sistema de Gestão de Recomendações
 * Ficheiro responsável pelas queries.
 */

#include <stdio.h>
#include <ctype.h>
#include "../../includes/sgr.h"

struct sgr {
	int validUsr;
	int validBiz;
	int validRev;
	int validStats;

	UserInfo usr;
	BusinessInfo biz;
	ReviewInfo rev;
	
	Stats stats;
};

typedef struct sgraux{
	char* busid;
	char* name;
	float values;
} *SgrAux;

static SgrAux new_sgrAux(){
	SgrAux new = malloc(sizeof(struct sgraux));
	return new;
}

static gint sgrauxcmp(gconstpointer a,gconstpointer b,gpointer data){
	(void) data;
	SgrAux temp1 = (SgrAux) a; SgrAux temp2 = (SgrAux) b;
	return (gint) ((temp2->values*100) - (temp1->values*100));
}

static void free_sgrAux(SgrAux t){
	if(t->busid != NULL) free(t->busid);
	if(t->name != NULL) free(t->name);
	free(t);
}

static float calculaStars (SGR sgr, DadToSons reviews) {
	float soma = 0;
	float total;
	int tam = get_DadToSons_numChilds (reviews);

	for (int i=0; i<tam; i++) {
		char* reviewID = get_DadToSons_childID (reviews, i);
		StrReview rev = findReview (reviewID, sgr->rev);
		soma += get_reviewStars(rev);
		freeReview (rev);
		free (reviewID);
	}
	total = soma / (float) tam;
	return total;
}  

static void index_add_all_usrToRev (gpointer key,gpointer value,gpointer user_data){
	(void) key;
	StrReview rev = (StrReview) value;
	Stats stats = (Stats) user_data;
	stats_index_addEntry(get_Index_pointer(stats,0),get_reviewUserID(value),get_reviewID(value));
}

static void index_add_all_bizToRev (gpointer key, gpointer value, gpointer data) {
	(void) key;
	StrReview rev = (StrReview) value;
	Stats stats = (Stats) data;
	stats_index_addEntry(get_Index_pointer(stats,1),get_reviewBusinessID(value),get_reviewID(value));
}

SGR init_sgr () {
    SGR new = malloc (sizeof (struct sgr));
	new->validUsr = 0;
	new->validBiz = 0;
	new->validRev = 0;
	new->validStats = 0;
    return new;
}

int sgr_user_valido (SGR sgr) {
	return sgr->validUsr;
}
int sgr_bus_valido (SGR sgr) {
	return sgr->validBiz;
}
int sgr_rev_valido (SGR sgr) {
	return sgr->validRev;
}
int sgr_stats_valido (SGR sgr) {
	return sgr->validStats;
}
int sgr_stats_numUsr (SGR sgr) {
	return get_statsnumUsr(sgr->stats);
}
int sgr_stats_numBiz (SGR sgr) {
	return get_statsnumBiz(sgr->stats);
}
int sgr_stats_numRev (SGR sgr) {
	return get_statsnumRev(sgr->stats);
}

void free_SGR (SGR sgr) {
	if(sgr->validUsr) freeUserInfo (sgr->usr);
	if(sgr->validBiz) freeBusinessInfo (sgr->biz);
	if(sgr->validRev) freeReviewInfo (sgr->rev);
	free_Stats(sgr->stats);
	free (sgr);
}

/* query 1 */
SGR load_sgr(char*users,char*businesses,char*reviews) {
	int numUsr,numBiz,numRev;
	SGR loaded = init_sgr();
	loaded->stats = init_Stats();
	
	if(!loaded->validUsr){                                                       //LOAD USER
		loaded->usr = load_UserInfo(users,&numUsr,0);
		if (numUsr != -1) {
			loaded->validUsr = 1;
			set_statsnumUsr(loaded->stats,numUsr);
		} 
	}
	
	if(!loaded->validBiz){                                                       //LOAD BUSINESS
		loaded->biz = load_BusinessInfo(businesses,&numBiz);
		if (numBiz != -1) {
			loaded->validBiz = 1;
			set_statsnumBiz(loaded->stats,numBiz);
		}
	}
	
	if(!loaded->validRev && loaded->validUsr && loaded->validBiz){               //LOAD REVIEWS
		loaded->rev = load_ReviewInfo(reviews,&numRev,loaded->usr,loaded->biz);
		if (numRev != -1){
			loaded->validRev = 1;
			set_statsnumRev(loaded->stats,numRev);
		} 
	}
	
	if(loaded->validRev){                                                        //LOAD INDEXs 
		infoReview_foreach(loaded->rev,(GHFunc) index_add_all_usrToRev,loaded->stats);
		infoReview_foreach(loaded->rev,(GHFunc) index_add_all_bizToRev,loaded->stats);
	}
	
	if(loaded->validUsr && loaded->validBiz && loaded->validRev)loaded->validStats = 1;
	
	return loaded;
}


/* query 2 */ 
void ajudaQuery2 (char* key, StrBusiness value, QueryArg data) { //Função para o "for_each"
	(void) key;
	char* nome = get_businessName (value);

	int letra, other = 65;
	letra = get_query_letra(data);

	if (letra >= 97 && letra <= 122) other = letra-32;                        //CASE INSENSITIVE
	else if (letra >= 65 && letra <= 90) other = letra+32;
	else other=letra;

	if (nome[0] == letra || nome[0] == other) {                               //SE LETRA MATCHES
		table_append_line (get_query_table(data) ,get_businessName(value));   //ADD
		inc_query_int (data);
	}

	free (nome);
}

TABLE businesses_started_by_letter (SGR sgr,char letter) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr_bus_valido(sgr))return get_table_invalidSgr(res);                   //CHECK INICIAL

	char *topline = strdup("Business Name");
	table_append_line(res,topline);

	QueryArg data = init_queryarg();
	set_query_int (data, 0);
	set_query_letra (data, letter);
	set_query_table (data, res); 

	infoBusiness_foreach (sgr->biz, (GHFunc) ajudaQuery2, (gpointer) data);     //FOREACH Business "AJUDAQUERY2"
	TABLE clone = table_clone(get_query_table(data));
	
	char * string = malloc (sizeof (char) * 80);
	sprintf (string, "Negócios começados com o caracter '%c': %d",get_query_letra(data),get_query_int(data));
	free_queryarg(data);

	table_setInfoLine_now(clone);
	table_append_line (clone,string);

	return clone;
}


/* query 3 */ 
TABLE business_info (SGR sgr,char* business_id) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr_bus_valido(sgr)) return get_table_invalidSgr(res);                               //CHECK INICIAL

	
	StrBusiness bus = findBusiness (business_id, sgr->biz);
	if(bus == NULL){
		table_append_line(res,strdup("O negócio não existe"));
		free(business_id);
		return res;
	}
	char *topline = strdup("Business Name;City;State;Stars;Nº reviews");
	table_append_line(res,topline);

	DadToSons revs = findDadToSonsFromFather (get_Index_pointer(sgr->stats,1), strdup(business_id));  //INDEX BIZ->REVS
	float stars = calculaStars (sgr, revs);                                                           //CALCULA A MÉDIA

	char* nstars = malloc(sizeof(char) * 10);              //FLOAT -> STRING
    sprintf(nstars,"%.1f", stars);

	char* nrevs = malloc (sizeof (char) * 6); 
	int numrevs = get_DadToSons_numChilds(revs);
	sprintf (nrevs, "%d", numrevs);

	int arr[6] = {0,1,1,1,0,0};
	char* business = businessToString (bus,arr);

	int tamtotal = strlen (business) + 20;                 //Monta a line
	char* total = malloc (sizeof (char) * tamtotal);
	strcpy (total, business);strcat (total, ";");
	strcat (total, nstars);strcat (total, ";");
	strcat (total, nrevs);
	
	table_append_line (res, strdup (total));
	
	free_DadToSons (revs);freeBusiness (bus);
	free (business);free (nstars);free (nrevs);free (total);free(business_id);

	return res;
}


/* query 4 */ 
TABLE businesses_reviewed (SGR sgr, char* user_id) {
	TABLE res = new_empty_table(1);

	if(!sgr->validStats || !sgr->validBiz) return get_table_invalidSgr(res);              //CHECK INICIAL

	DadToSons revs = findDadToSonsFromFather (get_Index_pointer(sgr->stats,0), user_id); //INDEX BIZ -> REVS

	if (revs != NULL) {

		char *topline = strdup("Business ID;Business Name");
		table_append_line(res,topline);

		int numRevs = get_DadToSons_numChilds(revs); 

		StrReview review;
		StrBusiness biz;
		char*reviewID;
		char *string;
		char *businessID;
		int num=0;

		for (int i=0; i<numRevs; i++) {
			reviewID = get_DadToSons_childID (revs, i);  //Vê o ID da review
			review = findReview (reviewID, sgr->rev);    //Encontra a review
			businessID = get_reviewBusinessID(review);   //Encontra o ID do negócio associado
			biz = findBusiness(businessID,sgr->biz);     //Encontra o negócio 
			int arr[6] = {1,1,0,0,0,0};
			string = businessToString(biz,arr);          //Monta a line
			table_append_line (res, string);
			num++;

			freeBusiness(biz);freeReview (review);
			free (businessID);free (reviewID);
		}
		table_setInfoLine_now(res);                     //ESCREVE INFO LINE
		free_DadToSons(revs);

		if (num == 0) table_append_line (res, strdup("O utilizador não fez review a nenhum negócio!"));
		else { 
			char* line = malloc(sizeof(char)*70);
			sprintf(line,"Foram encontrados %d reviews com a autoria do user!", num);
			table_append_line(res, line);
		}
		return res;	
	}
	else table_append_line (res, strdup ("User não encontrado!"));


	return res;
}

/* query 5 */ 
TABLE businesses_with_stars_and_city (SGR sgr,float stars,char* city) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr->validBiz || !sgr->validStats) return get_table_invalidSgr(res); //CHECK INICIAL

	Index bus = get_Index_pointer(sgr->stats, 1);

	char *topline = strdup("Business ID;Business Name");
	table_append_line(res,topline);

	int numbus = get_statsIndexLength(bus);                     //Num negócios no INDEX biz -> rev

	char *string;
	int num=0;	

	for (int i = 0; i < numbus; i++) {
		int arr[6] = {1,1,0,0,0,0};
		DadToSons revs = get_statsIndexEntry (bus,i);           //Vê a listagem de reviews de um negócio
		char* busID = get_DadToSons_fatherID (revs);            //Vê o ID do negócio
		StrBusiness business = findBusiness (busID,sgr->biz);   //Encontra o negócio               
		char* c = get_businessCity (business);
			
		if (strcasecmp (c, city) == 0) {                        //Verifica a cidade e as stars
			float star = calculaStars (sgr, revs);              //Calcula a sua média
			if(star >= stars){                                  //Verifica se "passa"
			string = businessToString (business, arr);
			table_append_line (res, string);                    //Adiciona
			num++;
			}
		}
		free (c);free_DadToSons(revs);freeBusiness (business);free (busID);	
	}	
	table_setInfoLine_now(res);                       //ESCREVE INFOLINE

	if (num == 0) table_append_line (res, strdup("Não foram encontrados negócios que correspondessem aos requisitos!"));
	else { 
		char* line = malloc(sizeof(char)*64);
		sprintf(line,"Foram encontrados %d negócios com essa descrição!", num);
		table_append_line(res, line);
	}

	free(city);
	return res;
}

/* query 6 */
void ajudaQuery6 (char* key, GSequence* value, QueryArg data) {                                       //As Gsequence contêm os negocios organizados por
	char* city = strdup(key);                                                                         //ordem decrescente de stars.
	int top = get_query_int(data);
	GSequenceIter* iter = g_sequence_get_begin_iter(value);
	for (int aux = 0;aux < top && !g_sequence_iter_is_end(iter);aux++){                                //Percorre até ao fim ou até "top"
		SgrAux escreve = g_sequence_get(iter);
		char* line = malloc(sizeof(char)*164);
		if(aux == 0) sprintf(line,"%s;%s;%s;%.1f",city,escreve->busid,escreve->name,escreve->values);   //Monta a primeira Line (Com a cidade)
		else sprintf(line,";%s;%s;%.1f",escreve->busid,escreve->name,escreve->values);                  //Monta as próximas Lines 
		table_append_line(get_query_table(data),line);                                                  //Adiciona
		iter = g_sequence_iter_next(iter);
	}
	free(city);
}

TABLE top_businesses_by_city (SGR sgr,int top) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr_bus_valido(sgr) && !sgr_stats_valido(sgr))return get_table_invalidSgr(res);    //CHECK INICIAL

	GHashTable* cities = g_hash_table_new_full (g_str_hash,g_str_equal,                    //HashTable de (key City,value GSequence)
                                                free,(GDestroyNotify) g_sequence_free);

    Index bus = get_Index_pointer(sgr->stats, 1);
	int numbus = get_statsIndexLength(bus);  
	int seen[numbus];
	for(int aux = 0;aux < numbus;aux++) seen[aux] = 0;                                     //Array "seen" para evitar rever negócios já adicionados 
	
	for (int i = 0; i < numbus; i++) {                                                     //Percorre o INDEX BUS -> REV
		DadToSons revs = get_statsIndexEntry(bus,i);
		char* bizID = get_DadToSons_fatherID(revs);
		StrBusiness biz = findBusiness(bizID,sgr->biz);
		char* city = get_businessCity(biz);
		int exists = g_hash_table_contains(cities,city);                                   //Verifica se a cidade já foi adicionada na HashTable
		if(!exists) {                                                                      //Se não existir, vai colher todos os negócios dessa cidade.
			GSequence* leader = g_sequence_new((GDestroyNotify) free_sgrAux);
			for(int j = i;j<numbus;j++){
				if(!seen[j]){                                                              //Evita negócios já vistos
					DadToSons act = get_statsIndexEntry(bus,j);
					char* father = get_DadToSons_fatherID(act);
					StrBusiness bizin = findBusiness(father,sgr->biz);
					char* biztest = get_businessCity(bizin);
					int found= 0;
					if(strcasecmp(biztest,city) == 0){                                     //Compara a cidade de ambos os negócios
						SgrAux new = new_sgrAux();
						new->values = calculaStars(sgr,act);
						new->busid = get_businessID(bizin);
						new->name = get_businessName(bizin);
						g_sequence_append(leader,(gpointer) new);                          //Adiciona à Gsequence
						seen[j] = 1;
					}
					free_DadToSons(act);freeBusiness(bizin);
					free(father);free(biztest);
				}
				
			}
			g_sequence_sort(leader,sgrauxcmp,NULL);                                        //Sorts Gsequence por decrescente de Stars
			g_hash_table_insert(cities,strdup(city),(gpointer) leader);                    //Insere GSequence com a cidade
		}
		free_DadToSons(revs);freeBusiness(biz);free(city);free(bizID);
	}

	char *topline = strdup("Cidade;Business ID;Business Name;Stars");
	table_append_line(res,topline);

	QueryArg data = init_queryarg();
	set_query_int(data,top);
	set_query_table (data, res);

	g_hash_table_foreach(cities,(GHFunc) ajudaQuery6,(gpointer) data);                    //Percorre a HashTable com "AjudaQuery6"
	TABLE clone = table_clone(get_query_table(data));
	free_queryarg(data);
	g_hash_table_destroy(cities);

	return clone;
}

/* query 7 */ 
TABLE international_users (SGR sgr) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr->validStats)return get_table_invalidSgr(res);          //CHECK INICIAL

	char *topline = strdup("User ID");
	table_append_line(res, topline);

	int arr[6] = {1,0,0,0,0,0};
	int contagem = 0;
	int len = get_statsIndexLength(get_Index_pointer(sgr->stats,0));	               //Numero de Users a percorrer no INDEX usr->rev
	for(int aux = 0;aux<len;aux++){
		int positive = 0;
		DadToSons holder = get_statsIndexEntry(get_Index_pointer(sgr->stats,0),aux);   //Vê a listagem de reviews de um user
		int numRevs = get_DadToSons_numChilds(holder);                                 //Vê número de reviews
		if(numRevs > 1){                                                               //Se não for >1, ignora
			char* base = get_DadToSons_childID(holder,0);                              //Arranja o ID da primeira review
			StrReview baseR = findReview(base,sgr->rev);                               //Encontra a Review
			char* bizID = get_reviewBusinessID(baseR);                                 //Arranja o ID do negócio
			StrBusiness baseB = findBusiness(bizID,sgr->biz);                          //Encontra o negócio
			char* state = get_businessState(baseB);                                    //Arranja o estado do negócio
			for(int revs = 1;revs < numRevs && !positive;revs++){                 
				char *test=get_DadToSons_childID(holder,revs);                          //Arranja o ID da review na lista
				StrReview testR = findReview(test,sgr->rev);                            //Encontra a Review
				char* tbizID = get_reviewBusinessID(testR);                             //Arranja o ID do negócio
				StrBusiness testB = findBusiness(tbizID,sgr->biz);                      //Encontra o negócio
				char* tstate = get_businessState(testB);                                //Arranja o estado do negócio
				positive = strcmp(state,tstate);                                        //Testa com o da primeira review
				free(test);free(tbizID);free(tstate);
				freeBusiness(testB);freeReview(testR);
			}
			free(base);free(bizID);free(state);
			freeBusiness(baseB);freeReview(baseR);
			if (positive){                                                               //Se user international, adiciona
				char* userID = get_DadToSons_fatherID(holder);
				StrUser inter = findUser(userID,sgr->usr);                        
				char* line = userToString(inter,arr);
				table_append_line(res, line);
				freeUser(inter);
				free(userID);
				contagem++;
			}
		}
		free_DadToSons(holder);
	}
	table_setInfoLine_now(res);                                    //ESCREVE INFO LINE

	if (contagem == 0) table_append_line(res,strdup("Não há utilizadores Internacionais!"));
	else {
		char* line = malloc(sizeof(char)*60);
		sprintf(line,"Foram encontrados %d Utilizadores Internacionais!",contagem);
		table_append_line(res, line);
	}
	
	return res;
}

/* query 8 */
TABLE top_businesses_with_category (SGR sgr,int top,char* category) {
	(void) sgr;
	TABLE res = new_empty_table(1);

	if(!sgr_bus_valido(sgr) && !sgr_stats_valido(sgr))return get_table_invalidSgr(res);          //CHECK INICIAL

	GSequence* leaderboard = g_sequence_new((GDestroyNotify) free_sgrAux);                //Lista dos top negócios

    Index bus = get_Index_pointer(sgr->stats, 1);
	int numbus = get_statsIndexLength(bus);                      

	for (int i = 0; i < numbus; i++) {                                                    //Percorre o INDEX BUS->REV
		DadToSons revs = get_statsIndexEntry(bus,i);
		char* bizID = get_DadToSons_fatherID(revs);
		StrBusiness biz = findBusiness(bizID,sgr->biz);
		char* categorias = get_businessCategorias(biz);
		char* liberta = categorias;
		int found = 0;
		while(!found && categorias){                                                     //Procura a categoria pedida
			char* test = strdup(strsep(&categorias,"/"));
			if (strcmp(test,category) == 0) found = 1;
			free(test);
		}
		if(found){                                                                       //Se encontrada, adiciona à lista
			SgrAux new = new_sgrAux();
			new->values = calculaStars(sgr,revs);
			new->busid = get_businessID(biz);
			new->name = get_businessName(biz);
			g_sequence_append(leaderboard,(gpointer) new);
		}
		free_DadToSons(revs);freeBusiness(biz);free(liberta);free(bizID);
	}
	char *topline = strdup("Business ID;Business Name;Stars");
	table_append_line(res,topline);
	g_sequence_sort(leaderboard,sgrauxcmp,NULL);                                         //Ordena a lista por ordem decrescente de Stars
	GSequenceIter* iter = g_sequence_get_begin_iter(leaderboard);
	for (int aux = 0;aux < top && !g_sequence_iter_is_end(iter);aux++){                 //Percorre a lista até ao fim ou "top" chegado
		SgrAux escreve = g_sequence_get(iter);
		char* line = malloc(sizeof(char)*128);
		sprintf(line,"%s;%s;%.1f",escreve->busid,escreve->name,escreve->values);
		table_append_line(res,line);                                                     //Adiciona a line
		iter = g_sequence_iter_next(iter);
	}
	g_sequence_free(leaderboard);
	free(category);

	return res;
}

/* query 9 */
void ajudaQuery9 (char* key, StrReview value, QueryArg data) {
	(void) key;
	char* text = get_reviewTexto(value);
	char* word = get_query_string(data);
	int lenword = strlen(word);
	char* found = strstr(text,word);
	int stop = 0;
	while(found && !stop){
		if (found == text){                                                         //Caso encontra palavra no ínicio do texto
			if (found[lenword] == ' ' || ispunct(found[lenword])){                  //Apenas verifica se tem espaço no fim
				table_append_line(get_query_table(data),get_reviewID(value));       //Se encontra, adiciona e quebra o ciclo 
				inc_query_int(data);
				stop = 1;
			}
			else found = &found[1];                                                  //Atualiza pointer de procura
		}
		else if (found[-1] == ' ' && (found[lenword] == ' ' || found[lenword] == '\0' || ispunct(found[lenword]))){  //Caso a palavra esteja no meio/fim do texto.
			table_append_line(get_query_table(data),get_reviewID(value));             //Se encontra, adiciona e quebra o ciclo 
			inc_query_int(data);
			stop = 1;
		}
		else found = &found[1];                                                       //Atualiza pointer de procura
		found = strstr(found,word);  
	}
	free(text);free(word);
}

TABLE reviews_with_word(SGR sgr,int top,char* word) {
	(void) sgr; (void) top;
	TABLE res = new_empty_table(1);

	if(!sgr_rev_valido(sgr)) return get_table_invalidSgr(res);                 //CHECK INICIAL

	char *topline = strdup("Review ID");
	table_append_line(res,topline);

	QueryArg data = init_queryarg();
	set_query_int (data, 0);
	set_query_string (data,word);
	set_query_table (data, res); 

	infoReview_foreach (sgr->rev, (GHFunc) ajudaQuery9, (gpointer) data);    //ForEach Review "AjudaQuery9"
	TABLE clone = table_clone(get_query_table(data));

	int size = strlen(word);
	char * string = malloc (sizeof (char) * (size+90));
	sprintf (string, "Reviews com a palvra '%s' identificada no seu texto: %d",word,get_query_int(data));

	free_queryarg(data);
	table_setInfoLine_now(clone);
	table_append_line (clone,string);

	return clone;
}
