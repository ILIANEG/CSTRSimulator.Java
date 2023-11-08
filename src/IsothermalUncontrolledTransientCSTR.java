import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.Euler;
import CHG4343_Design_Project_ODESolver.Function;
import CHG4343_Design_Project_ODESolver.ODEStepper;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    /* TODO: ODEStepper as a parameter in the constructor */
    private double volume;
    private ODEStepper odeEngine;
    public IsothermalUncontrolledTransientCSTR(Flow inlet, AbstractReaction reaction, double volume) throws NullPointerException, NumericalException {
        super(inlet, reaction);
        this.volume = volume;
        this.odeEngine = new Euler();
    }

    @Override
    public AbstractReactor clone() {
        return null;
    }
    /**
     * Runs CSTR till steady state;
     * @param timeStep
     */
    @Override
    public void run(int timeStep) {

    }
    @Override
    public void run(int timeStep, int runTime) throws NumericalException {
        double currentTime = 0;
        while(currentTime <= runTime) {
            for(int i = 0; i < super.inlet.mixture.getNumberOfSpecies(); i++) {
                ChemicalSpecies currSpecies = super.inlet.mixture.getSpecies()[i];
                this.g_Outlet.mixture.setConcentration(currSpecies,
                        this.odeEngine.step(timeStep, currentTime, super.g_Outlet.mixture.getConcentration(currSpecies),
                                this.generateDifferentialEquation(currSpecies)));
            }
            currentTime += timeStep;
        }
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
        + super.getReaction().getStoichiometry(species)*super.getReaction().calculateReactionRate(this.g_Outlet.mixture));
    }
}
