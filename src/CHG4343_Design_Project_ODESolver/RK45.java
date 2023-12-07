package CHG4343_Design_Project_ODESolver;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.LengthMismatch;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class RK45 extends AbstractODESolver {
    public static final double[][] butcherTableau = {{0}, {1./4,1./4}, {3./8,3./32,9./32},
            {12./13,1923./2197,-7200./2197,7296./2197},{1,439./216,-8,3680./513,-845./4104},
            {1./2,-8./27,2,-3544./2565,1859./4104,-11./40}};
    public static final double[] butcherTableau4thOrder = {25./216,0,1408./2565,2197./4104,-1./5,0};
    public static final double[] butcherTableau5thOrder = {16./135,0,6656./12825,28561./56430,-9./50,2./55};
    public RK45(double defaultStepSize, double tolerance, int maxIterations) throws NumericalException {
        super(defaultStepSize, tolerance, maxIterations);
    }
    public RK45() {
        super();
    }
    public RK45(RK45 source) {
        super(source);
    }
    @Override
    public AbstractODESolver clone() {
        return new RK45(this);
    }

    @Override
    public double[] step(double x, double[] y, XYFunction[] dydx) throws ArrayException, NumericalException {
        if(y.length != dydx.length) throw new ArrayException("Array y does not match array of functions",
                new LengthMismatch("Length of arrays y does not match length of arrays functions",
                        y.length, dydx.length));
        double[] yii = new double[y.length];
        double[] zii = new double[y.length];
        boolean localConvergence = false;
        int iterator = 0;
        while(!localConvergence) {
            if(this.maxIterations <= iterator) throw new NumericalException("Maximum iterations exceeded");
            double[][] k = new double[6][y.length];
            for (int i = 0; i < k.length; i++) {
                double tmpX = x + RK45.butcherTableau[i][0] * this.getStepSize();
                double[] tmpY = new double[y.length];
                for (int j = 0; j < k[i].length; j++) {
                    tmpY[j] = y[j];
                    for (int b = 1; b < RK45.butcherTableau[i].length; b++) {
                        tmpY[j] += RK45.butcherTableau[i][b] * k[b - 1][j];
                    }
                }
                for (int j = 0; j < dydx.length; j++) {
                    k[i][j] = this.getStepSize() * dydx[j].evaluate(tmpX, tmpY);
                }
            }
            for (int i = 0; i < y.length; i++) {
                yii[i] = y[i];
                zii[i] = y[i];
                for (int j = 0; j < k.length; j++) {
                    yii[i] += RK45.butcherTableau4thOrder[j] * k[j][i];
                    zii[i] += RK45.butcherTableau5thOrder[j] * k[j][i];
                }
            }
            double newStep = calculateStepSize(zii, yii);
            localConvergence = this.g_dx <= newStep;
            this.setStepSize(newStep);
            iterator++;
        }
        this.setEpsilon(this.calculateEpsilone(y, yii));
        return yii;
    }
    private double calculateEpsilone(double[] z, double[] y) {
        double maxError = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < z.length; i++) {
            double delta = Math.abs(z[i] - y[i]);
            if( maxError < delta) maxError = delta;
        }
        return maxError;
    }
    private double calculateStepSize(double[] z, double[] y) {
        return this.getStepSize() * Math.pow((this.getTolerance()*this.getStepSize())
                /(2*calculateEpsilone(z, y)), 1./4);
    }
}
