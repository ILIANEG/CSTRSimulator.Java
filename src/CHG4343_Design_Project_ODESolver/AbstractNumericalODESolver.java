package CHG4343_Design_Project_ODESolver;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_CustomExcpetions.ODESolverException;
import CHG4343_Design_Project_Mathematical.XYFunction;

/**
 * Abstract ODE solver that solves a system of differential equations for an array of functions (implementing interface XYFunction)
 * and double array of initial conditions.
 */
public abstract class AbstractNumericalODESolver {
    private double dx0;
    private double tolerance; // local tolerance
    private double convergance; // global tolerance
    private int maxIterations;
    protected double g_dx;
    protected double g_epsilon;

    /**
     * ODESolver constructor.
     *
     * @param dx0 initial step size.
     * @param tolerance tolerance (both local and global)
     * @param convergance
     * @param maxIterations
     */
    public AbstractNumericalODESolver(double dx0, double tolerance, double convergance, int maxIterations) {
        this.convergance = convergance;
        if(dx0 <= 0) throw new NumericalException("Default step size can not be 0 or negative number in ODE Solver.");
        if(tolerance <= 0) throw new NumericalException("Tolerance can not be 0 or negative number in ODE Solver.");
        if(convergance <= 0) throw new NumericalException("Convergance can not be 0 or negative number in ODE Solver.");
        if(maxIterations <= 0) throw new NumericalException("Maximum iterations can not be 0 or negative number in ODE Solver");
        this.tolerance = tolerance;
        this.dx0 = dx0;
        this.maxIterations = maxIterations;
        this.reset();
    }

    /**
     * Copy constructor.
     * @param source
     */
    public AbstractNumericalODESolver(AbstractNumericalODESolver source) {
        this.dx0 = source.dx0;
        this.tolerance = source.tolerance;
        this.maxIterations = source.maxIterations;
        this.convergance = source.convergance;
        this.reset();

    }
    public abstract AbstractNumericalODESolver clone();

    /* Accessors & Mutators */

    /**
     * Default step size getter.
     * @return default step size.
     */
    public double getDx0() {
        return dx0;
    }

    /**
     * Default step size setter.
     * @param dx0
     */
    public void setDx0(double dx0) {
        if(dx0 <= 0) throw new ODESolverException("Invalid default step size in ODE solver Object.");
        this.dx0 = dx0;
    }

    /**
     * Tolerance accessor.
     * @return tolerance.
     */
    public double getTolerance() { return this.tolerance; }

    /**
     * Tolerance mutator.
     * @param tolerance
     */
    public void setTolerance(double tolerance) {
        if(tolerance <= 0) throw new ODESolverException("Tolerance can not be negative or 0");
        this.tolerance = tolerance;
    }

    /**
     * Convergence mutator.
     * @return
     */
    public double getConvergance() { return this.convergance; }
    public void setConvergance(double convergance) {
        if(convergance <= 0) throw new IllegalArgumentException("Convergance value can not be negative or 0");
        this.convergance = convergance;
    }

    /**
     * Maximum iterations accessor.
     * @return
     */
    public double getMaxIteration() { return this.maxIterations; }

    /**
     * Maximum iterations mutator.
     * @param maxIterations
     */
    public void setMaxIterations(int maxIterations) {
        if(this.maxIterations <= 0) throw new NumericalException("Maximum number of iterations can not be 0 or negative number");
        this.maxIterations = maxIterations;
    }
    /**
     * check if ODESolver is in the converged state
     * @return
     */
    public boolean isConverged() {
        return this.g_epsilon <= this.convergance;
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
        while(!this.isConverged()) {
            if(this.maxIterations <= iterator) throw new ODESolverException("Maximum number of iterations reached");
            yi = this.step(x, yi, dydx);
            x += this.g_dx;
            iterator++;
        }
        return yi;
    }

    /**
     * Definite integral.
     * @param x0 initial x.
     * @param xf final x.
     * @param y initial conditions at x0.
     * @param dydx array of differential equations.
     * @return
     */
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
     * @return new value of y.
     */
    public abstract double[] step(double x, double[] y, XYFunction[] dydx) throws ArrayException, NumericalException;
}
