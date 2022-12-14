import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SRVTransmitter {
    /** Socket */
    private Socket socket;

    /**
     * Constructor method. Requires a socket to read/write from/to.
     * @param socket TCP Socket
     */
    public SRVTransmitter(Socket socket){
        this.socket = socket;
    }

    /**
     * Method used to read SRVPackets.
     * @return SRVPacket read
     * @throws IOException I/O error
     * @throws InvalidSRVPacketException Data Read doesn't match the SRVPacket Standards
     */
    public SRVPacket readPacket() throws IOException,InvalidSRVPacketException {
        DataInputStream input = new DataInputStream(this.socket.getInputStream());

        /** Read Short for size, then read Data */
        short size = input.readShort();
        byte[] data = new byte[size];
        input.read(data);

        /** Deserialize Data */
        return SRVPacket.deserialize(data);
    }

    /**
     * Method to send SRVPackets.
     * @param packet SRVPacket to send
     * @throws IOException I/O Error
     */
    public void writePacket(SRVPacket packet) throws IOException {
        DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());

        /** Serialize packet and calculate Data size */
        byte[] data = packet.serialize();
        short size = (short) data.length;

        /** Write size and Data */
        output.writeShort(size);
        output.write(data);
    }

    /**
     * Method to close the transmitter
     */
    public void close(){
        try {this.socket.close();
        } catch (IOException e){}
    }

}
