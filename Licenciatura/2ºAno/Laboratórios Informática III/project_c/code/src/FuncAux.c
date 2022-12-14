/**
 * Data de criação: 10/04/2021
 * Autor: Vicente Moreira - A93296
 * Versão: 20210417
 *
 * Ficheiro que contem funções auxiliares ao projeto.
 */

#include "../includes/FuncAux.h"

#define buffSize 1024

struct strList{
	int capacity;
	int len;
	char **content;
};

char *fgetsDinamico (FILE *file,int *signal){
	*signal = 0;
	int done = 0;
	int size = buffSize;

	char *res = malloc(sizeof(char) * size);
	res[size-2] = '\0';

	if(fgets(res,size,file)){};  //Como o fgets devolve size-1 elementos lidos... o elemento [size-1] será sempre '\0'

	while(!done){
		if(res[size-2] == '\0' || res[size-2] == '\n') {
			done = 1;
			if (feof(file)){
			*signal = 1;
			}
		}
		else {
			size *= 2; 
			res = realloc(res,sizeof(char) * size);
			res[size-2] = '\0';

			if(fgets(&(res[(size/2)-1]),(size/2)+1,file)){};
			//(size/2)-1 Dado que recomeça a escrever a metade do array -1 pois o fgets anterior deixou
			// '\0' no fim. EX:  "O.L.A.\0"(size 4) --> "O.L.A.\0.\0.\0.\0.\0"(size 8) 8/2-1= 3(\0)
			//
			//Também escreve (size/2)+1 devido as mesmas razões.
		}
	}
	return res;
}

StringL new_StringL_XEmpty(){
	StringL new = malloc(sizeof(struct strList));
	return new;
}

StringL new_StringL(){
	StringL new = new_StringL_XEmpty();
	new->capacity = 1;
	new->len = 0;
	return new;
}

StringL new_StringL_size(int size){ 
	StringL new = malloc(sizeof(struct strList));
	new->capacity = size;
	new->len = 0;
	new->content = malloc(sizeof(char*) * size);
	return new;
}

void StringL_append_line(StringL target,char* line){
	if (target->len == 0 && target->capacity == 1){
		target->content = malloc(sizeof(char*)); //Se é a primeira inserção
	}
	else if (target->capacity == target->len){  //Aumenta a capacity
		target->capacity *= 2;
		target->content = (char**) reallocarray(target->content,sizeof(char*),target->capacity);
	}
	target->content[target->len] = strdup(line);
	target->len++;
	free(line);
}

void StringL_remove_line(StringL target,int pos){
	if(pos<target->len){
		free(target->content[pos]);                 //Apaga a line
		target->len--;
		for(int aux = pos;aux<target->len;aux++){   //Move as linhas
			target->content[aux] = strdup(target->content[aux+1]);
			free(target->content[aux+1]);
		}
	}
}

char* get_StringL_line(StringL target,int pos){
	if (pos>target->len) return strdup("");   
	else return strdup(target->content[pos]);
}

int get_StringL_size(StringL target){
	return target->len;
}

int get_StringL_line_size(StringL target,int pos){
	if (pos>target->len) return 0;
	else return strlen(target->content[pos]);
}

int get_StringL_totalChrSize(StringL target){
	int cont = 0;
	for(int aux = 0;aux < target->len;aux++){
		cont += get_StringL_line_size(target,aux);
	}
	cont++;
	return cont;
}

void free_StringL(StringL target){
	if(target->len != 0){
			for(int aux =0 ;aux < target->len;aux++){
		free(target->content[aux]);
	}
	free(target->content);
	}
	free(target);
}

StringL clone_StringL(StringL target){
	StringL cloned = new_StringL(); 
	for(int aux = 0;aux<target->len;aux++){
		char *holder = strdup(target->content[aux]);
		StringL_append_line(cloned,holder);
	}
	return cloned;
}
