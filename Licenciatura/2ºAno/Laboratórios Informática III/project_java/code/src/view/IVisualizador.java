/**
 * @Author Joana Maia
 * @Date 05/06/2021
 *
 * Interface de Visualizador
 */

public interface IVisualizador {

    /**
     * Imprime Menu de Boas-Vindas
     */
    public void boasVindas();

    /**
     * Imprime Menu principal
     * @param catalogo_valido Validade do catalogo
     */
    public void menuInicio (Boolean catalogo_valido);

    /**
     * Imprime Menu de Queries
     */
    public void menuQueries ();

    /**
     * Imprime Menu de Saida
     */
    public void menuSaida ();

    /**
     * Limpa o ecra
     */
    public void clearScreen();

    /**
     * Imprime mensagem customizada
     * @param msg Mensagem
     */
    public void print (String msg) ;

    /**
     * Imprime mensagem customizada (Com paragrafo final)
     * @param msg Mensagem
     */
    public void println (String msg);

    /**
     * Imprime o tempo fornecido. Muda dinamicamente a unidade de forma a manter a legibilidade.
     * @param time Tempo em nanosgundos.
     */
    public void printTime (double time);

}
