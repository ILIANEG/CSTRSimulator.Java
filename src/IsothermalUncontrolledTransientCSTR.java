import CHG4343_Design_Project_ODESolver.Function;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    private Function[] reactantsDifferentialEquations;
    private Function[] productDifferentialEquations;
    // TODO
    public IsothermalUncontrolledTransientCSTR(Flow inlet, AbstractReaction reaction, double volume) throws NullPointerException {
        super(inlet, reaction);
        this.volume = volume;
        double[] productStoichiometry = super.getReaction().getProductsStoichiometry();
        double[] reactantsStoichiometry = super.getReaction().getReactantsStoichiometry();
    }


    @Override
    public AbstractReactor clone() {
        return null;
    }
    @Override
    public void run(int runTime, int timeStep) {

    }
    @Override
    public void run(int timeStep) {

    }
    @Override
    protected void initializeOutletFlow() {

    }
}
