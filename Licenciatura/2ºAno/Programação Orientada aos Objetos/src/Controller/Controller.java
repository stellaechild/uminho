import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Classe do Controlador
 * @author Vicente Moreira
 * @author Maria Cunha
 * @author Joana Alves
 *
 * @date 19/05/2021
 */

public class Controller {
    private Liga liga;
    private View view;

    /**
     * Construtor por omissão
     */
    public Controller(){
        this.liga = new Liga();
    }

    /**
     * Construtor parametrizado
     */
    Controller(Liga liga,View view){
        this.liga = liga;
        this.view = view;
    }

    //--------------------------------------------METODOS PARA EXECUCAO DE MENUS----------------------------

    /**
     * Método principal.
     * Corre o programa.
     */
    public void run(){
        int opcao;
        while (true){

            view.showInicial();
            opcao = executa_menu("inicial",null);
            view.clearScreen();
            switch (opcao){
                case 0:                                                             //SAIR
                    view.showExit();
                    return;
                case 1:                                                             //LIGA DEFAULT
                    liga = LigaDefault.ligaDefault();
                    break;
                case 2:                                                             //CARREGAR JOGO
                    view.printLine("Insira o nome do ficheiro:");
                    String path = getString();
                    try{
                        carregaEstado(path);
                    }
                    catch (IOException e){
                        view.printLine("Ficheiro inexistente/Erro de load");
                        opcao = -1;
                    }
                    catch (ClassNotFoundException e){
                        view.printLine("Ficheiro inválido");
                        opcao = -1;
                    }
                    if(opcao != -1) view.printLine("Jogo carregado com sucesso!");
                    break;
                case 3:
                    try{
                        carregaEstadoFromLOG();
                    }
                    catch (IOException|EquipaNaoExistenteException|EquipaJaExistenteException|
                        JogadorJaExistenteException|JogoJaExistenteException e){
                        view.printLine(e.toString());
                    }
                    break;
            }
            if(opcao == 1 || opcao == 2 || opcao == 3) executa_loop_jogo();                       //EXECUTA JOGO
            view.clearScreen();
        }
    }

    /**
     * Corre o loop do menu do jogo.
     */
    void executa_loop_jogo(){
        int opcao;
        while (true){
            boolean[] preCond = new boolean[5];
            preCond[0] = true; preCond[1] = preCond[0];
            preCond[2] = preCond[0];preCond[3] = preCond[0];
            preCond[4] = false;

            opcao = executa_menu("principal",preCond);
            view.clearScreen();
            switch (opcao){

                case 0:                                                             //VOLTAR AO MENU
                    view.printLine("Pretende Sair?");
                    int ficar = executa_menu("sair",preCond);
                    if (ficar == 0) return;
                    break;

                case 1:                                                             //GUARDAR JOGO
                    view.printLine("Insira o nome do ficheiro:");
                    String path = getString();
                    try{
                        saveToFile(path);
                    }
                    catch (IOException e){
                        view.printLine("Erro ao gravar");
                        view.printLine(e.toString());
                        opcao = -1;
                    }
                    if(opcao != -1) view.printLine("Jogo gravado com sucesso!");
                    break;

                case 2:                                                             //CONSULTA
                    executa_consulta();
                    break;

                case 3:                                                             //GESTÃO
                    executa_gestao();
                    break;

                case 4:                                                             //PRÓXIMO JOGO

                    break;

                default:
                    break;
            }
        }
    }

    //-----------------------------------------GESTAO------------------------------------------------

    /**
     * Opção para executar a consulta.
     */
    void executa_gestao(){
        int opcao;
        while (true){

            boolean[] preCond = new boolean[11];
            for(int aux = 0;aux < 11;aux++)preCond[aux] = true;
            if(this.liga.getNumEquipas() <= 0) preCond[5] = false;
            preCond[6] = preCond[5]; preCond[7] = preCond[5]; preCond[9] = preCond[5]; preCond[10] = preCond[5];
            if(this.liga.getNumJogos() <= 0) preCond[8] = false;

            view.print(liga.toString());
            opcao = executa_menu("gestao",preCond);
            view.clearScreen();
            switch (opcao){
                case 0:
                    return;
                case 1:  //Adiciona Equipa
                    adicionaEquipa();
                    break;
                case 2:  //Adicionar Jogador
                    adicionaJogador();
                    break;
                case 3:  //Adicionar Treinador
                    adicionaTreinador();
                    break;
                case 4:  //Adicionar Jogo
                    adicionaJogo();
                    break;
                case 5:  //Remover Equipa
                    removeEquipa();
                    break;
                case 6:  //Remover Jogador
                    removeJogador();
                    break;
                case 7:  //Remover Treinador
                    removeTreinador();
                    break;
                case 8:  //Remover Jogo
                    removeJogo();
                    break;
                case 9:  //Transferir Jogador
                    transfereJogador();
                    break;
                case 10: //Transferir Treinador
                    transfereTreinador();
                    break;
                default:
                    break;
            }
        }
    }


    //---------------------------------------------CONSULTA----------------------------------------------

    /**
     * Opção para executar a consulta.
     */
    void executa_consulta(){
        int opcao;
        while (true){
            boolean[] preCond = new boolean[4];
            preCond[0] = true; preCond[1] = liga.getNumEquipas()>0;
            preCond[2] = liga.getNumJogos() > 0; preCond[3] = preCond[1];

            view.print(liga.toString());
            opcao = executa_menu("consulta",preCond);
            view.clearScreen();
            switch (opcao){
                case 0:
                    return;
                case 1:
                    view.printLine("Insira o nome da Equipa a consultar:");
                    String equipa = getString();
                    try{
                        executa_consulta_equipa(equipa);
                    }
                    catch (EquipaNaoExistenteException e){
                        view.print(e.toString());
                    }
                    break;
                case 2:
                    view.print(liga.JogosToString());
                    break;
                case 3:
                    view.print(liga.classificacaoToString());
                    break;
            }
        }
    }

    /**
     * Método auxiliar ao "executa_consulta", Lista elementos da equipa
     */
    void executa_consulta_equipa(String equipa) throws EquipaNaoExistenteException{
        int opcao;
        while (true){
            Equipa alvo = liga.getEquipa(equipa);
            boolean[] preCond = new boolean[3];
            preCond[0] = true;
            preCond[1] = alvo.getNumJogadores()>0;
            preCond[2] = alvo.getNumTreinadores() > 0;

            view.print(alvo.toString());
            opcao = executa_menu("consulta_EQ",preCond);
            view.clearScreen();
            switch (opcao){
                case 0:
                    return;
                case 1: //Consulta de Jogadores da Equipa
                    view.print(alvo.JogadoresToString());
                    break;
                case 2: //Consulta de Treinadores da Equipa
                    view.print(alvo.TreinadoresToString());
                    break;
            }
        }
    }








    //------------------------------------------METODOS AUXILIARES/RECOLHA DE INPUTS--------------------------

    /**
     * Executa os Menus da aplicação
     */
    public int executa_menu(String menu,boolean[] valido){
        while (true){
            int opcao = -1;
            view.imprime_menu(menu,valido);
            view.print("Opção: ");
            try{
                opcao = getOpcao(this.view.tamanho_menu(menu));
                view.clearScreen();
            }
            catch (OpcaoInvalidaException e){
                view.clearScreen();
                view.printLine(e.toString());
                opcao = -1;
            }
            if(opcao != -1){
                if (valido == null || (valido != null && valido[opcao])){
                    return opcao;
                }
                view.printLine("Opção Inválida");
            }
        }
    }

    /**
     * Recolhe uma opção, esta tem q estar entre 0 e maxopcoes
     */
    public int getOpcao(int maxopcoes) throws OpcaoInvalidaException{
        int op = 0;
        Scanner sc = new Scanner(System.in);
        try{
            op = sc.nextInt();
        }
        catch (InputMismatchException e){
            throw new OpcaoInvalidaException("Input inválido!");
        }
        if (op < 0 || op > maxopcoes){
            throw new OpcaoInvalidaException("Opção "+op+" inválida!");
        }
        return op;
    }

    /**
     * Recolhe uma String to utilizador
     */
    public String getString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * Recolhe a Equipa onde é efetuada a operação
     */
    public String getEquipaName(){
        view.print(liga.EquipasToString());
        view.print("Insira o nome da Equipa onde pretende efetuar a ação: ");
        return getString();
    }










    //--------------------------------------------METODOS DE INTERACAO COM O MODELO--------------------------------

    /**
     * Guarda o estado do jogo
     */
    public void saveToFile(String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(this.liga);

        oos.flush();
        oos.close();
    }

    /**
     * Carrega o estado do jogo
     */
    public void carregaEstado (String fetch) throws IOException,ClassNotFoundException{
        FileInputStream fis = new FileInputStream(fetch);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.liga = (Liga) ois.readObject();
    }

    /**
     * Load txt File
     */
    public void carregaEstadoFromLOG () throws IOException,EquipaNaoExistenteException,EquipaJaExistenteException,
                                                JogadorJaExistenteException,JogoJaExistenteException{
        FileInputStream fis=new FileInputStream("LogV2.txt");
        Scanner sc=new Scanner(fis);
        this.liga = new Liga("Liga LOG");
        String equipa = "";
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            equipa = interpretaLinha(line,equipa);
        }
    }

    /**
     * Função auxiliar ao loadLOG
     */
    private String interpretaLinha(String line,String equipa) throws EquipaNaoExistenteException,EquipaJaExistenteException,
                                                                    JogadorJaExistenteException,JogoJaExistenteException{
        String[] comando = line.split(":");
        String[] args = comando[1].split(",");
        if(comando[0].equals("Equipa")){
            Equipa eq = new Equipa(args[0]);
            eq.setEstadio(new Estadio(args[0]+" Stadium",args[0]+" Street",50000));
            this.liga.addEquipa(eq);
            return args[0];
        }
        else if(comando[0].equals("Jogo")){
            String[] data_args = args[4].split("-");
            Jogo jog = new Jogo(args[0],args[1],LocalDateTime.of(Integer.valueOf(data_args[0]), Integer.valueOf(data_args[1]),
                                                                 Integer.valueOf(data_args[2]),20,30));
            jog.setGolosvisitados(Integer.valueOf(args[2]));
            jog.setGolosvisitantes(Integer.valueOf(args[3]));
            jog.setEstadodojogo(Jogo.FIM);
            jog.setTempo(LocalTime.of(1,30));
            this.liga.addJogo(jog);
        }
        else if(comando[0].equals("Guarda-redes")){
            Jogador gdr = new GuardaRedes(args[0],Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]),Integer.valueOf(args[7]),
                    Integer.valueOf(args[8]),50,50,50);
            this.liga.getEquipa(equipa).addJogador(gdr, LocalDate.now());
        }
        else if(comando[0].equals("Lateral")){
            Jogador lat = new Lateral(args[0],Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]),Integer.valueOf(args[7]),
                    Integer.valueOf(args[8]),50,50,50);
            this.liga.getEquipa(equipa).addJogador(lat,LocalDate.now());
        }
        else if(comando[0].equals("Defesa")){
            Jogador def = new Defesa(args[0],Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]),Integer.valueOf(args[7]),
                    Integer.valueOf(args[8]),50,50,50);
            this.liga.getEquipa(equipa).addJogador(def,LocalDate.now());
        }
        else if(comando[0].equals("Medio")){
            Jogador med = new Medio(args[0],Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]),Integer.valueOf(args[7]),
                    Integer.valueOf(args[8]),50,50,50);
            this.liga.getEquipa(equipa).addJogador(med,LocalDate.now());
        }
        else if(comando[0].equals("Avancado")) {
            Jogador ava = new Avancado(args[0], Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]),Integer.valueOf(args[7]),
                    Integer.valueOf(args[8]), 50, 50, 50);
            this.liga.getEquipa(equipa).addJogador(ava,LocalDate.now());
        }

        return equipa;
    }


    /**
     * Adiciona uma nova Equipa
     */
    public void adicionaEquipa(){
        view.printLine("Insira os Dados da Equipa no seguinte formato:");
        view.printLine("[Nome];[Nome Do Estádio];[Local];[Capacidade do estádio]");
        String line = getString();
        try {liga.addEquipaFromLine(line);}
        catch (ArgumentosInvalidosException|NumeroDeArgumentosInvalidosException|EquipaJaExistenteException e){
            view.printLine(e.toString());}
    }

    /**
     * Adiciona um novo Jogador
     */
    public void adicionaJogador(){
        try {
            String nomeequipa = getEquipaName();
            Equipa eq = this.liga.getEquipa(nomeequipa);
            view.printLine("Posições disponíveis: avancado,defesa,guardaredes,lateral,medio");
            view.printLine("Insira a informação do jogador no seguinte formato:");
            view.printLine("[Nome];[Posicao];[Número];[VEL];[RES];[DES];[IMP];[JGC];[REM];[PAS];[ESP1];[ESP2];[ESP3]");
            String line = getString();
            eq.addjogadorFromLine(line,liga.getData_atual());
        }
        catch (ArgumentosInvalidosException|NumeroDeArgumentosInvalidosException
                |JogadorJaExistenteException|EquipaNaoExistenteException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Adiciona um novo Treinador
     */
    public void adicionaTreinador(){
        try {
            String nomeequipa = getEquipaName();
            Equipa eq = this.liga.getEquipa(nomeequipa);
            view.printLine("Insira a informação do Treinador no seguinte formato:");
            view.printLine("[Nome];[Habilidade]");
            String line = getString();
            eq.addTreinadorFromLine(line);
        }
        catch (ArgumentosInvalidosException|NumeroDeArgumentosInvalidosException
                |TreinadorJaExistenteException|EquipaNaoExistenteException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Adiciona um Jogo à Liga
     */
    public void adicionaJogo(){
        view.print(liga.EquipasToString());
        view.printLine("Insira a informação do jogo no seguinte formato:");
        view.printLine("[Equipa Visitada];[Equipa Visitante];[Ano];[Mês];[Dia];[Hora];[Minuto]");
        String line = getString();
        try {
            this.liga.addJogoFromLine(line);
        }
        catch (NumeroDeArgumentosInvalidosException|ArgumentosInvalidosException|
                JogoJaExistenteException|EquipaNaoExistenteException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Remove uma Equipa
     */
    public void removeEquipa(){
        try {
            String nome = getEquipaName();
            this.liga.removeEquipa(nome);
        }
        catch (EquipaNaoExistenteException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Remove um Jogador
     */
    public void removeJogador(){
        try {
            String nome = getEquipaName();
            view.print(this.liga.getEquipa(nome).JogadoresToString());
            view.print("Insira o número do Jogador que pretende remover: ");
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
            this.liga.getEquipa(nome).removeJogador(num);
        }
        catch (EquipaNaoExistenteException|JogadorNaoExistenteException|NumberFormatException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Remove um Treinador
     */
    public void removeTreinador(){
        try {
            String nome = getEquipaName();
            view.print(this.liga.getEquipa(nome).TreinadoresToString());
            view.print("Insira o nome do Treinador que pretende remover: ");
            String treinador = getString();
            this.liga.getEquipa(nome).removeTreinador(treinador);
        }
        catch (EquipaNaoExistenteException|TreinadorNaoExistenteException|NumberFormatException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Remove um Jogo
     */
    public void removeJogo(){
        view.print(this.liga.JogosToString());
        view.print("Insira o número do jogo a ser removido: ");
        try {
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
            this.liga.removeJogo(num-1);
        }
        catch (JogoNaoExistenteException|NumberFormatException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Transfere Jogador
     */
    public void transfereJogador(){
        try {
            view.printLine("Equipa de origem:");
            String nome_orig = getEquipaName();
            view.print(this.liga.getEquipa(nome_orig).JogadoresToString());
            view.print("Insira o número do Jogador que pretende transferir: ");
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
            if(!this.liga.getEquipa(nome_orig).containsJogador(num)) throw new JogadorNaoExistenteException("Jogador inexistente");

            view.printLine("Equipa de destino:");
            String nome_destino = getEquipaName();
            if(this.liga.getEquipa(nome_destino).containsJogador(num)) throw new JogadorJaExistenteException("Jogador já existe nessa equipa");

            Jogador jogador = this.liga.getEquipa(nome_orig).retiraJogador(num,this.liga.getData_atual());
            this.liga.getEquipa(nome_destino).addJogador(jogador,this.liga.getData_atual());

        }
        catch (EquipaNaoExistenteException|JogadorNaoExistenteException|JogadorJaExistenteException|NumberFormatException e){
            view.printLine(e.toString());
        }
    }

    /**
     * Transfere Treinador
     */
    public void transfereTreinador(){
        try {
            view.printLine("Equipa de origem:");
            String nome_orig = getEquipaName();
            view.print(this.liga.getEquipa(nome_orig).TreinadoresToString());
            view.printLine("Insira o nome do Treinador que pretende transferir: ");
            String nome = getString();
            if(!this.liga.getEquipa(nome_orig).containsTreinador(nome));

            view.printLine("Equipa de destino:");
            String nome_destino = getEquipaName();
            if(this.liga.getEquipa(nome_destino).containsTreinador(nome));

            Treinador trei = this.liga.getEquipa(nome_orig).retiraTreinador(nome);
            this.liga.getEquipa(nome_destino).addTreinador(trei);

        }
        catch (EquipaNaoExistenteException|TreinadorNaoExistenteException|TreinadorJaExistenteException|NumberFormatException e){
            view.printLine(e.toString());
        }
    }

}
