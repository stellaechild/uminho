import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class ServerLog {
    /** Ficheiro Log*/
    private FileWriter file;
    /** Local do printer */
    private PrintWriter print;
    /** Lock de concorrência */
    private ReentrantLock l = new ReentrantLock();
    /** Estado do ficheiro log */
    private boolean open = false;


    /** Formato da data no nome do ficheiro Log */
    private DateTimeFormatter fileDateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
    /** Formato da data na escrita de mensagens */
    private DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");

    /**
     * Construtor inicial
     */
    public ServerLog(){
        open = false;
    }

    /**
     * Inicializa o ficheiro Log. Escreve a data de abertura
     * @throws IOException IOError
     */
    public void initialize() throws IOException{
        if(open) return;
        String time = LocalDateTime.now().format(fileDateFormat);
        Path path = Paths.get("logs");
        Files.createDirectories(path);
        file = new FileWriter("logs/Log ["+time+"].txt",false);
        print = new PrintWriter(file);
        open =  true;
    }

    /**
     * Escreve uma mensagem personalizada
     * @param msg Mensagem
     */
    public void logMessage(String msg){
        if (!open) return;
        try {
            l.lock();
            print.print(Thread.currentThread().getName());
            print.println(": [" + LocalDateTime.now().format(logFormat) + "] " + msg);
            print.flush();
        } finally {l.unlock();}
    }

    /**
     * Escreve uma mensagem personalizada, escrevendo também na consola.
     * @param msg Mensagem
     */
    public void logAndPrintMessage(String msg){
        if (open){
            try {
                l.lock();
                print.print(Thread.currentThread().getName());
                print.println(": [" + LocalDateTime.now().format(logFormat) + "] " + msg);
                print.flush();
            } finally {l.unlock();}
        }
        System.out.println(msg);
    }

    /**
     * Termina o ficheiro Log
     * @throws IOException IOError
     */
    public void closeLog() throws IOException {
        print.close();
        file.close();
        open = false;
    }
}
