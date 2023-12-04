package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class PIController extends AbstractController implements P, I {
    private double kC;
    private double tauI;
    private double g_iPrev;
    private double g_tPrev;
    public PIController(double setPoint, double k, double tau, double theta) throws NumericalException {
        super(setPoint);
        reset();
        tune(k, tau, theta);
    }
    public PIController(PIController source) throws NumericalException {
        super(source.getSetPoint());
        reset();
        this.kC = source.kC;
        this.tauI = source.tauI;
    }
    public PIController clone() {
        try {
            return new PIController(this);
        } catch (NumericalException e) {
            return null;
        }
    }

    public void reset() {
        this.tauI = 0;
        this.kC = 0;
    }

    @Override
    public void tune(double k, double tau, double theta) {
        this.kC = 1/k * (0.9*tau / theta + 1./12.);
        this.tauI = theta * ((30 + 3* theta/tau)/(9 + 20 * theta/tau));
    }

    @Override
    public double calculateControlSignal(double t, double value) {
        double p = this.calculateP(this.kC, this.e(value));
        double i = this.calculateI(this.kC, this.tauI, this.e(value), t, this.g_tPrev,this.g_iPrev);
        this.g_tPrev = t;
        this.g_iPrev = i;
        return p + i;
    }

    @Override
    public double calculateI(double kC, double tauI, double e, double t, double tPrev, double iPrev) {
        return iPrev + kC/tauI * e*(t-tPrev);
    }

    @Override
    public double calculateP(double kC, double e) {
        return kC * e;
    }
}
