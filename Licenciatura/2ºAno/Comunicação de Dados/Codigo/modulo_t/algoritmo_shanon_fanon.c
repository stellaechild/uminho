#include "algoritmo_shanon_fanon.h"


void shanon_fanon(Casas_de_Simbolos topo_de_lista){

    int total_simbolos = 0;
    Casas_de_Simbolos percorre = topo_de_lista;

    while (proxima_casa(percorre) != NULL) {   //Faz a contagem total dos símbolos na lista
        total_simbolos += le_casa_frequencia(percorre);
        percorre = proxima_casa(percorre);
    }
    total_simbolos += le_casa_frequencia(percorre);

    //Função recursiva
    encontra_meio(topo_de_lista,total_simbolos);
}



void encontra_meio(Casas_de_Simbolos inicio,int total_simbolos){
    int difAntes,difDepois,simb_restantes,simb_restantes_copy;
    int meio = total_simbolos/2;
    int numero_simb_ja_vistos = 0;
    int freq_inicial = le_casa_frequencia(inicio);
    Casas_de_Simbolos temporario = NULL;

    //Check inicial caso o elemento é único (Retira a freq do primeiro símbolo da lista e verifica se
    // o "total de simbolos" é igual a essa frequência)
    if (total_simbolos != freq_inicial){

        // Insere 0 na codificação do primeiro símbolo
        acrescenta_digito_casa(0, inicio);
        numero_simb_ja_vistos += freq_inicial;
        temporario = inicio;

        // Entrada no loop de "Será que já cheguei o mais perto possível do meio?"
        do {
            temporario = proxima_casa(temporario);
            difAntes = abs(meio - numero_simb_ja_vistos);
            difDepois = abs(meio - numero_simb_ja_vistos - le_casa_frequencia(temporario));
            if (difAntes > difDepois){                                   //Se não cheguei ao meio...
                acrescenta_digito_casa(0, temporario);             // Atribui 0 à codificação do próximo símbolo
                numero_simb_ja_vistos += le_casa_frequencia(temporario); // Atualiza "símbolos já lidos"
            }
        } while (difAntes > difDepois);

        //Cálculo do tamanho restante da lista
        simb_restantes = total_simbolos-numero_simb_ja_vistos;
        simb_restantes_copy = simb_restantes;
        Casas_de_Simbolos metade = temporario;

        //Insere 1's nos elementos restantes
        do {
            temporario = acrescenta_digito_casa(1, temporario);
            simb_restantes = simb_restantes-le_casa_frequencia(temporario);
            temporario = proxima_casa(temporario);
        } while (simb_restantes != 0);

        //Chamada recursiva dos dois sub-grupos.
        encontra_meio(inicio,numero_simb_ja_vistos);
        encontra_meio(metade,simb_restantes_copy);
    }
}


Casas_de_Simbolos organiza_array_na_struct (int *array){
    //Inicialização
    int posicao_max,freq_max,atual;
    int array_freq_vazias = 0;
    Casas_de_Simbolos topo,percorre,anterior;
    topo = novo_simbolo();
    percorre = topo;
    anterior = percorre;

    // Percorre o array 256 vezes ou até não haver mais frequências não nulas.
    for (int aux = 0;aux<256 && array_freq_vazias == 0;aux++){
        posicao_max = -1;
        freq_max = 0;

        //Percorre o array inteiro à procura do elemento com maior frequência
        for (int aux2 = 0;aux2<256;aux2++){
            atual = array[aux2];
            if (atual > freq_max){
                freq_max = atual;
                posicao_max = aux2;
            }
        }

        if (posicao_max == -1) array_freq_vazias = 1; //Caso não haja valores não nulos, marca como vazio
        else {
            escreve_casa_freq(freq_max,percorre);                        //Depois de encontrado o valor maior...
            escreve_casa_simbolo((unsigned char) posicao_max,percorre);  //Cria um novo nodo de Casa de Simbolo,
            adiciona_nova_casa(percorre);                                // guarda lá o simbolo correspondente
            anterior = percorre;                                         // e a sua frequência.
            percorre = proxima_casa(percorre);                           // Põe a 0 no array das frequências para
            array[posicao_max] = 0;                                      // a próxima passagem.
        }

    }

    free (anterior->prox);
    anterior->prox = NULL;
    return topo;
}
