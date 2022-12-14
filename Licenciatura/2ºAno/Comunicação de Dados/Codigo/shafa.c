#include <stdio.h>
#include <time.h>

#include "interface.h"
#include "modulo_f/modulo_f.h"
#include "modulo_t/modulo_t.h"
#include "modulo_c/modulo_c.h"
#include "modulo_d/modulo_d.h"


int main (int argc, char **argv) {
    int forcar_rle=0, modulo = 0;
    char tamanho_rle = 'k';                   //Tamanhos:'k' -64Kbytes /'K' -640Kbytes /'m'-8Mbytes /'M'-64Mbytes
    int pos_nome=-1,erro;
    int opcao_descomprimido = 0;
    int controle=0;

    erro = le_argumentos(argc,argv,&pos_nome,&modulo,&forcar_rle,&tamanho_rle,&opcao_descomprimido);

    if (erro == 0 && pos_nome != -1){
        if (modulo != 0){
            mensagem_execucao_modulo_individual(argv[pos_nome],modulo,forcar_rle,tamanho_rle,opcao_descomprimido);
            switch (modulo) {
                case 1: modulo_f (argv[pos_nome],forcar_rle,tamanho_rle);
                    break;
                case 2: modulo_t (argv[pos_nome]);
                    break;
                case 3: modulo_c (argv[pos_nome]);
                    break;
                case 4: modulo_d (argv[pos_nome],opcao_descomprimido);
                    break;
                default: fprintf(stdout, "Erro inesperado, o programa nao devia ter chegado aqui\n");
                    break;
            }
        }
        else {
            clock_t inicio = clock();
            controle = modulo_f (argv[pos_nome],forcar_rle,tamanho_rle);
            if (controle == 0)controle = modulo_t (argv[pos_nome]);
            if (controle == 0)controle = modulo_c (argv[pos_nome]);
            if (controle == 0){
                apaga_ficheiros_intermedios(argv[pos_nome]);
                clock_t fim = clock();
                double tempo_total = ((double) (fim - inicio) / CLOCKS_PER_SEC);
                fprintf(stdout,"Compressao terminada em %.2fs\n",tempo_total);
            }
        }
    }
    else { if (erro == -1) mensagem_ajuda();
           else mensagem_erro(erro);
         }

    return 0;
}


