package Exceptions;

public class CorruptedPacketException extends Exception {
    public CorruptedPacketException(){super();}
    public CorruptedPacketException(String msg) {
        super(msg);
    }
}
