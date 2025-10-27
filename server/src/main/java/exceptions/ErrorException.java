package exceptions;

public class ErrorException extends Exception {
    public final int code;
    public ErrorException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorException(int code, String message, Throwable t){
        super(message,t);
        this.code = code;
    }
}
