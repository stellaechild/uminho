/**
 * @Author Joana Maia
 * @Date 05/06/2021
 *
 * Classe Visualizador
 */

public class Visualizador implements IVisualizador{

    public Visualizador(){
    }

    /**
     * Imprime Menu de Boas-Vindas
     */
    public void boasVindas() {
        StringBuilder res = new StringBuilder();
        res.append("----------------------------------------------------------------------------------------------------------------------------\n");
        res.append("                                                    WELCOME TO UMYELP!                                                      \n");
        res.append("----------------------------------------------------------------------------------------------------------------------------\n\n");
        System.out.print(res.toString());
    }

    /**
     * Imprime Menu principal
     * @param catalogo_valido Validade do catalogo
     */
    public void menuInicio(Boolean catalogo_valido){
        StringBuilder novo = new StringBuilder();
        novo.append("Estado do Catálogo: ");
        if(catalogo_valido) novo.append("válido\n");
        else novo.append("inválido!\n");
        novo.append("MENU INICIAL: \n");
        novo.append("1. Carregamento de ficheiros de texto\n");
        novo.append("2. Execução de Queries\n");
        novo.append("3. Acesso a estatísticas\n");
        novo.append("4. Guardar  Catálogo\n");
        novo.append("5. Carregar Catálogo\n");
        novo.append("6. Sair do programa\n");
        System.out.println(novo.toString());
    }

    /**
     * Imprime Menu de Saida
     */
    public void menuSaida() {
        StringBuilder novo = new StringBuilder();
        novo.append("----------------------------------------------------------------------------------------------------------------------------\n");
        novo.append("                                                    GOODBYE ! HAVE FUN !                                                    \n");
        novo.append("----------------------------------------------------------------------------------------------------------------------------\n\n");
        System.out.print(novo.toString());
    }

    /**
     * Imprime Menu de Queries
     */
    public void menuQueries(){
        StringBuilder novo = new StringBuilder();
        novo.append("QUERIES: \n");
        novo.append("1.  Lista ordenada alfabeticamente com os identificadores dos negócios nunca avaliados e o seu respetivo total.\n");
        novo.append("2.  Número total global de reviews e de users distintos que as realizaram, dado um mês e um ano.\n");
        novo.append("3.  Dado um código de utilizador, determinar para cada mês quantas reviews fez, quantos negócios distintos avaliou \n" +
                    "    e que nota média atribui.\n");
        novo.append("4.  Dado um código de um negócio, determinar, mês a mês, quantas vezes foi avaliado, por quantos users diferentes \n" +
                    "    e a média da classificação.\n");
        novo.append("5.  Dado o código de um utilizador determinar a lista de nomes de negócios que mais avaliou, e quantos, ordenada por\n" +
                    "    ordem decrescente de quantidade e, para quantidades iguais, por ordem alfabética dos negócios.\n");
        novo.append("6.  Determinar o conjunto dos X - um número inteiro dado pelo utilizador - negócios mais avaliados (com mais reviews)\n" +
                    "    em cada ano, indicando o número total de distintos utilizadores que o avaliaram.\n");
        novo.append("7.  Determinar, para cada cidade, a lista dos três mais famosos negócios em termos de número de reviews.\n");
        novo.append("8.  Determinar os códigos dos X - um número inteiro dado pelo utilizador - utilizadores que avaliaram mais negócios \n" +
                    "    diferentes, indicando quantos, sendo o critério de ordenação a ordem descrescente do número de negócios.\n");
        novo.append("9.  Dado o código de um negócio, determinar o conjunto dos X users que mais o avaliaram e, para cada um, qual o valor\n" +
                    "    médio de classificação - ordenação cf. 5.\n");
        novo.append("10. Determinar para cada estado, cidade a cidade, a média de classificação de cada negócio.\n");
        novo.append("11. Voltar\n");
        System.out.println(novo.toString());
    }

    /**
     * Limpa o ecra
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Imprime mensagem customizada
     * @param msg Mensagem
     */
    public void print(String msg){ System.out.print(msg); }

    /**
     * Imprime mensagem customizada (Com paragrafo final)
     * @param msg Mensagem
     */
    public void println (String msg){
        System.out.println(msg);
    }

    /**
     * Imprime o tempo fornecido. Muda dinamicamente a unidade de forma a manter a legibilidade.
     * @param time Tempo em nanosgundos.
     */
    public void printTime (double time) {
        int unit = 0;
        while (time > 1000) {
            time /= 1000;
            unit++;
        }
        System.out.print ("Comando executado com sucesso! (" +String.format("%.3f",time)+" ");
        if (unit == 0) System.out.println("nanosegundos)");
        else if (unit == 1) System.out.println("microsegundos)");
        else if (unit == 2) System.out.println("milissegundos)");
        else if (unit == 3) System.out.println("segundos)");
        else System.out.println("");
    }
}
