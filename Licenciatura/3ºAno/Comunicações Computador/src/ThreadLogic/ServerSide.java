import java.io.IOException;
import java.util.Arrays;

public class ServerSide implements Runnable{

    /** Transmissor Servidor -> Cliente */
    private PacketTransmitter transmitter;
    /** Estado das Threads da App. */
    private AppStatus appStatus;
    /** Chave de acesso ao Estado da App */
    private String statusKey;
    /** Log de registo */
    private FFSyncLog log;
    /** Round Trip Time (segundos) */
    private float rtt;
    /** MetaDados */
    private MetaData myMetada;
    /** Pasta de sincronização */
    private String folder;
    /** Número de transferências realizadas */
    private int numTransf;

    /**
     * Construtor da Thread Server.
     * @param transmitter Transmissor a ser utilizado.
     * @param folder Pasta de sincronização
     * @param app Estado das Threads da App.
     * @param log Log de registo
     */
    public ServerSide(PacketTransmitter transmitter, String folder, AppStatus app, FFSyncLog log){
        this.transmitter = transmitter;
        this.folder = folder;
        this.statusKey = AppStatus.getKey(transmitter.getResponseIP(),true,false,0);
        this.appStatus = app;
        this.log = log;
        this.numTransf = 0;
        this.myMetada = new MetaData();
    }

    /**
     * Método Run da Thread.
     * Envia uma pacote Hello.
     * Recebe pacote Hello.
     * Atendimento de pacotes [MR] e [FR].
     * Caso [MR] -> Envia metadados.
     * Caso [FR] -> Cria Thread com a informação para envio.
     */
    public void run() {
        try {
            /** INITIALIZING */
            appStatus.addEntry(statusKey, AppStatus.Estados.Initializing,Thread.currentThread().getName(),"Sending Files");
            log.logAndPrintMessage("STARTING: Sending Files to "+transmitter.getResponseIP().toString());

            /** SEND HELLO AND RECORD TIME */
            FTRP_Packet hello = FTRP_Packet.helloPacket();
            this.transmitter.sendPacket(hello);
            long sentTime = System.nanoTime();

            /** RESPONSE AND CALCULATE ROUND TRIP TIME */
            FTRP_Packet helloResp = this.transmitter.receivePacket();
            this.rtt = ((float)(System.nanoTime()-sentTime))/1000000000;
            if(!helloResp.getCommand().equals("HE")) log.logMessage("WARNING: Expected [HE]");
            log.logAndPrintMessage("INFO: Server connected with "+this.transmitter.getResponseIP()+" | Round Trip Time: "+this.rtt *1000+"(ms)");

            /** WHILE LOOP FOREVER */
            while (true) {
                appStatus.updateEntry(statusKey, AppStatus.Estados.Listening);
                FTRP_Packet response = transmitter.receivePacket();

                /** RECEIVE MR OR FR */
                if (response.getCommand().equals("MR")) {
                    sendMetadata();
                } else if (response.getCommand().equals("FR")) {
                    PacketTransmitter newTransmitter = transmitter.makeCopy();
                    String file = response.getFilename();
                    numTransf++;
                    Thread t = new Thread(new DataSender(newTransmitter, rtt, folder, file, appStatus, numTransf));
                    t.start();
                } else {
                    log.logMessage("WARNING: Expected [MR] or [FR] packet");
                }
            }
        } catch (IOException e){
            log.logMessage("ERROR:"+e);
        }

    }


    /**
     * Método para envio dos Metadados.
     * Atualiza os MetaDados, Serializa e, caso necessário, divide em vários pacotes
     * que são depois enviados pelo transmitter do Servidor.
     */
    public void sendMetadata(){
        try {
            //this.myMetada = new MetaData();
            /** GENERATE METADATA */
            this.myMetada.updateMetadata(this.folder);

            /** SERIALIZE, AND CALCULATE DIVISION */
            byte[] bytes = this.myMetada.serialize();
            int mtSize = bytes.length;
            int segs = (mtSize / 1024) + 1;

            /** DIVIDE AND SEND*/
            for (int i = 1; i <= segs; i++) {
                byte[] seg;
                if (i == segs) seg = Arrays.copyOfRange(bytes, (i - 1) * FTRP_Packet.MAXDATASIZE, mtSize);
                else seg = Arrays.copyOfRange(bytes, (i - 1) * FTRP_Packet.MAXDATASIZE, i * FTRP_Packet.MAXDATASIZE);
                FTRP_Packet data = FTRP_Packet.dataPacket(i, seg);
                transmitter.sendPacket(data,false);
            }
            log.logMessage("MetaData Sent");

        } catch (Exception e){
            log.logMessage("ERROR:"+e);
        }

    }
}
