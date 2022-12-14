import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @Author Maria Eugénia
 * @Date 07/06/2021
 *
 * Classe Controller
 */

public class Controller{

    /** Base de Dados do programa */             private IgestReviews sgr;
    /** Objeto Utilizado para métodos da View */ private IVisualizador show;

    /**
     * Construtor por omissão
     */
    public Controller(){
        this.sgr = new gestReviews();
        this.show = new Visualizador();
    }

    /**
     * Método Principal.
     * Este faz o load inicial e executa o primeiro menu.
     */
    public void run (){
        show.clearScreen();
        show.println("A efetuar o carregamento dos catálogos default.");
        show.println("USER     PATH: "+IgestReviews.user_path);
        show.println("BUSINESS PATH: "+IgestReviews.bus_path);
        show.println("REVIEW   PATH: "+IgestReviews.rev_path+"\n");
        try {
            sgr.load_IgestReviews_csv(IgestReviews.user_path, IgestReviews.bus_path, IgestReviews.rev_path);
        }
        catch(UserIOException|ReviewIOException|BusinessIOException e){
           show.println("Erro no carregamento inicial. Verifique se contém os ficheiros base.");
            primaEnter();
        }
        executa_menu_inicial();
        show.clearScreen();
        show.menuSaida();
    }

    /**
     * Método auxiliar ao método "run".
     * Este executa a lógica da opção escolhida no menu principal
     */
    public void executa_menu_inicial () {
        int op = 1;
        while(true){
            show.clearScreen();
            if(op<=0||op>6) show.println("Opção inválida");
            show.boasVindas();
            show.menuInicio(sgr.valido());
            op = getOpcao(1,6);
            if (!this.sgr.valido() && (op == 2 || op == 3 || op == 4) ){
                show.println("Opção inválida pois não tenho o catálogo corretamente carregado.");
                primaEnter();
            }
            else{
                switch (op) {
                    case 1: //Carregamento de Ficheiros
                        carregamento_txt();
                        break;
                    case 2: //Queries
                        run_queries();
                        break;
                    case 3: //Stats
                        show.print(sgr.statsToString());
                        primaEnter();
                        break;
                    case 4: //Save Object
                        save_dat();
                        break;
                    case 5: //Load Object
                        load_dat();
                        break;
                    case 6: //Exit
                        return;
                    default:
                        break;
                }
            }


        }

    }

    /**
     * Método para carregar os ficheiros txt para o catálogo
     */
    public void carregamento_txt(){
        clear_IgestReviews();
        String usr = getStringMsg("User      File Path (Enter for default)");
        if (usr.equals("")) usr = IgestReviews.user_path;
        String biz = getStringMsg("Busisness File Path (Enter for default)");
        if (biz.equals("")) biz = IgestReviews.bus_path;
        String rev = getStringMsg("Review    File Path (Enter for default)");
        if (rev.equals("")) rev = IgestReviews.rev_path;
        try {
            show.println("Efetuando o load...");
            sgr.load_IgestReviews_csv(usr,biz,rev);
            show.println("Carregamento efetuado com sucesso!");
        }
        catch (UserIOException|BusinessIOException|ReviewIOException e){
            show.println(e.toString());
        }
        primaEnter();
    }

    /**
     * Método para guardar o catálogo
     */
    public void save_dat(){
        String path = getStringMsg("Save file (Enter for default)");
        if (path.equals("")) path = IgestReviews.dat;
        show.println("Saving...");
        try{
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.sgr);
            oos.flush();
            oos.close();

            show.println("Catálogo guardado com sucesso!");
        }
        catch (IOException e){
            show.println("Erro ao gravar o catálogo.\nMais Info: "+e);
        }
        primaEnter();
    }

    /**
     * Método para carregamento do catálogo
     */
    public void load_dat(){
        clear_IgestReviews();
        String path = getStringMsg("Load file (Enter for default)");
        if (path.equals("")) path = IgestReviews.dat;
        show.println("Loading...");
        try{
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.sgr = (IgestReviews) ois.readObject();

            show.println("Catálogo carregado com sucesso!");

        }
        catch (IOException|ClassNotFoundException e){
            show.println("Erro ao carregar o catálogo.\nMais Info: "+e);
        }
        primaEnter();
    }

    /**
     * Aciona o menu de opções para as queries e executa-as.
     */
    public void run_queries () {
        int opcao = 1;
        while(opcao != 11){
            show.clearScreen();
            if(opcao<=0||opcao>11) show.println("Opção inválida");
            show.menuQueries();
            opcao = getOpcao(1,11);
            String res = ""; String iden = "";
            int x = 5;
            long timeStart = System.nanoTime();

            switch (opcao) {
                case 1:
                    res = sgr.q1_listaNegociosNaoAvaliados();
                    break;
                case 2:
                    int ano = getValorNoIntervalo("Ano",1,3000);
                    int mes = getValorNoIntervalo("Mês",1,12);
                    timeStart = System.nanoTime();
                    res = sgr.q2_reviewsNumMes(ano,mes);
                    break;
                case 3:
                    iden = getStringIdentificador("User");
                    timeStart = System.nanoTime();
                    res = sgr.q3_infoUserReviews(iden);
                    break;
                case 4:
                    iden = getStringIdentificador("Negócio");
                    timeStart = System.nanoTime();
                    res = sgr.q4_avaliacaoMesAMes(iden);
                    break;
                case 5:
                    iden = getStringIdentificador("User");
                    timeStart = System.nanoTime();
                    res = sgr.q5_userNegociosMaisAvaliados(iden);
                    break;
                case 6:
                    x = getValorNoIntervalo("Top X",1,10000);
                    timeStart = System.nanoTime();
                    res = sgr.q6_conjuntoXNegociosMaisAvaliados(x);
                    break;
                case 7:
                    res = sgr.q7_cityHallOfFame();
                    break;
                case 8:
                    x = getValorNoIntervalo("Top X",1,10000);
                    timeStart = System.nanoTime();
                    res = sgr.q8_usersComMaisReviews(x);
                    break;
                case 9:
                    iden = getStringIdentificador("Negócio");
                    x = getValorNoIntervalo("Top X",1,10000);
                    timeStart = System.nanoTime();
                    res = sgr.q9_usersMaisAvaliaramNegocio(iden,x);
                    break;
                case 10:
                    res = sgr.q10_medias();
                    break;
                case 11:
                    return;
                default:
                    res = "";
                    break;
            }
            long timeFinal = System.nanoTime();
            double tempo = timeFinal - timeStart;
            show.print(res);
            show.printTime(tempo);
            primaEnter();
        }
    }

    /**
     * Recolhe a opção do menu desejada pelo o utilizador
     * @param limInferior limite inferior válido (inclusive)
     * @param limSuperior limite superior válido (inclusive)
     * @return opçcao
     */
    public int getOpcao (int limInferior, int limSuperior) {
        Scanner sc = new Scanner(System.in);
        int opcao = limInferior-1; //é inválido
        while(opcao < limInferior || opcao > limSuperior){
            show.print("Opção: ");
            try{
                opcao =sc.nextInt();
            }
            catch(InputMismatchException e){
                opcao = limInferior-1;
                String cleanErr = sc.nextLine();
            }
            if (opcao < limInferior || opcao > limSuperior) show.println("Opção Inválido.");
        }
        return opcao;
    }

    /**
     * Recolhe um valor dado pelo o utilizador, entre os limites indicados
     * @param infoValor informação a recolher do valor
     * @param limInferior limite inferior válido (inclusive)
     * @param limSuperior limite superior válido (inclusive)
     * @return valor recolhido
     */
    public int getValorNoIntervalo(String infoValor, int limInferior, int limSuperior){
        Scanner sc = new Scanner(System.in);
        int valor = limInferior-1; //é inválido
        while(valor < limInferior || valor > limSuperior){
            show.print("Introduza um valor ("+infoValor+") entre "+limInferior+" e "+limSuperior+": ");
            try{
                valor=sc.nextInt();
            }
            catch(InputMismatchException e){
                valor = limInferior-1;
            }
            if (valor < limInferior || valor > limSuperior) show.println("Valor Inválido.");
        }
        return valor;
    }

    public void clear_IgestReviews(){
        this.sgr = new gestReviews();
    }

    /**
     * Recolhe o identificador dado pelo o utilizador
     * @param infoString identificador a ser recolhido
     * @return identificador
     */
    public String getStringIdentificador (String infoString){
        Scanner sc = new Scanner(System.in);
        show.print("Introduza o identificador ("+infoString+"): ");
        return sc.nextLine();
    }

    /**
     * Recolhe uma string de input. Informa o utilizador para que esta string serve
     * @param msg mensagem antes da recolha da String
     * @return string
     */
    public String getStringMsg (String msg){
        Scanner sc = new Scanner(System.in);
        show.print(msg+": ");
        return sc.nextLine();
    }

    /**
     * Função de interrupção do programa. Espera por um enter simples
     */
    public void primaEnter(){
        show.print("Prima Enter para avançar ");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }


}
