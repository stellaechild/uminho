#include <stdlib.h>
#include "modulo_c.h"
#include <errno.h>
#include <string.h>
#include <time.h>


int modulo_c (char *nome) {
	clock_t tempo_inicial = clock();

	fprintf(stdout,"A iniciar a execução do módulo C...\n\n");

	int num_blocos, rle, tam_bloco, tam_max_codificacao;
	int tam_bloco_comprimido;
	unsigned char rl, caracter_comprimido;
	Comprimidos lista_comprimidos, tmp, anterior;
	Digitos_Binarios array_codificado[256];

	FILE *ficheiro_cod = abre_ficheiro_correto (nome, 2);
	FILE *shaf;

	size_t tam_nome_original = strlen (nome);

	if (errno == 0) { // 0 == abre o ficheiro com sucesso
		char nome_shaf[tam_nome_original+10];
		if(fscanf(ficheiro_cod, "@%c@%d@",&rl,&num_blocos)){};
		int tam_blocos_antes  [num_blocos];
		int tam_blocos_depois [num_blocos];

		rle = (rl == 'R')? 1 : 0;

		trata_nome_ficheiro(nome,nome_shaf,4,rle);
        FILE *original = fopen(nome_shaf,"rb");

        if (errno == 0){

		trata_nome_ficheiro (nome, nome_shaf, 3, rle);
		shaf = fopen (nome_shaf, "wb");
		fprintf (shaf, "@%d@", num_blocos);
		
		for (int aux = 1; aux <= num_blocos; aux++) {
			if (fscanf (ficheiro_cod, "%d@", &tam_bloco)){};
			tam_blocos_antes[aux-1] = tam_bloco;
			tam_max_codificacao = cria_array_codificado (ficheiro_cod, array_codificado);
			lista_comprimidos = le_caracter (original, array_codificado, tam_max_codificacao, tam_bloco, &tam_bloco_comprimido);
			tam_blocos_depois[aux-1] = tam_bloco_comprimido;
			tmp = lista_comprimidos;
			fprintf (shaf, "%d@", tam_bloco_comprimido);
			
			for (int i = 0; i < tam_bloco_comprimido; i++) {
				caracter_comprimido = acesso_simbolos_comprimidos(tmp);
				fprintf (shaf, "%c", caracter_comprimido);
				anterior=tmp;
				tmp = devolve_proximo(tmp);
				free(anterior);
			}
			if (aux != num_blocos) fprintf(shaf, "@");
			progresso (aux, num_blocos);
		}
		fclose (original);
		fclose (ficheiro_cod);
		fclose (shaf);
		
		clock_t tempo_final = clock();
		double tempo_total = ((double) (tempo_final - tempo_inicial) / CLOCKS_PER_SEC) * 1000;
		mensagem_sucesso (num_blocos, tam_blocos_antes, tam_blocos_depois, tempo_total, nome_shaf);
	    }
        else mensagem_erro_modulo_c();
	}
	else mensagem_erro_modulo_c();

	fputc('\n',stdout);
	return errno;
}