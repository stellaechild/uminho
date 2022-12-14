import java.util.HashMap;
import java.util.Map;

/**
 * Classe da View
 * @author Vicente Moreira
 * @date 19/05/2021
 */

public class View {

    private Map<String,Menu> menus;

    /**
     * Construtor por Omissão
     */
    public View(){
        this.menus = new HashMap<>();
        montaMenus();
    }

    /**
     * Método para impressão de uma linha de texto. Criando um parágrafo
     */
    public void printLine(String msg){
        System.out.println(msg);
    }

    /**
     * Método para impressão de texto
     */
    public void print(String msg){
        System.out.print(msg);
    }

    /**
     * Imprime o Menu Inicial
     */
    public void showInicial(){
        System.out.println(Show.showInicial());
    }

    /**
     * Imprime o
     */
    public void showExit(){
        System.out.println(Show.showExit());
    }

    /**
     * Método que devolve o tamanho do menu
     */
    public int tamanho_menu(String menu){
        if(this.menus.containsKey(menu))
            return this.menus.get(menu).get_tamanho();
        else return 0;
    }

    /**
     * Limpa o ecrã
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Imprime um Menu
     */
    public void imprime_menu(String menu,boolean[] valido){
        this.menus.get(menu).imprime_menu(valido);
    }

    /**
     * Método que monta os Menus da aplicação
     */
    private void montaMenus(){

        Menu inicial = new Menu(new String[]{
                "Sair",
                "Novo jogo (Default)",
                "Carregar Jogo",
                "Carregar Jogo do LOG"});

        menus.put("inicial",inicial);
        Menu principal = new Menu(new String[]{
                "Voltar ao menu",
                "Guardar jogo",
                "Consulta",
                "Gestão de Equipas/Jogadores",
                "Próximo jogo"});

        menus.put("principal",principal);

        Menu sair = new Menu(new String[]{
                "Sair",
                "Ficar",});
        menus.put("sair",sair);

        Menu consulta = new Menu(new String[]{
                "Voltar",
                "Informação de uma Equipa",
                "Lista de Jogos",
                "Classificações"});
        menus.put("consulta",consulta);

        Menu consulta_eq = new Menu(new String[]{
                "Voltar",
                "Listar Jogadores",
                "Listar Treinadores"});
        menus.put("consulta_EQ",consulta_eq);

        Menu gestao = new Menu(new String[]{
                "Voltar",
                "Adicionar Equipa",
                "Adicionar Jogador",
                "Adicionar Treinador",
                "Adicionar Jogo",
                "Remover Equipa",
                "Remover Jogador",
                "Remover Treinador",
                "Remover Jogo",
                "Transferir Jogador",
                "Transferir Treinador"});
        menus.put("gestao",gestao);

    }

}
