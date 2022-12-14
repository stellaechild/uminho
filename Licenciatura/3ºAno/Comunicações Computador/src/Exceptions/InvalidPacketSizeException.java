package Exceptions;

public class InvalidPacketSizeException extends Exception{
    public InvalidPacketSizeException() {
        super();
    }
    public InvalidPacketSizeException(String msg) {
        super(msg);
    }
}
