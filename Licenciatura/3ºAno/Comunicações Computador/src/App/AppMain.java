import Exceptions.InvalidArgumentsException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppMain {

    /**
     * Método Main da Aplicação
     * @param args Argumentos: Pasta de Sincronização ; IP's ....
     */
    public static void main(String[] args){

        AppMain main = new AppMain();
        main.run(args);
        System.out.println("Exiting...");
        System.exit(0);
        return;

    }

    /** Pasta de sincronização */
    private String folder;
    /** Log de Registo */
    private FFSyncLog log;
    /** Estado da aplicação */
    private AppStatus status;
    /** Lista de IPs permitidos */
    private List<InetAddress> ips;

    /**
     * Construtor por omissão
     */
    public AppMain(){
        this.ips = new ArrayList<>();
        this.log = new FFSyncLog();
        this.status = new AppStatus();
    }


    /**
     * Método Run da Thread. Corre a aplicação.
     * Começa por verificar a existência da pasta, de seguida verifica os IP's, depois disso estabelece as Threads de
     * atendimento HTTP e as Threads Cliente, por último, fica numa porta de atendimento onde gera Threads Servidor.
     * @param args Argumentos do programa
     */
    public void run(String[] args){

        /** VALIDATE ARGUMENTS */
        System.out.println("Validating arguments");
        try {validaArgumentos(args);}
        catch (InvalidArgumentsException e){
            System.out.println(e.getMessage());
            return;
        }

        /** VALIDATE HTML */
        File http = new File("CC-TP2.html");
        if (!http.exists()){
            System.out.println("Couldn't Find 'CC-TP2.html'");
            return;
        }

        /** INITIALIZE LOG */
        try {log.initialize();}
        catch (IOException e) {
            System.out.println("Error while making LogFile");
            System.out.println(e);
            return;
        }

        /** LOG OK */
        log.logMessage("FFSyncLog Starting");
        log.logAndPrintMessage("Arguments OK:");
        log.logAndPrintMessage("Folder: "+this.folder);
        log.logAndPrintMessage(this.ips.size()+" IP(s) detected");
        log.logAndPrintMessage("Initializing HTTP Server");

        /** START HTTP THREAD */
        Thread t = new Thread(new HTTP(status));
        t.start();

        /** CREATE CLIENT THREADS */
        log.logAndPrintMessage("Initializing Client Threads");
        for (InetAddress ip : ips){
            Thread client = new Thread(new ClientSide(ip,this.folder,status,log));
            client.start();
        }

        /** SERVICE SOCKET */
        atendimento();
    }

    /**
     * Método de validação de todos os argumentos inseridos. Cria os registos na Estado da App
     * @param args Argumentos do programa
     * @throws InvalidArgumentsException Argumentos inválidos
     */
    public void validaArgumentos(String[] args)throws InvalidArgumentsException {
        /** NUMBER OF ARGUMENTS */
        if(args.length < 2) throw new InvalidArgumentsException("Insufficient arguments");

        /** FOLDER EXISTS, AND ADJUSTS DE "/" */
        Path path = Paths.get(args[0]);
        if(!Files.exists(path)) throw new InvalidArgumentsException("Folder doesn't exist");
        args[0] = args[0].replace("\\","/");
        if(!args[0].contains("/")) args[0] += "/";
        this.folder = args[0];

        for(int i = 1;i<args.length;i++){
            try {
                /** FOR EVERY IP, ADDS A CLIENT AND SERVER STATUS */
                InetAddress ip = InetAddress.getByName(args[i]);
                ips.add(ip);

                String keyClient = AppStatus.getKey(ip,false,false,0);
                status.addEntry(keyClient, AppStatus.Estados.NotCreated,"","Receive files");
                String keyServer = AppStatus.getKey(ip,true,false,0);
                status.addEntry(keyServer, AppStatus.Estados.NotCreated,"","Send files");
            }
            catch (UnknownHostException e) {
                throw  new InvalidArgumentsException("One or more unknown hosts");
            }
        }

        return;
    }

    /**
     * Método para o atendimento prepétuo de clientes FFSyinc.
     */
    public void atendimento(){

        /** STARTS SERVICE SOCKET */
        log.logAndPrintMessage("Initializing Service Socket");
        PacketTransmitter receiver;
        try {
            receiver = new PacketTransmitter(80, log);
        } catch (SocketException e){
            System.out.println("Error while creating Service Socket");
            System.out.println("Check if you granted admin privileges");
            return;
        }

        /** SERVICE SOCKET LOOP */
        status.addEntry("Service Socket", AppStatus.Estados.Listening,Thread.currentThread().getName(),"Listening for FFSync Clients");
        while(true) {
            try {
                /** RECEIVE [HE], IF NOT IGNORE */
                FTRP_Packet hello = receiver.receivePacket();
                if (hello.getCommand().equals("HE")) {

                    /** CHECK IF RESPONSE IP IS IN THE IP LIST */
                    if (ips.contains(receiver.getResponseIP())) {
                        Thread server = new Thread(new ServerSide(receiver.makeCopy(), this.folder, status, log));
                        server.start();
                    }
                    else {
                        log.logMessage("WARNING: Unknown IP connected, ignoring...");
                    }
                } else {
                    log.logMessage("WARNING: Unexpected packet, ignoring...");
                }
            }catch (IOException e){
                log.logAndPrintMessage("ERROR: "+e);
            }
        }
    }


}
