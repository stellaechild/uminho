import Exceptions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class FTRP_Packet {

    /** Tamanho Mínimo dos pacotes */
    public static final int MINPACKETSIZE = 16;
    /** Tamanho Máximo dos pacotes */
    public static final int MAXPACKETSIZE = 1024;
    /** Tamanho Máximo dos dados dos pacotes */
    public static final int MAXDATASIZE = MAXPACKETSIZE - MINPACKETSIZE - 8;
    /** Cifra pré-calculada */
    public static final byte[] cipher = generateCipher();

    /** Comando do pacote. Dois caracteres */
    private String command = "";
    /** Tamanho do pacote */
    private short size;
    /** Sequência do pacote */
    private int seq;
    /** Ficheiro a ser pedido */
    private String filename = "";
    /** Dados do pacote */
    private byte[] data = new byte[0];

    /**
     * Construtor privado de pacotes. Este é utilizado na criação e deserialização de pacotes
     * @param command Comando
     * @param seq Sequência
     */
    private FTRP_Packet(String command,int seq){
        this.command = command;
        this.size = MINPACKETSIZE;
        this.seq = seq;
    }

    /**
     * Construtor privado de pacotes. Este é utilizado na deserealização de pacotes "Hello"
     * @param command Comando
     * @param size Tamanho
     * @param seq Sequência
     */
    private FTRP_Packet(String command,short size,int seq){
        this.command = command;
        this.size = size;
        this.seq = seq;
    }

    /**
     * Método de criação de pacotes "Hello". TimeStamp automático
     * @return Pacote Hello
     */
    public static FTRP_Packet helloPacket(){
        FTRP_Packet res = new FTRP_Packet("HE",1);
        return res;
    }

    /**
     * Método de criação de pacotes "Error".
     * @param seq Número de Sequência
     * @return Pacote Error
     */
    public static FTRP_Packet errorPacket(int seq){
        return new FTRP_Packet("ER",seq);
    }

    /**
     * Método de criação de pacotes "Acknowledgement".
     * @return Pacote Acknowledgement
     */
    public static FTRP_Packet acknowledgementPacket(){
        return new FTRP_Packet("AK",1);
    }


    /**
     * Método de criação de pacotes "MetaData Request".
     * @return Pacote MetaData Request
     */
    public static FTRP_Packet metaDataRequestPacket(){
        return new FTRP_Packet("MR",1);
    }

    /**
     * Método de criação de pacotes "File Request".
     * @param file Nome do Ficheiro a ser pedido
     * @return Pacote File Request
     * @throws InvalidPacketSizeException Nome de Ficheiro maior que 1000 caracteres
     */
    public static FTRP_Packet fileRequestPacket(String file)throws InvalidPacketSizeException{
        FTRP_Packet res = new FTRP_Packet("FR",1);
        res.filename = file;
        res.data = file.getBytes(StandardCharsets.UTF_8);
        if(MINPACKETSIZE+8+res.data.length > MAXPACKETSIZE) throw new InvalidPacketSizeException();
        res.size = (short) (MINPACKETSIZE+8+res.data.length);
        return res;
    }

    /**
     * Método de criação de pacotes de Dados.
     * @param seq Sequência
     * @param data Dados
     * @return Pacote de dados.
     * @throws InvalidPacketSizeException Dados ultrapassam os 1000bytes
     */
    public static FTRP_Packet dataPacket(int seq,byte[] data) throws InvalidPacketSizeException {
        FTRP_Packet res = new FTRP_Packet("DT",seq);
        if(data.length > MAXDATASIZE) throw new InvalidPacketSizeException();
        res.data = data.clone();
        res.size = (short) (MINPACKETSIZE+8+data.length);
        return res;
    }

    //---------------------MÉTODOS GET---------------------
    public int getSize () {return this.size;}
    public int getDataSize (){return this.size-MINPACKETSIZE-8;}
    public byte[] getData (){return this.data.clone();}
    public String getCommand(){return this.command;}
    public int getSeq() {return seq;}
    public String getFilename() {return filename;}

    /**
     * Método de serialização do pacote
     * @return Pacote Serializado
     * @throws IOException Erro I/O genérico
     */
    public byte[] serialize() throws IOException {
        /** SERIALIZE COMMAND, SIZE, AND SEQUENCE */
        ByteArrayOutputStream headerBuff = new ByteArrayOutputStream();
        headerBuff.write(this.command.getBytes(StandardCharsets.UTF_8));
        headerBuff.write(SerializeAux.serialize(size));
        headerBuff.write(SerializeAux.serialize(this.seq));

        /** ADD HEADER CHECKSUM, IF HEADER ONLY PACKET, FINISH */
        byte[] finalHeader = addChecksum(headerBuff.toByteArray());
        if(!this.command.equals("DT") && !this.command.equals("FR"))
            return cesarCipher(finalHeader,0,finalHeader.length,true);

        /** IF DATA, ADD CHECKSUM */
        byte[] finalData = addChecksum(this.data);
        ByteArrayOutputStream finalPacket = new ByteArrayOutputStream();
        finalPacket.write(finalHeader);
        finalPacket.write(finalData);
        byte[] encripted = finalPacket.toByteArray();

        /** ENCRYPT PACKET */
        encripted = cesarCipher(encripted,0,encripted.length,true);

        return encripted;
    }


    /**
     * Método que adiciona checksum a uma quantidade de dados
     * @param data Dados a serem checksummed
     * @return Novo array de bytes contendo a checksum no seu início
     * @throws IOException Erro I/O génerico
     */
    private byte[] addChecksum(byte[] data) throws IOException{
        Checksum crc32 = new CRC32();
        crc32.update(data);

        ByteArrayOutputStream packet = new ByteArrayOutputStream();
        packet.write(SerializeAux.serialize(crc32.getValue()));
        packet.write(data);
        return packet.toByteArray();
    }

    /**
     * Deserializa o cabeçalho do pacote, e devolve um pacote inicial
     * @param packetData Bytes a serem deserializados
     * @return Pacote inicial deserializado
     * @throws CorruptedHeaderException Corrupção detetada na checksum
     */
    public static FTRP_Packet deserializeHeader(byte[] packetData) throws CorruptedHeaderException {

        /** DECRYPT HEADER */
        packetData = cesarCipher(packetData,0,16,false);

        /** VALIDATE CHECKSUM */
        try{validateChecksum(packetData);}
        catch (CorruptedException e){throw new CorruptedHeaderException();}

        ByteBuffer reader = ByteBuffer.wrap(packetData);
        reader.position(8);

        /** READ COMAND, SIZE, AND SEQUENCE */
        short com = reader.getShort();
        String command = SerializeAux.deserialize(ByteBuffer.wrap(SerializeAux.serialize(com)));
        short size = reader.getShort();
        int seq = reader.getInt();

        return new FTRP_Packet(command,size,seq);
    }

    /**
     * Deserializa o resto dos dados
     * @param packetdata Bytes de Dados a serem deserializados
     * @throws CorruptedDataException Corrupção dos Dados
     */
    public void deserializeData(byte[] packetdata)throws CorruptedDataException{

        /** DECRYPT DATA */
        packetdata = cesarCipher(packetdata,16,packetdata.length,false);

        /** VALIDATE CHECKSUM */
        try{validateChecksum(packetdata);}
        catch (CorruptedException e){throw new CorruptedDataException();}

        /** READ DATA */
        int dataSize = this.size-MINPACKETSIZE-8;
        ByteBuffer buff = ByteBuffer.wrap(packetdata);
        buff.position(8);
        byte[] newdata = new byte[dataSize];
        buff.get(newdata,0,dataSize);

        /** IF FILE REQUEST, READ AS STRING */
        this.data = newdata;
        if(this.command.equals("FR")){
            this.filename = SerializeAux.deserialize(ByteBuffer.wrap(newdata));
        }
    }

    /**
     * Método que verifica a integridade dos dados.
     * @param data Dados a serem validados
     * @throws CorruptedException Corrupção detetada
     */
    private static void validateChecksum(byte[] data) throws CorruptedException {
        ByteBuffer reader = ByteBuffer.wrap(data);
        long checksum = reader.getLong();

        CRC32 crc32 = new CRC32();
        crc32.update(data,8,data.length-8);
        if(checksum != crc32.getValue()) throw new CorruptedException();
    }

    /**
     * Método para calcular os valores das cifras. Função sinusoidal
     * @return Cifra
     */
    private static byte[] generateCipher(){
        byte[] cipher = new byte[1024];

        for(int i = 0; i < cipher.length;i++){
            cipher[i] = (byte) (128 * Math.sin(i * ((Math.PI * 16)/512)));
        }

        return cipher;
    }

    /**
     * Método para encriptar e desencriptar packets. Utiliza uma cifra de cesar
     * @param arr Array a encriptar
     * @param pos Inicio da cifra
     * @param max Tamanho a encriptar
     * @param encrypt Modo encriptar
     * @return
     */
    private static byte[] cesarCipher(byte[] arr,int pos,int max,Boolean encrypt){
        if(encrypt){
            for(int i = 0;i < max;i++)
                arr[i] += cipher[pos+i];
        }
        else {
            for(int i = 0;i < max;i++)
                arr[i] -= cipher[pos+i];
        }
        return arr;
    }

    /**
     * Método toString
     * @return String Formatada.
     */
    public String toString(){
        String r = "["+this.command+"] Size:"+this.size+"";
        r+= " Seq:"+this.seq+"";
        if(this.command.equals("FR")) r+= " File:"+this.filename +"";
        return r ;
    }

}
