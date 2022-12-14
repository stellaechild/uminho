import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SRVPacket {

    /** Session ID */
    private String idSession;
    /** Packet Command */
    private SRVPacket.Commands command;
    /** Arguments */
    private List<String> arguments;

    /** List of commands */
    public enum Commands{
        Login,ListarVoos,ReservarVoo,CancelarReserva,ListarReservas,
        NovoVoo,EncerrarDia,
        Confirmacao,Erro
    }

    /** List of error messages*/
    public enum Erros{
        ErroGenerico,NaoAutenticado,SemVoos,SemPermissao
    }

    /**
     * Private Constructor, using Session ID
     * @param idSession Session ID
     */
    private SRVPacket(String idSession){
        this.idSession = idSession;
        this.arguments = new ArrayList<>();
    }

    /**
     * User packet, Login Request
     * @param name User Name
     * @param password User Password
     * @return Resulting Packet
     */
    public static SRVPacket login(String name,String password){
        SRVPacket packet = new SRVPacket("Nxxxxx");
        packet.command = Commands.Login;
        packet.arguments.add(name);
        packet.arguments.add(password);
        return packet;
    }

    /**
     * User packet, Flight List Request
     * @param idSession Session ID
     * @param origem Origin
     * @param destino Destination
     * @return Resulting Packet
     */
    public static SRVPacket listaVoos(String idSession,String origem,String destino){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.ListarVoos;
        packet.arguments.add(origem);
        packet.arguments.add(destino);
        return packet;
    }

    /**
     * User packet, cancelation of a Reservation
     * @param idSession Session ID
     * @param idReserva Reservation ID
     * @return Resulting Packet
     */
    public static SRVPacket cancelarReserva(String idSession,String idReserva){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.CancelarReserva;
        packet.arguments.add(idReserva);
        return packet;
    }

    /**
     * User packet, Reservation Request
     * @param idSession Session ID
     * @param data Reservation Date
     * @param horaInicial Starting Hour
     * @param horaFinal Finishing Hour
     * @param origem Origin
     * @param escalas Possible MidPoints
     * @param destino Destination
     * @return Resulting Packet
     */
    public static SRVPacket reservarVoo(String idSession,String data,String horaInicial,String horaFinal,String origem,List<String> escalas,String destino){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.ReservarVoo;
        packet.arguments.add(data);
        packet.arguments.add(horaInicial);
        packet.arguments.add(horaFinal);
        packet.arguments.add(origem);
        for(int i=0;escalas != null && i < escalas.size();i++)
            packet.arguments.add(escalas.get(i));
        packet.arguments.add(destino);
        return packet;
    }

    /**
     * User packet, List all User Reservations Request
     * @param idSession Session ID
     * @return Resulting Packet
     */
    public static SRVPacket listaReservas(String idSession){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.ListarReservas;
        return packet;
    }

    /**
     * Admin packet, requesting to add a flight
     * @param idSession Session ID
     * @param origem Origin
     * @param destino Destination
     * @param capacidade Flight Capacity
     * @return Resulting Packet
     */
    public static SRVPacket novoVoo(String idSession,String origem,String destino,String capacidade,String hora){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.NovoVoo;
        packet.arguments.add(origem);
        packet.arguments.add(destino);
        packet.arguments.add(capacidade);
        packet.arguments.add(hora);
        return packet;
    }

    /**
     * Admin packet, requesting to close reservations of a particular day
     * @param idSession Session ID
     * @param dia Day to close reservations
     * @return Resulting Packet
     */
    public static SRVPacket encerrarDia(String idSession,String dia){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.EncerrarDia;
        packet.arguments.add(dia);
        return packet;
    }

    /**
     * Server Packet, Confirm command
     * @param idSession Session ID
     * @param confirmacao Command which was confirmed
     * @param output Possible outputs
     * @return Resulting Packet
     */
    public static SRVPacket confirma(String idSession,SRVPacket.Commands confirmacao,List<String> output){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.Confirmacao;
        packet.arguments.add(confirmacao.toString());
        for(int i=0;output != null && i < output.size();i++)
            packet.arguments.add(output.get(i));
        return packet;
    }

    /**
     * Server Packet, Error command
     * @param idSession Session ID
     * @param erro Error type
     * @return Resulting Packet
     */
    public static SRVPacket erro(String idSession,SRVPacket.Erros erro,String infoErro){
        SRVPacket packet = new SRVPacket(idSession);
        packet.command = Commands.Erro;
        packet.arguments.add(erro.toString());
        packet.arguments.add(infoErro);
        return packet;
    }


    /**
     * Get Method. Returns the packet Command
     * @return Packet Command
     */
    public Commands getCommand() {
        return command;
    }

    /**
     * Get Method. Returns the packet IdSession
     * @return IdSession
     */
    public String getIdSession() {
        return idSession;
    }

    /**
     * Returns the list of arguments
     * @return List of packet Arguments
     */
    public List<String> getArgumentsList(){
        return this.arguments.stream().collect(Collectors.toList());
    }

    /**
     * Returns the argument at the specified index
     * @param idx Argument Index
     * @return Argument
     */
    public String getArgumentIdx(int idx){
        if (idx >= this.arguments.size() ) return "";
        else return this.arguments.get(idx);
    }

    /**
     * Method Used to serialize the SRVPacket
     * @return byte array serialized
     * @throws IOException Error on writing
     */
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();

        /** WRITE SESSION ID */
        buff.write(idSession.getBytes(StandardCharsets.UTF_8));
        buff.write("\n".getBytes(StandardCharsets.UTF_8));

        /** WRITE COMMAND */
        buff.write(command.toString().getBytes(StandardCharsets.UTF_8));
        buff.write("\n".getBytes(StandardCharsets.UTF_8));

        /** WRITE ARGUMETNS, IF LAST DOES'NT WRITE THE FINAL \n */
        for(int i = 0;i< arguments.size();i++){
            buff.write(arguments.get(i).getBytes(StandardCharsets.UTF_8));
            if (i+1 < arguments.size()) buff.write("\n".getBytes(StandardCharsets.UTF_8));
        }

        return buff.toByteArray();
    }

    /**
     * Method Used to deserialize the packet data
     * @param data Byte array containing the information
     * @return SRVPacket
     * @throws InvalidSRVPacketException Error on reading the data
     */
    public static SRVPacket deserialize(byte[] data) throws InvalidSRVPacketException{
        /** READ ALL BYTES AND SPLITS */
        String[] fields = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(data)).toString().split("\n",-1);

        /** CHECK FIELD LENGTH AND SESSION ID SIZE */
        if(fields.length < 3 /*|| fields[0].length() != 6  (NAO SEI SE QUERO ISTO) */) throw new InvalidSRVPacketException();

        /** NEW PACKET, READ COMMAND */
        SRVPacket packet = new SRVPacket(fields[0]);
        try{
            packet.command = Commands.valueOf(fields[1]);
        } catch (IllegalArgumentException e) {
            throw new InvalidSRVPacketException();
        }

        /** READ ARGUMETNS */
        for(int i = 2; i < fields.length;i++){
            packet.arguments.add(fields[i]);
        }

        return packet;
    }

}
