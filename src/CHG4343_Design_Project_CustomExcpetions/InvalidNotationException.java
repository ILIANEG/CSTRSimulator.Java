package CHG4343_Design_Project_CustomExcpetions;

public class InvalidNotationException extends Exception {
    public InvalidNotationException(String errorMessage) {
        super(errorMessage);
    }
}
