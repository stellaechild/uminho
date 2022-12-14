#include "camadadados.h"
#include "gestaodeficheiros.h"
#include "botavancado.h"

int main(int argc,char **argv){
    if (argc == 3){
        int mensagem;
        ESTADO *e = inicializar_estado();
        mensagem = ler(e,argv[1],&mensagem);
        jog2(e);
        gravar(e,argv[2]);
    }

return 0;
}

