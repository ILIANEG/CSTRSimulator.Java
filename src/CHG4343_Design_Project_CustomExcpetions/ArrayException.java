package CHG4343_Design_Project_CustomExcpetions;

public class ArrayException extends Exception {
    public ArrayException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
