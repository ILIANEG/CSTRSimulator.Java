import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.Function;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    // TODO
    public IsothermalUncontrolledTransientCSTR(Flow inlet, AbstractReaction reaction, double volume) throws NullPointerException, NumericalException {
        super(inlet, reaction);
        this.volume = volume;
    }

    @Override
    public AbstractReactor clone() {
        return null;
    }
    @Override
    public void run(int timeStep, int runTime) {

    }

    /**
     * Runs CSTR till steady state;
     * @param timeStep
     */
    @Override
    public void run(int timeStep) {

    }
    public void run(int timeStep, double epsilon) {

    }
    @Override
    protected void initializeOutletFlow() throws NumericalException {
        super.g_Outlet = new Flow(super.inlet.getVolumetricFlowrate());
    }

    @Override
    public String toString() {
        return "Inlet: " + this.inlet.toString() + "\n" + "Outlet: " + this.g_Outlet.toString() + "\n" + "V: " + this.volume;
    }
    private Function generateDifferentialEquation(ChemicalSpecies species) {
        return ((x, y) ->
                this.inlet.getVolumetricFlowrate() / this.volume *
                        (this.inlet.mixture.getConcentration(species) - this.g_Outlet.mixture.getConcentration(species))
        + super.getReaction().getStoichiometry(species)*super.getReaction().getRateConstant());
    }
}
