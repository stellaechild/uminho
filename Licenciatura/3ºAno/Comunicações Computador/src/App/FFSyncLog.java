import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class FFSyncLog {
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
    private DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("mm:ss.nnnnnn");

    /** Mensagens possíveis de estado para as packets */
    public enum Status{Received,Sent,Corrupted}

    /**
     * Construtor inicial
     */
    public FFSyncLog(){
        open = false;
    }

    /**
     * Inicializa o ficheiro Log. Escreve a data de abertura
     * @throws IOException IOError
     */
    public void initialize() throws IOException{
        if(open) return;
        String time = LocalDateTime.now().format(fileDateFormat);
        file = new FileWriter("Log ["+time+"].txt",false);
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
     * Escreve no log o estado da packet, se esta foi recebida ou enviada e a sua informação
     * @param status Estado Recebida ou Enviada
     * @param packet Packet a ler informação
     */
    public void logPacket(Status status,FTRP_Packet packet){
        if (!open) return;
        try {
            l.lock();
            print.print(Thread.currentThread().getName());
            print.print(": [" + LocalDateTime.now().format(logFormat) + "] ");
            print.println(status.toString() + ": " + packet.toString());
            print.flush();
        } finally{l.unlock();}
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
