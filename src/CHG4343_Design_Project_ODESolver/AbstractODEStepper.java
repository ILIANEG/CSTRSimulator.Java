package CHG4343_Design_Project_ODESolver;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public abstract class AbstractODEStepper {
    private double dx0;
    private double tolerance;
    protected int maxIterations;
    protected double g_dx;
    protected double g_epsilon;
    public AbstractODEStepper(double dx0, double tolerance, int maxIterations) throws NumericalException {
        if(dx0 <= 0) throw new NumericalException("Invalid initial step size in ODEStepper Object.");
        if(tolerance <= 0) throw new NumericalException("Tolerance can not be 0 or negative number");
        this.tolerance = tolerance;
        this.dx0 = dx0;
        this.maxIterations = maxIterations;
        this.reset();
    }

    /**
     * Constructor with default step size = 0.00001
     */
    public AbstractODEStepper() {
        this.dx0 = 0.01;
        this.tolerance = 1E-10;
        this.maxIterations = 1000000;
        reset();
    }
    public AbstractODEStepper(AbstractODEStepper source) {
        this.dx0 = source.dx0;
        this.tolerance = source.tolerance;
        this.maxIterations = source.maxIterations;
        this.reset();

    }
    public abstract AbstractODEStepper clone();

    /* Accessors & Mutators */

    public double getDx0() {
        return dx0;
    }
    public void setDx0(double stepSize) throws NumericalException {
        if(stepSize <= 0) throw new NumericalException("Invalid step size in ODEStepper Object.");
        this.dx0 = stepSize;
    }
    public void setStepSize(double stepSize) throws NumericalException {
        if(stepSize <= 0) throw new NumericalException("Invalid step size in ODEStepper Object.");
        this.g_dx = stepSize;
    }
    public double getStepSize() {
        return this.g_dx;
    }
    public double getEpsilon() {
        return this.g_epsilon;
    }
    public void setEpsilon(double epsilon) {
        if(epsilon < 0) throw new NumericalException("Epsilon can not be negative");
        this.g_epsilon = epsilon;
    }
    public double getTolerance() { return this.tolerance; }
    public void setTolerance(double tolerance) throws NumericalException {
        if(tolerance <= 0) throw new NumericalException("tolerance can not be negative or 0");
        this.tolerance = this.tolerance;
    }
    public boolean isConverged() {
        return this.g_epsilon <= this.getTolerance();
    }
    /**
     * Reset global variables.
     */
    public void reset() {
        this.g_epsilon = Double.POSITIVE_INFINITY;
        this.g_dx = this.dx0;
    }
    public double[] converge(double x0, double[] y, XYFunction[] dydx) {
        double[] yi = y;
        double x = x0;
        int iterator = 0;
        while(this.getTolerance() < this.g_epsilon) {
            if(this.maxIterations <= iterator) throw new NumericalException("Maximum number of iterations reached");
            yi = this.step(x, yi, dydx);
            x += this.g_dx;
            iterator++;
        }
        return yi;
    }
    public double[] integrate(double x0, double xf, double[] y, XYFunction[] dydx) {
        double[] yi = y;
        double x = x0;
        while(x < xf) {
            yi = this.step(x, yi, dydx);
            x += this.g_dx;
        }
        return yi;
    }
    /**
     * Perform differentiation step with optimal step size and update epsilon.
     * @param x Current x.
     * @param y Current y.
     * @param dydx Differentiation function for current step.
     * @return New value of y.
     */
    public abstract double[] step(double x, double[] y, XYFunction[] dydx) throws ArrayException, NumericalException;
}
