package CHG4343_Design_Project_CustomExcpetions;

public class ArrayException extends RuntimeException {
    public ArrayException(String errorMessage) {
        super(errorMessage);
    }
    public ArrayException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
