import Exceptions.InvalidPacketSizeException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataSender implements Runnable{

    /** Ficheiro a ser enviado */
    private String file = "";
    /** Pasta de Origem */
    private String folder = "";
    /** Transmissor a ser usado */
    private PacketTransmitter transmitter;
    /** Log de Registo */
    private FFSyncLog log;
    /** Tempo de Transmissão */
    private float tt;
    /** Estado das Threads da App */
    private AppStatus appStatus;
    /** Número da Transferência */
    private int numTransf;

    /**
     * Construtor da Thread
     * @param transmitter Transmissor a ser usado
     * @param rtt Round Trip TIme
     * @param folder Pasta de sincronização
     * @param file Ficheiro a ser enviado
     * @param appstatus Estado das Threads da App
     * @param numTransf Número da Transferência
     */
    public DataSender(PacketTransmitter transmitter, float rtt, String folder, String file,AppStatus appstatus,int numTransf){
        this.transmitter = transmitter;
        this.tt = rtt;
        this.folder = folder;
        this.file = file;
        this.log = transmitter.getLog();
        this.appStatus = appstatus;
        this.numTransf = numTransf;
    }

    /**
     * Método Run da Thread.
     * Thread reparte o ficheiro em pacotes e envia estes pelo transmissor fornecido.
     * No fim espera por um [AK] de finalização ou um [ER] para retransmissão.
     */
    public void run(){
        /** INITIALIZING */
        String statusKey = AppStatus.getKey(this.transmitter.getResponseIP(),true,true,numTransf);
        try {
            appStatus.addEntry(statusKey, AppStatus.Estados.Initializing,Thread.currentThread().getName(),"Send file "+file);
            log.logMessage("STARTING: Sending file "+file);

            /** PARSING FILE */
            Map<Integer, FTRP_Packet> fileParsed = parseFile();

            int packetChunks = 0;
            appStatus.updateEntry(statusKey, AppStatus.Estados.SendingData);
            log.logMessage("Sending "+fileParsed.size()+" packets...");

            /** SEND DATA */
            for(int i = 1;i <= fileParsed.size();i++){
                transmitter.sendPacket(fileParsed.get(i),false);
                packetChunks++;
                if (packetChunks >= 16){ /** EVERY 16 PACKETS, WAIT 1.5 MILLISECONDS */
                    try{Thread.sleep(1);} catch (InterruptedException e){}
                    packetChunks = 0;
                }
            }

            /** RETRANSMISSION PHASE */
            appStatus.updateEntry(statusKey, AppStatus.Estados.Retransmission);
            Boolean done = false;
            int errs = 0;
            while (!done) {
                try {
                    /** WAIT FOR RESPONSE */
                    transmitter.setTimeout(10);
                    FTRP_Packet response = transmitter.receivePacket(false);

                    /** IF [AK] DONE, IF [ER] RETRANSMIT */
                    if (response.getCommand().equals("AK")) done = true;
                    else if (response.getCommand().equals("ER")) {
                        errs++;
                        int seq = response.getSeq();
                        if (seq == 0) seq = fileParsed.size();
                        transmitter.sendPacket(fileParsed.get(seq),false);
                    } else {
                        log.logMessage("WARNING: Unexpected packet ["+response.getCommand()+"], Ignoring");
                    }
                } catch (IOException e){
                    /** TIMEOUT - LEAVE */
                    appStatus.updateEntry(statusKey, AppStatus.Estados.NoConfirmation);
                    log.logMessage("WARNING: File sent, but didn't receive AK or ER. Leaving...");
                    return;
                }
            }

            /** FILE SENT SUCCESSFULLY */
            if (errs > 0) log.logMessage("Retransmitted "+errs+" packets");
            appStatus.updateEntry(statusKey, AppStatus.Estados.Success);
            log.logMessage("FINISHED: File '"+file+"' sent successfully");
            transmitter.closeSocket();
            return;

        } catch (IOException|InvalidPacketSizeException e){
            appStatus.updateEntry(statusKey, AppStatus.Estados.Fail);
            log.logMessage("ERROR: "+e);
            return;
        }
    }

    /**
     * Método de partição de um ficheiro nos seus pacotes. Devolve um map com todos os pacotes a serem enviados.
     * @return Mapa com pacotes prontos a serem enviados
     * @throws IOException Erro de I/O
     * @throws InvalidPacketSizeException Erro de tamanho da divisão
     */
    public Map<Integer, FTRP_Packet> parseFile() throws IOException, InvalidPacketSizeException {
        Map<Integer, FTRP_Packet> parse = new HashMap<>();

        /** GET FILE PATH, IF ON WINDOWS REPLACE "/" WITH "\" */
        String filePlace = ""+this.folder+""+this.file;
        if (System.getProperty("os.name").toLowerCase().contains("win")){
            filePlace = filePlace.replace("/","\\");
        }

        FileInputStream fd = new FileInputStream(filePlace);

        int n = 1;
        Boolean done = false;
        while(!done){
            /** READ MAXDATASIZE BYTES AND CREATE DATA PACKET */
            byte[] data = fd.readNBytes(FTRP_Packet.MAXDATASIZE);
            FTRP_Packet packet = FTRP_Packet.dataPacket(n,data);
            parse.putIfAbsent(n,packet);
            n++;
            if(data.length < FTRP_Packet.MAXDATASIZE) done = true;
        }

        fd.close();
        return parse;
    }
}
