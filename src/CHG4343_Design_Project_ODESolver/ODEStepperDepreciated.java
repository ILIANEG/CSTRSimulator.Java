package CHG4343_Design_Project_ODESolver;

public interface ODEStepperDepreciated {
    abstract double step(double h, double xStart, double yStart, XYFunction dydx);
}
