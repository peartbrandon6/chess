package exceptions;

public class ServiceException extends Exception {
    public int code;
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }
}
