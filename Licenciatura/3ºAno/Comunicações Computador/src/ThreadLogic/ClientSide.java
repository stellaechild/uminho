import Exceptions.MetaDataFailException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSide implements Runnable {

    /** IP do servidor */
    private InetAddress destIp;
    /** Transmissor a ser usado */
    private PacketTransmitter transmitter;
    /** Estado das Threads da App */
    private AppStatus appStatus;
    /** Chave de acesso ao Estado da App */
    private String statusKey;
    /** Log de Registo */
    private FFSyncLog log;
    /** Round Trip Time (segundos) */
    private float rtt;
    /** MetaData */
    private MetaData myMetada;
    /** Pasta de sincronização */
    private String folder;
    /** Número de tranferências */
    private int numTransf;

    /**
     * Construtor da Thread
     * @param destIp Ip do Servidor
     * @param folder Pasta de Sincronização
     * @param app Estado das Threads da App
     * @param log Log de Registo
     */
    public ClientSide(InetAddress destIp,String folder,AppStatus app,FFSyncLog log){
        this.destIp = destIp;
        this.folder = folder;
        this.statusKey = AppStatus.getKey(destIp,false,false,0);
        this.appStatus = app;
        this.log = log;
        this.numTransf = 0;
        this.myMetada = new MetaData();
    }

    /**
     * Método de Run da Thread.
     * Esta começa por tentar estabelecer comunicação com o servidor, através da porta de atendimento.
     * Depois de estabelicida a comunicção, entra num loop onde pede os MetaDados do servidor, compara com os seus
     * e efetua pedidos de ficheiros necessários.
     */
    public void run(){
        /** INITIALIZING */
        appStatus.addEntry(statusKey, AppStatus.Estados.Connecting,Thread.currentThread().getName(),"Fetching Files");
        log.logAndPrintMessage("STARTING: Trying to connect with "+destIp.toString());
        /** ESTABLISH CONNECTION */
        establishConnection();
        log.logAndPrintMessage("INFO: Client connected with "+destIp.toString()+" | Round Trip Time: "+this.rtt *1000+"(ms)");

        /** WHILE LOOP */
        while (true) {
            int ntry = 0;
            appStatus.updateEntry(statusKey, AppStatus.Estados.Working);
            MetaData serverMetadata = new MetaData();
            Boolean success = false;
            while (!success) {
                try {
                    /** GET SERVER METADATA AND GENERATE OWN METADATA */
                    serverMetadata = getServerMetaData();
                    myMetada.updateMetadata(folder);
                    success = true;
                } catch (Exception e) {
                    /** METADATA ERROR */
                    log.logMessage("ERROR: "+e);
                    try {
                        ntry++;
                        if (ntry >= 3) Thread.sleep(14000);
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            /** OBTAIN MISSING FILES NAME */
            List<String> files = myMetada.compareMetadata(serverMetadata);
            int numFiles = files.size();
            Thread[] threads = new Thread[numFiles];
            int idx = 0;
            if(files.isEmpty()) log.logMessage("Nothing to Request... Sleeping (8s)");

            /** FOR EACH MISSING FILE, CREATE A DATARECEIVER THREAD */
            for (String file : files) {
                try{
                    PacketTransmitter trans = this.transmitter.makeCopy();
                    numTransf++;
                    String dataFile = serverMetadata.getDataFile(file);
                    threads[idx] = new Thread(new DataReceiver(trans, this.rtt, folder, file,dataFile, appStatus, numTransf));
                    threads[idx].start();
                    idx++;
                } catch (SocketException e){log.logMessage("WARNING: SocketError while requesting file");}
            }

            try {
                /** WAIT FOR THREADS */
                appStatus.updateEntry(statusKey, AppStatus.Estados.Updating);
                for (int i = 0; i < numFiles; i++) {
                    threads[i].join();
                }

                /** SLEEP */
                appStatus.updateEntry(statusKey, AppStatus.Estados.Sleeping);
                Thread.sleep(8000);

            } catch (InterruptedException e){}


        }
    }


    /**
     * Método para estabelecer a comunicação.
     * Envia pacotes [HE] para a porta de atendimento.
     */
    public void establishConnection(){
        try {
            this.transmitter = new PacketTransmitter(0, destIp, 80, log);
            while (true) {
                try {
                    /** SEND HELLO PACKET */
                    FTRP_Packet hello = FTRP_Packet.helloPacket();
                    this.transmitter.sendPacket(hello);
                    long sentTime = System.nanoTime();
                    /** WAIT FOR RESPONSE */
                    this.transmitter.setTimeout(6);
                    FTRP_Packet response = this.transmitter.receivePacket();
                    if (response.getCommand().equals("HE")) {
                        /** IF ANSWERED, SEND HELLO AGAIN */
                        rtt = ((float)(System.nanoTime()-sentTime)) / 1000000000;
                        this.transmitter.sendPacket(hello);
                        break;
                    } else log.logMessage("WARNING: Expected [HE] Packet");
                } catch (IOException e) {
                    /** TIMED OUT */
                    log.logMessage("INFO: No answer, sleeping... (6s)");
                }
            }

        } catch (IOException e) {
            log.logMessage("ERROR: "+e);
        }
    }

    /**
     * Método para receber os MetaDados do Servidor.
     * @return MetaDados do Servidor
     * @throws MetaDataFailException Erro na receção de dados
     */
    public MetaData getServerMetaData() throws MetaDataFailException{
        try {
            /** SEND METADATA REQUEST */
            FTRP_Packet metadataRequest = FTRP_Packet.metaDataRequestPacket();
            this.transmitter.sendPacket(metadataRequest,false);
            log.logMessage("Fetching Server Metadata");

            Map<Integer, FTRP_Packet> packets = new HashMap<>();
            int pckSize = 1024;

            /** RECEIVE DATA PACKETS */
            while (pckSize >= 1024) {
                this.transmitter.setTimeout(5);
                FTRP_Packet data = this.transmitter.receivePacket(false);
                packets.put(data.getSeq(), data);
                pckSize = data.getSize();
            }

            /** JOIN THEM AND DESERIALIZE */
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            for (int i = 1; i <= packets.size(); i++) {
                buff.write(packets.get(i).getData());
            }
            return MetaData.deserialize(buff.toByteArray());
        }
        catch (IOException e) {
            throw new MetaDataFailException(e.getMessage());
        }
    }

}
