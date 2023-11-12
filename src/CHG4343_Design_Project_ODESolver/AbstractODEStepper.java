package CHG4343_Design_Project_ODESolver;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public abstract class AbstractODEStepper {
    private double initialStepSize;
    private double g_stepSize;
    private double g_epsilon;
    public AbstractODEStepper(double defaultStepSize) throws NumericalException {
        if(0 <= defaultStepSize) throw new NumericalException("Invalid step size in ODEStepper Object.");
        this.initialStepSize = defaultStepSize;
        this.reset();
    }

    /**
     * Constructor with default step size = 0.00001
     */
    public AbstractODEStepper() {
        this.initialStepSize = 0.00001;
    }
    public AbstractODEStepper(AbstractODEStepper source) {
        this.initialStepSize = source.initialStepSize;
        this.reset();

    }
    public abstract AbstractODEStepper clone();

    /* Accessors & Mutators */

    public double getInitialStepSize() {
        return initialStepSize;
    }
    public void setInitialStepSize(double stepSize) throws NumericalException {
        if(stepSize <= 0) throw new NumericalException("Invalid step size in ODEStepper Object.");
        this.initialStepSize = stepSize;
    }
    public double getStepSize() {
        return this.g_stepSize;
    }
    public double getEpsilon() {
        return this.g_epsilon;
    }

    /**
     * Reset global variables.
     */
    public void reset() {
        this.g_epsilon = Double.POSITIVE_INFINITY;
        this.g_stepSize = this.initialStepSize;
    }

    /**
     * Perform differentiation step with optimal step size and update epsilon.
     * @param x Current x.
     * @param y Current y.
     * @param dydx Differentiation function for current step.
     * @return New value of y.
     */
    public abstract double[] step(double x, double[] y, XYFunction[] dydx) throws ArrayException;

    /**
     *
     * @param yi Previous y.
     * @param yii New y.
     */
    /*protected void updateEpsilon(double[] yi, double[] yii) {
        this.g_epsilon = Math.abs(yii-yi) / Math.abs(yi);
    }*/
    /**
     * Update current step size.
     * @param x Current x.
     * @param y Current y.
     * @param dydx Differentiation function for current step.
     */
    protected abstract void updateStepSize(double x, double y, XYFunction dydx);
}
