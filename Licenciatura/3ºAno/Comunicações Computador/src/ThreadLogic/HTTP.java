import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class HTTP implements Runnable {

    public class HTTPWorker implements Runnable{

        /** Socket Cliente */
        private Socket socket;
        /** Estado das Threads da App */
        private AppStatus appStatus;

        /**
         * Construtor da Thread HTTPWorker
         * @param socket Socket do Cliente
         * @param appStatus Estado das Threads da App
         */
        public HTTPWorker(Socket socket,AppStatus appStatus) {
            this.socket = socket;
            this.appStatus = appStatus;
        }

        /**
         * Método run da Thread.
         * Abre o ficheiro HTML, envia seguindo com a informação contida no "AppStatus"
         */
        public void run() {
            try {
                OutputStream out = socket.getOutputStream();

                FileInputStream html = new FileInputStream("CC-TP2.html");
                Path path = Path.of("CC-TP2.html");

                String status = this.appStatus.toHtml()+"</div> </body> </html>";
                long size = Files.size(path) + status.length();

                out.write(("HTTP/1.1 200 OK\r\n").getBytes());
                out.write(("Content-Type: text/html\r\n").getBytes());
                out.write(("Content-Length: " +size+"\r\n").getBytes());
                out.write(("\r\n").getBytes());
                out.write(html.readAllBytes());
                out.write(status.getBytes());

                out.flush();
                out.close();
            }catch (IOException e){}

        }
    }

    /** Estado das Threads da App */
    private AppStatus appStatus;

    /**
     * Construtor da Thread
     * @param appStatus Estado das Threads da App
     */
    public HTTP(AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    /**
     * Método Run da Thread.
     * Abre a porta de atendimento TCP. Quando receber uma ligação reencaminha para o HTTPWorker
     */
    public void run() {

        appStatus.addEntry("HTTP", AppStatus.Estados.Listening,Thread.currentThread().getName(),"Atendimento pedidos HTTP");
        try{
            ServerSocket atendimento = new ServerSocket(80);
            /** LOOP ATENDIMENTO */
            while(true){
                Socket socket = atendimento.accept();
                Thread t = new Thread(new HTTPWorker(socket,appStatus));
                t.start();
            }
        } catch (IOException e){
            appStatus.updateEntry("HTTP", AppStatus.Estados.Fail);
        }

    }

}
