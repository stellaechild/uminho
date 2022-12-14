/**
 * Esta classe implementa um menu em modo texto.
 *
 * @author Vicente Moreira
 * @author Maria Cunha
 *
 * @date 19/05/2021
 */
import java.util.Arrays;
import java.util.List;

public class Menu {

    private List<String> opcoes;

    /**
     * Constructor for objects of class Menu
     */
    public Menu(String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
    }

    /**
     * Devolve o número de opções do menu
     */
    public int get_tamanho(){
        return this.opcoes.size();
    }

    /**
     * Imprime o menu. Recebendo o array de Booleans para quais opções estão disponíveis
     */
    public void imprime_menu(boolean[] valido){
        int i = 0;
        for(;valido != null && i < valido.length && i < this.opcoes.size();i++){
            if (valido[i]) System.out.print(i);
            else System.out.print("X");

            System.out.println(" - "+opcoes.get(i));
        }
        for(;i < this.opcoes.size();i++){
            System.out.println(i+" - "+opcoes.get(i));
        }
    }

    /**
     * Imprime uma lina de Menu
     */
    public void imprime_linha(int pos,boolean valido){
        if(pos < this.opcoes.size() && pos >= 0){
            if(valido) System.out.println(pos+" - "+opcoes.get(pos));
            else System.out.println("X - "+opcoes.get(pos));
        }
    }











































/*
    /**
     * Método para apresentar o menu e ler uma opção.
     *
     */ /*
    public void executa() {
        do {
            showMenu();
            this.op = lerOpcao();
            if(this.op != -1) executaOpcao();
        } while (this.op != 0);
    }

    /**
     * Função que testa se a opção é inválida
     */ /*
    private Boolean testaPreCond (int pos) {
        if (pos < 0 || pos > this.opcoes.size()-1) return false;
        else if (this.condicoes.get(pos) == null) return true;
        else return this.condicoes.get(pos).testa();
    }

    /** Apresentar o menu */ /*
    private void showMenu() {
        System.out.println("\n *** Menu *** ");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+" - ");
            if(!testaPreCond(i)) System.out.print("(Inválido) "); //isto aqui só resulta se tivermos tantas condiçoes quanto opcoes
            System.out.print(this.opcoes.get(i));
            System.out.println("");
        }
    }

    /** Ler uma opção válida */ /*
    private int lerOpcao() {
        int op;
        Scanner is = new Scanner(System.in);

        System.out.print("Opção: ");
        try { op = is.nextInt(); }
        catch (InputMismatchException e) {op = -1;}

        if (!testaPreCond(op)) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }

    /**
     * Executa a opção, sendo esta válida
     */ /*
    private void executaOpcao(){
        //TRY AND CATCH AQUI PLS
        //HandlerNotSetException
        if (this.handlers.get(op) != null) this.handlers.get(op).executa();
    }

    /**
     * Método para obter a Última opção lida
     */ /*
    public int getOpcao() {
        return this.op;
    }

    /**
     * Set PreCondition
     */ /*
    public void setCondicao(int opcao, PreCondition condition){
        this.condicoes.set(opcao,condition);
    }

    /**
     * Set Handler
     */ /*
    public void setHandler(int opcao, Handler func){
        this.handlers.set(opcao,func);
    }
    */
}