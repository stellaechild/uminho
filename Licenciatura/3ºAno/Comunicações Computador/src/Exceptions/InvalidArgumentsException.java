package Exceptions;

public class InvalidArgumentsException extends Exception{
    public InvalidArgumentsException() {super();}
    public InvalidArgumentsException(String msg) {
        super(msg);
    }
}
