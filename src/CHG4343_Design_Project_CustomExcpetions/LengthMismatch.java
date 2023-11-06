package CHG4343_Design_Project_CustomExcpetions;

public class LengthMismatch extends Throwable {
    int arrayLength;
    int adjArrayLength;
    public LengthMismatch(String errorMessage, int arrayLength, int adjArrayLength) {
        super(errorMessage);
        this.arrayLength = arrayLength;
        this.adjArrayLength = adjArrayLength;
    }
}
