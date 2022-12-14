package Exceptions;

public class CorruptedDataException extends Exception{
    public CorruptedDataException(){super();}
    public CorruptedDataException(String msg) {
        super(msg);
    }
}
