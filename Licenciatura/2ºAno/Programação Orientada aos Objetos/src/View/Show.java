/**
 * Classe da View
 * @author Joana Alves
 * @date 20/05/2021
 */

public class Show {

    /**
     * Mensagem de Início do programa.
     */
    public static String showInicial() {
        final StringBuilder texto = new StringBuilder();
        texto.append("---------------------------------------------------------------\n");
        texto.append("                    BEM-VINDO AO FUMTEBOL !                    \n");
        texto.append("---------------------------------------------------------------\n");
        return texto.toString();
    }

    /**
     * Mensagem de Saída do programa.
     */
    public static String showExit() {
        final StringBuilder texto = new StringBuilder();
        texto.append("\n");
        texto.append("---------------------------------------------------------------\n");
        texto.append("        OBRIGADO POR JOGAR CONNOSCO! VOLTE DEPRESSA :D         \n");
        texto.append("---------------------------------------------------------------\n");
        return texto.toString();
    }
}