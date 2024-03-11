package pl.malcew.testbitly.exception;

public class WrongTimeFormatException extends RuntimeException{
    public WrongTimeFormatException(String message) {
        super(message);
    }
}
