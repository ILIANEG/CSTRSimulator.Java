package CustomExcpetions;

public class LengthMismatch extends Throwable {
    int arrayLength;
    int adjArrayLength;
    public LengthMismatch(String errorMessage, int arrayLength, int adjArrayLength) {
        super(errorMessage);
        this.arrayLength = arrayLength;
        this.adjArrayLength = adjArrayLength;
    }
}
