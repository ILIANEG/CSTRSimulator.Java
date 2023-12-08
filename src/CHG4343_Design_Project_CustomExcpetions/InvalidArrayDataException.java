package CHG4343_Design_Project_CustomExcpetions;

public class InvalidArrayDataException extends RuntimeException {
    private int index;

    public InvalidArrayDataException(String errorMessage, int index) {
        super(errorMessage);
        this.index = index;
    }
}
