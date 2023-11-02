package CustomExcpetions;
public class ElementNotFoundException extends Throwable {
    private Object array;
    private Object element;

    public ElementNotFoundException(String errorMessage, Object array, Object element) {
        super("Element has not been found in the array");
        this.array =
    }
}
