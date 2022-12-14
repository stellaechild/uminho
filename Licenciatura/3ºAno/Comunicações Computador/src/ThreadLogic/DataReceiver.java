import Exceptions.InvalidPacketSizeException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class DataReceiver implements Runnable{

    /** Ficheiro a ser pedido */
    private String file = "";
    /** Data modificação do file */
    private String fileDate = "";
    /** Pasta onde guardar o ficheiro */
    private String folder = "";
    /** Pacotes de dados recebidos */
    private Map<Integer, FTRP_Packet> packets;
    /** Sequência do pacote mais recente recebido */
    private int doneUpTo = 0;
    /** Lista de Sequências de pacotes em falta */
    private List<Integer> missing;
    /** Transmissor a ser usao */
    private PacketTransmitter transmitter;
    /** Log de registo */
    private FFSyncLog log;
    /** Tempo de transmissão */
    private float rtt;
    /** Estado das Threads da App */
    private AppStatus appStatus;
    /** Número da Transferência */
    private int numTransf;
    /** NanoSegundos do inicio da transferência */
    private long transfStartTime;

    /**
     * Construtor da Thread.
     * @param transmitter Transmissor a ser utilizado.
     * @param rtt Tempo de transmissão
     * @param folder Pasta onde o ficheiro é guardado
     * @param file Ficheiro a ser pedido.
     * @param fileDate Data do ficheiro pedido.
     * @param appstatus Estado das Threads da App
     * @param numTransf Número da Transferência
     */
    public DataReceiver(PacketTransmitter transmitter, float rtt, String folder, String file, String fileDate, AppStatus appstatus, int numTransf){
        this.transmitter = transmitter;
        this.folder = folder;
        this.file = file;
        this.fileDate = fileDate;
        this.rtt = rtt;
        this.log = transmitter.getLog();
        this.packets = new LinkedHashMap<>();
        this.doneUpTo = 0;
        this.appStatus = appstatus;
        this.numTransf = numTransf;
    }

    /**
     * Método Run da thread.
     * Começa por enviar um "File Request".
     * De seguida acumula todos os pacotes de Data que receber.
     * Quando receber o pacote final, avalia os pacotes em falta e pede reetransmissões.
     */
    public void run() {
        /**INITIALIZAING */
        String statusKey = AppStatus.getKey(this.transmitter.getResponseIP(),false,true,numTransf);
        try {
            appStatus.addEntry(statusKey, AppStatus.Estados.Initializing,Thread.currentThread().getName(),"Get file "+file);
            log.logMessage("STARTING: Fetching file "+file);
            int ntry = 1;

            /** SEND FILE REQUEST */
            FTRP_Packet fileRequest = FTRP_Packet.fileRequestPacket(this.file);
            transmitter.sendPacket(fileRequest);

            /** FIRST PHASE LOOP, INTERRUPTED WHEN RECEIVES LAST DATA PACKET */
            appStatus.updateEntry(statusKey, AppStatus.Estados.ReceivingData);
            Boolean finished = false;
            while (!finished) {
                try {
                    /** RECEIVE DATA PACKETS */
                    if (doneUpTo == 0) transmitter.setTimeout(8);
                    else transmitter.setTimeout((float) 0.5);
                    finished = addPacket(false);
                    ntry = 1;
                }
                catch (IOException e) {
                    if (ntry >= 3) {
                        /** AFTER 3 TRIES, LEAVES */
                        log.logMessage("ERROR: No Response... Leaving");
                        appStatus.updateEntry(statusKey, AppStatus.Estados.Fail);
                        return;
                    } else if (doneUpTo == 0) {
                        /** RESEND FILEREQUEST */
                        transmitter.sendPacket(fileRequest);
                    } else {
                        /** REQUESTING LAST DATA PACKET */
                        FTRP_Packet error = FTRP_Packet.errorPacket(0);
                        transmitter.sendPacket(error);
                    }
                    ntry++;
                }
            }

            /** SECOND PHASE LOOP, INTERRUPTED WHEN 0 PACKETS MISSING */
            while (true) {
                /** CHECKS MISSING PACKETS */
                checkMissing();
                if (missing.size() > 0) {
                    log.logMessage(missing.size() + " Packets Missing, Requesting Retransmission");
                    appStatus.updateEntry(statusKey, AppStatus.Estados.Retransmission);
                }
                if (this.missing.isEmpty()) break;

                /** SEND ERROR PACKETS */
                int packetChunks = 0;
                for (Integer idMiss : this.missing) {
                    FTRP_Packet error = FTRP_Packet.errorPacket(idMiss);
                    transmitter.sendPacket(error,false);

                    packetChunks++;
                    if(packetChunks >= 16 || this.missing.size() < 16) {
                        /** RECEIVE DATA PACKETS */
                        for (int i = 0; i < packetChunks; i++) {
                            try {
                                transmitter.setTimeout((float) 0.05);
                                addPacket(true);
                            } catch (IOException e) {
                                break;
                            }
                        }
                        packetChunks = 0;
                    }
                }
            }

            /** ALL PACKETS ARRIVES, SEND ACKNOWLEDGEMENT */
            FTRP_Packet ack = FTRP_Packet.acknowledgementPacket();
            transmitter.sendPacket(ack,false);
            log.logMessage("Received "+packets.size()+" data packets");
            calculateAndLogTR();
            /** MOUNT FILE */
            mountFile();
            log.logMessage("FINISHED: File '"+file+"' received successfully");
            appStatus.updateEntry(statusKey, AppStatus.Estados.Success);
            transmitter.closeSocket();
            return;

        }catch (IOException| InvalidPacketSizeException e){
            appStatus.updateEntry(statusKey, AppStatus.Estados.Fail);
            log.logMessage("ERROR: "+e);
        }
    }

    /**
     * Método para adição de pacotes de dados no map
     * @param missingMode Se a thread está adicionar pacotes retransmitidos
     * @return Verdadeiro se pacote adicionado é o último, ou seja, size < 1024
     * @throws IOException Erro I/O
     */
    private Boolean addPacket(Boolean missingMode)throws IOException{
        FTRP_Packet response = transmitter.receivePacket(false);

        if (response.getCommand().equals("DT")) {
            /** ADD PACKET TO MAP */
            int seq = response.getSeq();
            this.packets.put(seq, response);
            if(doneUpTo == 0) transfStartTime = System.nanoTime();               /** IF FIRST DATA PACKET, START TRANSFER TIMER */
            if(!missingMode) {
                doneUpTo = seq;                                                  /** UPDATE DONEUPTO SEQ */
                if (response.getSize() < FTRP_Packet.MAXPACKETSIZE) return true; /** IF SIZE < 1024, SIGNAL LAST PACKET */
            }
        } else {log.logMessage("ERROR: Expected Data Packet");}

        return false;
    }

    /**
     * Verifica todos os pacotes em falta. Percorre o Map de pacotes e devolve numa lista a sequência dos pacotes em falta.
     */
    private void checkMissing(){
        this.missing = new ArrayList<>();
        for(int i = this.doneUpTo; i > 0;i--){
            if(!this.packets.containsKey(i))
                this.missing.add(i);
        }
    }

    /**
     * Método para montar o ficheiro final.
     * @throws IOException Erro de I/O
     */
    private void mountFile()throws IOException {
        /** GET FULL PATH, CREATE NECESSARY DIRECTORIES, IF ON WINDOWS REPLACE "/" WITH "\" */
        String fileFullPath = ""+this.folder+""+this.file;
        createDirectory(fileFullPath);
        if (System.getProperty("os.name").toLowerCase().contains("win")){
            fileFullPath = fileFullPath.replace("/","\\");
        }

        /** WRITES TO FILE */
        FileOutputStream file = new FileOutputStream(fileFullPath);
        for(int i = 1;i <= doneUpTo; i++){
            file.write(this.packets.get(i).getData());
        }
        file.flush();
        file.close();

        /** PROCESS FILE DATE */
        String[] aux = this.fileDate.split("_");
        String[] dataBig = aux[0].split("-");
        String[] dataSmall = aux[1].split(":");
        int[] dataFinal = new int[6];
        for(int i = 0;i < 6;i++){
            int val;
            if (i>=3) val = Integer.parseInt(dataSmall[i-3]);
            else val = Integer.parseInt(dataBig[i]);
            dataFinal[i] = val;
        }

        /** CHANGES FILE METADATA (LAST MODIFIED TIME) */
        LocalDateTime dataLocal = LocalDateTime.of(dataFinal[0],dataFinal[1],dataFinal[2],dataFinal[3],dataFinal[4],dataFinal[5]);
        Instant instant = dataLocal.toInstant(ZoneOffset.UTC);
        Files.setLastModifiedTime(Path.of(fileFullPath), FileTime.from(instant));
    }

    /**
     * Método para criação das pastas e subpastas de um ficheiro.
     * EX: pasta1/pasta2/pasta3/ficheiro. -> cria pasta2/pasta3/
     * @param fileFullPath Path do ficheiro
     * @throws IOException I/O Error
     */
    private void createDirectory(String fileFullPath)throws IOException{
        String[] splitted = fileFullPath.split("/");
        String folderPath = "";
        for(int i = 0;i+1 < splitted.length;i++){
            folderPath += splitted[i];
            if(i+2 < splitted.length){
                /** IF ON WINDOWS, USE "\" */
                if (System.getProperty("os.name").toLowerCase().contains("win")) folderPath += "\\";
                else folderPath += "/";
            }

        }

        /** CREATE DIRECTORIES */
        Path path = Paths.get(folderPath);
        Files.createDirectories(path);
    }

    /**
     * Função que calcula a rate de transferência do ficheiroe e grava no log.
     * A rate calculada será a rate média efetiva, ou seja, divide o tamanho da informação recebida
     * pelo tempo passado desde o receção do primeiro pacote de data até todos os pacotes serem transmitidos.
     * Resultado em MiB/s ou KiB/s.
     */
    private void calculateAndLogTR(){
        /** TIME END OF TRANSMISSION */
        long transfTimeNano = System.nanoTime() - transfStartTime;
        double transfTimeSec = ((double)transfTimeNano) / 1000000000;

        /** GET TRANSFERED FILE SIZE IN MBYTES */
        long dataBytes = (packets.size() - 1) * FTRP_Packet.MAXDATASIZE;
        dataBytes += packets.get(doneUpTo).getDataSize();
        double mbBytes = ((double) dataBytes) / 1024 / 1024;

        /** CALCULATE TRANSFER RATE*/
        double transfRTMBS = mbBytes/transfTimeSec;

        /** PREPARE STRING, CONVERT IF NECESSARY */
        String show = "Transfer completed! Time: ";

        if(transfTimeSec < 1) show+= String.format("%.3f",transfTimeSec*1000)+"(ms)";
        else show +=  String.format("%.3f",transfTimeSec)+"(s)";

        show += " | Amount: "+dataBytes+" bytes";

        if (transfRTMBS < 1) show+= " | Rate: "+String.format("%.3f",transfRTMBS*1000*8)+" kbps -> "+String.format("%.3f", transfRTMBS*1000)+" KiB/s";
        else show += " | Rate: "+String.format("%.3f",transfRTMBS*8)+" mbps -> "+String.format("%.3f", transfRTMBS)+"MiB/s";

        log.logMessage(show);
        return;
    }
}
