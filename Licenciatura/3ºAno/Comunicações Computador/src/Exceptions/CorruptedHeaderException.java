package Exceptions;

public class CorruptedHeaderException extends Exception {
    public CorruptedHeaderException(){super();}
    public CorruptedHeaderException(String msg) {
        super(msg);
    }
}
