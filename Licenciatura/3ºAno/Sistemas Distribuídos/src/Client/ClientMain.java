import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {

    /** Ip do servidor */
    private InetAddress serverIP;
    /** ID da sessão do cliente atual */
    private String sessionID = "Nxxxxx";
    /** Porta default do servidor */
    private final short defaultPort = 8888;
    /** Privilégios de administrador */
    private boolean admin = false;
    /** Transmissor */
    private SRVTransmitter transmitter;

    public static void main(String[] args){
        ClientMain clientMain = new ClientMain();
        clientMain.run(args);
        System.out.println("Exiting...");
    }

    /**
     * Método run do main Cliente
     * @param args Argumentos de execução
     */
    public void run(String[] args){

        /** Verifica o número de argumentos */
        if (args.length != 1) {
            System.out.println("ServerIP required");
            return;
        }

        /** Verifica a validade do IP */
        try{
            serverIP = InetAddress.getByName(args[0]);
        }
        catch (UnknownHostException e){
            System.out.println("Invalid IP!");
            return;
        }

        System.out.println("SRV - Sistema de Reserva de Voos");
        System.out.println("MIEI/LEI - Sistemas Distribuídos (2021/2022)\n");

        while(true){
            /** Efetua login */
            admin = false;
            if(loginUser()){
                /** Verifica privilégios de admin, analisando o Id da Sessao recebido */
                if (sessionID.contains("A")) admin = true;
                /** Loop de Menu principal */
                if(mainMenu(admin)){
                    transmitter.close();
                    return;
                }
                clearScreen();
                System.out.println("Credenciais de acesso desatualizadas, repita o login");
            }
            /** Caso o login falhe, pergunta se pretende sair */
            else if (wantToExit()){
                transmitter.close();
                return;
            }
        }

    }

    /**
     * Método de interface de menus para login. Recolhe o nome e password e efetua a comunicação inicial
     * @return boolean - true se o login for efetuado com sucesso.
     */
    public boolean loginUser(){
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            sessionID = "Nxxxxx";
            System.out.println("Introduza as suas credenciais");
            System.out.print("Username: ");
            String username = reader.readLine();
            System.out.print("Password: ");
            String pass = reader.readLine();

            /** Efetua comunicação */
            System.out.println("Logging in...");
            SRVPacket loginPacket = SRVPacket.login(username, pass);
            transmitter = new SRVTransmitter(new Socket(serverIP, defaultPort));

            /** Recebe resposta */
            transmitter.writePacket(loginPacket);
            SRVPacket response = transmitter.readPacket();

            /** recolhe o ID da sessão */
            if (response.getCommand().equals(SRVPacket.Commands.Confirmacao) && response.getArgumentIdx(0).equals(SRVPacket.Commands.Login.toString())) {
                sessionID = response.getIdSession();
                return true;
            } else {
                /** Erro de Login */
                System.out.print("Login error: ");
                if (response.getCommand().equals(SRVPacket.Commands.Erro) && response.getArgumentIdx(0).equals(SRVPacket.Erros.ErroGenerico.toString()))
                    System.out.println(response.getArgumentIdx(1));
                return false;
            }
        }catch (IOException|InvalidSRVPacketException e){
            /** Erro inesperado */
            System.out.println("Error: "+e);
            return false;
        }
    }

    public boolean mainMenu(boolean admin){
        clearScreen();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        String Cyellow = "\u001B[33m";
        String CReset = "\u001B[0m";
        String CGreen = "\u001B[1;32m";

        int op = -1;
        while(op != 0) {
            printMenu(admin);

            /** Lê a opção inserida */
            try {op = Integer.parseInt(reader.readLine());}
            catch (IOException | IllegalArgumentException e) {op = -1;}
            clearScreen();

            try {
                String inputLine;
                String[] inputFields;
                switch (op) {
                    /** Listar Voos */
                    case 1:
                        System.out.println("Listar Voos - Escreva os inputs no seguinte formato:\n"+CGreen+"Origem;Destino"+CReset);
                        System.out.print("-> ");
                        inputFields = reader.readLine().split(";");
                        if (inputFields.length == 2) {
                            SRVPacket lv = SRVPacket.listaVoos(sessionID, inputFields[0], inputFields[1]);
                            transmitter.writePacket(lv);
                        } else{
                            op = erroInput();
                        }
                        break;

                    /** Reservar Voos */
                    case 2:
                        System.out.println("Reservar Voo - Escreva os inputs no seguinte formato:");
                        System.out.println(CGreen+"Data;HoraInicial;HoraFinal;Origem;Escala1;Escala2;...;Destino"+CReset);
                        System.out.println("Data: AAAA-MM-DD. (Ex: 2021-01-27)");
                        System.out.println("Hora: hh-mm. (Ex: 09:30 / 18:20)");
                        System.out.println(Cyellow+"Ex1: 2021-01-07;08:00;18:00;Lisbon;London");
                        System.out.println("Ex2: 2021-05-20;09:40;15:00;Lisbon;Paris;Madrid;London"+CReset);
                        System.out.print("-> ");
                        inputFields = reader.readLine().split(";");
                        if (inputFields.length >= 5 && inputFields[0].length() == 10 && inputFields[1].length() == 5 && inputFields[2].length() == 5) {
                            List<String> escalas = new ArrayList<>();
                            for (int i = 4; i + 1 < inputFields.length; i++) {
                                escalas.add(inputFields[i]);
                            }
                            SRVPacket rv = SRVPacket.reservarVoo(sessionID, inputFields[0],inputFields[1], inputFields[2], inputFields[3], escalas, inputFields[inputFields.length-1]);
                            transmitter.writePacket(rv);
                        } else{
                            op = erroInput();
                        }
                        break;

                    /** Listar Reservas */
                    case 3:
                        SRVPacket lr = SRVPacket.listaReservas(sessionID);
                        transmitter.writePacket(lr);
                        break;

                    /** Cancelar Reserva */
                    case 4:
                        System.out.println("Cancelar Reserva - Escreva os inputs no seguinte formato:");
                        System.out.println(CGreen+"IdReserva"+CReset);
                        System.out.print("-> ");
                        inputLine = reader.readLine();
                        SRVPacket cr = SRVPacket.cancelarReserva(sessionID, inputLine);
                        transmitter.writePacket(cr);
                        break;

                    /** Novo Voo */
                    case 5:
                        if(!admin) {
                            op = -1;
                            break;
                        }
                        System.out.println("Novo Voo - Escreva os inputs no seguinte formato:");
                        System.out.println(CGreen+"Origem;Destino;Hora;Capacidade"+CReset);
                        System.out.println("Hora: hh-mm. (Ex: 09:30 / 18:20)");
                        System.out.print("-> ");
                        inputFields = reader.readLine().split(";");
                        if (inputFields.length == 4 && inputFields[2].length() == 5) {
                            SRVPacket nv = SRVPacket.novoVoo(sessionID, inputFields[0], inputFields[1], inputFields[2], inputFields[3]);
                            transmitter.writePacket(nv);
                        } else{
                            op = erroInput();
                        }
                        break;

                    /** Fechar Dia */
                    case 6:
                        if(!admin){
                            op = -1;
                            break;
                        }
                        System.out.println("Fechar Dia - Escreva os inputs no seguinte formato:");
                        System.out.println(CGreen+"Data"+CReset);
                        System.out.println("Data: AAAA-MM-DD. (Ex: 2021-01-27)");
                        System.out.print("-> ");
                        inputLine = reader.readLine();
                        if (inputLine.length() == 10) {
                            SRVPacket ed = SRVPacket.encerrarDia(sessionID, inputLine);
                            transmitter.writePacket(ed);
                        } else{
                            op = erroInput();
                        }
                        break;


                    default:
                        break;
                }

                /** Verifica se a opção requere obter uma resposta do servidor */
                if (op >= 1 && op <= 6){
                    /** Caso receba um "NaoAutenticado" volta ao menu de login */
                    if (!processResponse())
                        return false;
                }

            }
            /** Erro inesperado */
            catch (IOException e){
                System.out.println("IOError: "+e);
            }

        }

        return true;
    }

    /**
     * Método para imprimir o menu principal, avalia se o cliente é admin
     * @param admin Admin
     */
    private void printMenu(boolean admin){
        System.out.println("SRV - Sistema de Reserva de Voos");
        System.out.print("Menu Principal:");
        if(admin) System.out.print(" (ADMIN)");
        System.out.println("");
        System.out.println("0 - Sair");
        System.out.println("1 - Listar Voos");
        System.out.println("2 - Reservar Voo");
        System.out.println("3 - Listar Reservas");
        System.out.println("4 - Cancelar Reserva");
        if (admin){
            System.out.println("5 - Novo Voo");
            System.out.println("6 - Encerrar Dia");
        }
        System.out.print("\nOpção: ");
    }

    /**
     * Método que pergunta ao utilizador se este prente sair da aplicação
     * @return boolean - true se pretende sair
     */
    public boolean wantToExit(){
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Do you want to exit? (Y/N)");
        String line = "";
        try{
            line = reader.readLine();
        } catch (IOException e){
            System.out.println("IO Error");
        }

        clearScreen();

        if (line.equals("Y") || line.equals("y")) return true;
        else return false;
    }

    /**
     * Método simples para premir qualquer Tecla
     */
    public void pressEnter(){
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e){
            System.out.println("IOError");
        }
    }

    /**
     * Método para limpar a consola
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Método para processar as respostas do servidor
     * @return boolean - false se a sessão foi expirada (Recebe erro "NaoAutenticado")
     */
    public boolean processResponse(){
        try {
            /** Recebe a packet */
            SRVPacket response = transmitter.readPacket();

            /** Escreve mensagem de sucesso ou erro */
            if (response.getCommand().equals(SRVPacket.Commands.Confirmacao))
                System.out.print("Operação efetuada com sucesso: ");
            else
                System.out.print("Erro ao efetuar a operação: ");


            /** Escreve o resto da informação contida na packet */
            List<String> output = response.getArgumentsList();
            for (int i = 0; i < output.size(); i++)
                if(!output.get(i).isEmpty())
                    System.out.println(output.get(i));

            System.out.println("");
            pressEnter();
            clearScreen();

            /** Caso seja um erro de sessão expirada, retorna false (Para voltar ao menu de login) */
            if (response.getCommand().equals(SRVPacket.Commands.Erro) &&
                    response.getArgumentIdx(0).equals(SRVPacket.Erros.NaoAutenticado.toString()))
                return false;

            return true;

        }
        /** Packet corrumpida */
        catch (IOException e){
            System.out.println("Erro de leitura da packet: "+e);
            /** Provavelmente o Servidor Fechou*/
            System.out.println("Provavelmente o Servidor Fechou");
            pressEnter();
            return false;
        }

        catch (InvalidSRVPacketException e){
            System.out.println("Erro de leitura da packet: "+e);
            pressEnter();
            return true;
        }
    }

    /**
     * Método para avisar de Erros
     * @return Op = -1, erro
     */
    public int erroInput(){
        System.out.println("Input incorreto");
        pressEnter();
        clearScreen();
        return -1;
    }
}
