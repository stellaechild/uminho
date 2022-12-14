import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    /** Porta default do servidor */
    private final short defaultPort = 8888;
    /** Caminho default do ficheiro de Voos */
    private final String flightsPath = "Flights.csv";
    /** Caminho default do ficheiro de Utilizadores */
    private final String usersPath = "Users.csv";
    /** Registo de Utilizadores */
    private UserRegistry userRegistry;
    /** Registo de Voos e Reservas */
    private SRVRegistry srvRegistry;
    /** Registo de Log */
    private ServerLog log;

    public static void main(String[] args){
        try {
            ServerMain main = new ServerMain();
            System.out.println("Initialization complete");
            main.run();
        } catch (IOException e){
            System.out.println("Error: "+e);
        }
    }

    /**
     * Construtor que initializa o registo de voos e registo de utilizadores.
     * @throws IOException Erro de IO
     */
    public ServerMain() throws IOException {
        this.log = new ServerLog();
        this.log.initialize();
        this.log.logAndPrintMessage("Log Started");
        this.log.logAndPrintMessage("Initialzing UserRegistry: '"+usersPath+"'");
        userRegistry = new UserRegistry("Users.csv");
        this.log.logAndPrintMessage("Initizaling FlightRegistry: '"+flightsPath+"'");
        srvRegistry = new SRVRegistry(flightsPath);
    }

    /**
     * Método run. Faz atendimento de clientes e gera um Server Worker por cada cliente.
     * @throws IOException Socket Error
     */
    public void run() throws IOException{
        ServerSocket mainSocket = new ServerSocket(defaultPort);

        while (true){
            Socket socket = mainSocket.accept();
            Thread t = new Thread(new ServerWorker(socket,srvRegistry,userRegistry,log));
            t.start();
        }
    }
}
