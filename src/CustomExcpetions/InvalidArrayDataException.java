package CustomExcpetions;

public class InvalidArrayDataException extends Throwable {
    private int index;
    public InvalidArrayDataException(int index) {
        super("Invalid data in array at index ["+ index + "].");
    }
    public InvalidArrayDataException(String message, int index) {
        super(message);
        this.index = index;
    }
    public InvalidArrayDataException(Throwable cause, int index) {
        super(cause);
        this.index = index;
    }
    public InvalidArrayDataException(String message, Throwable cause, int index) {
        super(message, cause);
        this.index = index;
    }
}
