#include "camadadados.h"
#include "camadainterface.h"

/**
@file main.c
Módulo principal do projeto
 */

/**
\brief Função main, principal. Inicializa um estado e chama o interpretador.
@return Indicador de sucesso, 0.
*/
int main(){
    ESTADO *e = inicializar_estado();
    interpretador(e);
return 0;
}

