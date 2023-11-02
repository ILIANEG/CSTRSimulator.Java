package CustomExcpetions;

public class EmptyArray extends Throwable {
    private boolean isNull;

    public EmptyArray(Object array) {
        super("Mandatory array is empty or null");
        if(array == null) this.isNull = true;
        else this.isNull = false;
    }
}
