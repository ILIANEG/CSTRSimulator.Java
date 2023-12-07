package CHG4343_Design_Project_CustomExcpetions;

public class InvalidArrayDataException extends RuntimeException {
    private int index;
    public InvalidArrayDataException(int index) {
        super("Invalid data in array at index ["+ index + "].");
    }
    public InvalidArrayDataException(Throwable cause, int index) {
        super(cause);
        this.index = index;
    }
    public InvalidArrayDataException(String errorMessage, int index) {
        super(errorMessage);
        this.index = index;
    }
}
