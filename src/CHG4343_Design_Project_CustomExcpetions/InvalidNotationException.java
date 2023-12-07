package CHG4343_Design_Project_CustomExcpetions;

public class InvalidNotationException extends RuntimeException {
    public InvalidNotationException(String errorMessage) {
        super(errorMessage);
    }
}
