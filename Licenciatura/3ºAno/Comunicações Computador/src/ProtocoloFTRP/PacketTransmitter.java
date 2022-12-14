import Exceptions.CorruptedDataException;
import Exceptions.CorruptedHeaderException;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class PacketTransmitter {

    /** Transmissor aberto */
    private Boolean open;
    /** Transmissor pronto para enviar pacotes */
    private Boolean readyToSend;
    /** Socket utilizada */
    private DatagramSocket socket;
    /** Ip de Destino */
    private InetAddress destIP;
    /** Porta de Destino */
    private int destPort;
    /** Log de registo */
    private FFSyncLog log;

    /**
     * Construtor de transmissor de escuta, apenas requer porta de escuta, caso receba 0, gera uma aleatória
     * @param myPort Porta Máquina
     * @param log Log de registo
     * @throws SocketException Erro de Sockets
     */
    public PacketTransmitter(int myPort,FFSyncLog log) throws SocketException{
        if (myPort == 0) this.socket = new DatagramSocket();
        else this.socket = new DatagramSocket(myPort);
        this.log = log;
        this.open = true;
        this.readyToSend = false;
    }

    /**
     * Construtor de transmissor pronto a efetuar envios.
     * @param myPort Porta Máquina
     * @param destIp Ip Destino
     * @param destPort Porta Destino
     * @param log Log de registo
     * @throws SocketException Erro de Sockets
     */
    public PacketTransmitter(int myPort, InetAddress destIp,int destPort,FFSyncLog log) throws SocketException {
        if (myPort == 0) this.socket = new DatagramSocket();
        else this.socket = new DatagramSocket(myPort);
        this.log = log;
        this.destIP = destIp;
        this.destPort = destPort;
        this.open = true;
        this.readyToSend = true;
    }

    /**
     * Método para defenir um timeout de receção.
     * @param time Tempo em segundos
     * @throws SocketException Erro de socket
     */
    public void setTimeout(float time) throws SocketException{
        this.socket.setSoTimeout((int)(time*1000));
    }

    /**
     * Método para desativar e fechar a socket
     */
    public void closeSocket(){
        socket.close();
        open = false;
        readyToSend = false;
    }

    //----------------Métodos GET--------------------
    public InetAddress getResponseIP() {return this.destIP;}
    public FFSyncLog getLog() {return log;}

    /**
     * Renaming do método seguinte, caso não receba um booleano de indicação de log, assume que grava.
     * @return Pacote Recebido
     * @throws IOException Socket Fechado ou Timeout excedido
     */
    public FTRP_Packet receivePacket() throws IOException{
        return receivePacket(true);
    }

    /**
     * Método de receção de pacotes. Este ignora todos pacotes corrumpidos que forem lidos, devolvendo apenas o primeiro
     * pacote não corrumpido encontrado. Depois de um pacote encontrado, redireciona o transmissor de forma a que respostas
     * sejam efetuadas ao emissor do pacote.
     * @return Pacote Recebido
     * @throws IOException Socket Fechado ou Timeout excedido
     */
    public FTRP_Packet receivePacket(Boolean logReport) throws IOException {
        if(!open) throw new IOException();

        boolean dataReadSuccessfully = false;
        FTRP_Packet packet = null;

        while (!dataReadSuccessfully) {
            byte[] packetBytes = new byte[1024];
            boolean headerReadSuccessfully = false;

            while (!headerReadSuccessfully) {
                /** RECEIVE PACKET */
                DatagramPacket inPacket = new DatagramPacket(packetBytes, packetBytes.length);
                socket.receive(inPacket);

                byte[] header = new byte[16];
                ByteBuffer.wrap(packetBytes).get(header,0,16);

                /** DESERIALIZE HEADER, IF SUCCESSFULL, REROUTE RESPONSE PORT AND IP */
                try {
                    packet = FTRP_Packet.deserializeHeader(header);
                    headerReadSuccessfully = true;
                    this.destPort = inPacket.getPort();
                    this.destIP = inPacket.getAddress();
                    this.readyToSend = true;
                } catch (CorruptedHeaderException e) {
                    log.logMessage("ERROR: Corrupted Header Found");
                }
            }

            /** IF THERE IS DATA, DESERIALIZE IT */
            if (packet.getSize() > 16) {
                int datasize = packet.getDataSize()+8;
                byte[] data = new byte[datasize];
                ByteBuffer.wrap(packetBytes).position(16).get(data,0,datasize);

                try {
                    packet.deserializeData(data);
                    dataReadSuccessfully = true;
                } catch (CorruptedDataException e) {
                    log.logPacket(FFSyncLog.Status.Corrupted,packet);
                }

            }
            else dataReadSuccessfully = true;
        }

        if(logReport) log.logPacket(FFSyncLog.Status.Received,packet);
        return packet;
    }

    /**
     * Renaming do método seguinte, caso não receba um booleano de indicação de log, assume que grava.
     * @param packet Packet a ser enviada.
     * @throws IOException I/O error
     */
    public void sendPacket(FTRP_Packet packet) throws IOException{
        sendPacket(packet,true);
    }

    /**
     * Envia um pacote pelo transmissor.
     * @param packet Pacote a ser enviado
     * @throws IOException Transmissor não preparado para envio
     */
    public void sendPacket(FTRP_Packet packet,Boolean logReport) throws IOException{
        if(!readyToSend) throw new IOException();
        byte[] bytes = packet.serialize();
        DatagramPacket outPacket = new DatagramPacket(bytes,bytes.length, this.destIP,this.destPort);
        socket.send(outPacket);
        if (logReport) log.logPacket(FFSyncLog.Status.Sent,packet);
    }

    /**
     * Método para criar cópias do transmissor, estes retém a informação de destino, mas utilizam uma socket diferente.
     * Utilizada para quando um transmissor recetor pretende efetuar um transmissor para resposta.
     * @return Novo Transmissor
     * @throws SocketException Socket Error
     */
    public PacketTransmitter makeCopy() throws SocketException{
        if(!readyToSend) return new PacketTransmitter(0,this.log);
        return new PacketTransmitter(0,this.destIP,this.destPort,this.log);
    }

}
