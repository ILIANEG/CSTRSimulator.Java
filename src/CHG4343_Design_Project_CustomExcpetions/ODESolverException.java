package CHG4343_Design_Project_CustomExcpetions;

public class ODESolverException extends RuntimeException {
    public ODESolverException(String errorMessage) {
        super(errorMessage);
    }
}
