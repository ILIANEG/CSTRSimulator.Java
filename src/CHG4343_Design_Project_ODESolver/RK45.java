package CHG4343_Design_Project_ODESolver;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_CustomExcpetions.ODESolverException;
import CHG4343_Design_Project_Mathematical.XYFunction;

/**
 * Implementation of AbstractODESolver that uses RK45 algorithm
 */
public class RK45 extends AbstractNumericalODESolver {
    /* Butcher tableaus for RK45 */
    public static final double[][] butcherTableau = {{0}, {1./4,1./4}, {3./8,3./32,9./32},
            {12./13,1923./2197,-7200./2197,7296./2197},{1,439./216,-8,3680./513,-845./4104},
            {1./2,-8./27,2,-3544./2565,1859./4104,-11./40}};
    public static final double[] butcherTableau4thOrder = {25./216,0,1408./2565,2197./4104,-1./5,0};
    public static final double[] butcherTableau5thOrder = {16./135,0,6656./12825,28561./56430,-9./50,2./55};

    /**
     * RK45 Constructor.
     * @see AbstractNumericalODESolver for super().
     */
    public RK45(double defaultStepSize, double tolerance, double convergance, int maxIterations) throws NumericalException {
        super(defaultStepSize, tolerance, convergance, maxIterations);
    }

    /**
     * Copy constructor.
     * @param source
     */
    public RK45(RK45 source) {
        super(source);
    }

    /**
     * Clone method.
     * @return
     */
    @Override
    public AbstractNumericalODESolver clone() {
        return new RK45(this);
    }

    /**
     * Perform RK45 step
     * @param x Current x.
     * @param y Current y.
     * @param dydx Differentiation function for current step.
     * @return
     */
    @Override
    public double[] step(double x, double[] y, XYFunction[] dydx) {
        // Check for valid y and dydx
        if(y == null) throw new IllegalArgumentException("Array y in ODESolver was found to be null");
        if(dydx == null) throw new IllegalArgumentException("Array of differential equations in ODESolver was found to be null");
        if(y.length != dydx.length) throw new ArrayException("Array y does not match array of functions in RK45");
        // Temporary arrays.
        double[] yii = new double[y.length];
        double[] zii = new double[y.length];
        // Boolean that stored local convergence state.
        boolean localConvergence = false;
        int iterator = 0;
        // While local convergence is not achieved.
        while(!localConvergence) {
            // Check for maximum iterations exceeded.
            if(this.getMaxIteration() <= iterator) throw new ODESolverException("Maximum iterations exceeded");
            // Arrays of k's.
            double[][] k = new double[6][y.length];
            // For each k.
            for (int i = 0; i < k.length; i++) {
                // Modify value of x and record to temporary variable.
                double tmpX = x + RK45.butcherTableau[i][0] * this.g_dx;
                double[] tmpY = new double[y.length];
                // Modify each value of y in temporary array.
                for (int j = 0; j < k[i].length; j++) {
                    tmpY[j] = y[j];
                    for (int b = 1; b < RK45.butcherTableau[i].length; b++) {
                        tmpY[j] += RK45.butcherTableau[i][b] * k[b - 1][j];
                    }
                }
                // calculate k[i]
                for (int j = 0; j < dydx.length; j++) {
                    k[i][j] = this.g_dx * dydx[j].evaluate(tmpX, tmpY);
                }
            }
            // calculate yii and zii
            for (int i = 0; i < y.length; i++) {
                yii[i] = y[i];
                zii[i] = y[i];
                for (int j = 0; j < k.length; j++) {
                    yii[i] += RK45.butcherTableau4thOrder[j] * k[j][i];
                    zii[i] += RK45.butcherTableau5thOrder[j] * k[j][i];
                }
            }
            // calculate new step size
            double newStep = calculateStepSize(zii, yii);
            // check local convergence.
            localConvergence = this.g_dx <= newStep;
            // if overflow encountered
            if(newStep == Double.POSITIVE_INFINITY) throw new ODESolverException("Overflow encountered, try to lower tolerance");
            this.g_dx = newStep;
            iterator++;
        }
        // calculate global epsilon (convergence)
        this.g_epsilon = this.calculateEpsilone(y, yii);
        return yii;
    }

    /**
     * Helper function that calculates epsilone.
     * @param z
     * @param y
     * @return
     */
    private double calculateEpsilone(double[] z, double[] y) {
        double maxError = 0;
        for(int i = 0; i < z.length; i++) {
            double delta = Math.abs(z[i] - y[i]);
            if( maxError < delta) maxError = delta;
        }
        return maxError;
    }

    /**
     * Helper function that calculates step size.
     * @param z
     * @param y
     * @return
     */
    private double calculateStepSize(double[] z, double[] y) {
        return this.g_dx * Math.pow((this.getTolerance()*this.g_dx)
                /(2*calculateEpsilone(z, y)), 1./4);
    }
}
