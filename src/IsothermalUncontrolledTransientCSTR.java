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
    // TODO
    @Override
    public AbstractReactor clone() { return null; }
    /**
     * Runs CSTR till steady state;
     * @param timeStep
     */
    @Override
    public void run(double timeStep) {

    }
    @Override
    public void run(double timeStep, double runTime) throws NumericalException {
        double currentTime = 0;
        while(currentTime < runTime) {
            ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[g_Outlet.mixture.getNumberOfSpecies()];
            double[] tmpConcentrations = new double[g_Outlet.mixture.getNumberOfSpecies()];
            for(int i = 0; i < super.g_Outlet.mixture.getNumberOfSpecies(); i++) {
                tmpSpecies[i] = super.g_Outlet.mixture.getSpecies()[i];
                tmpConcentrations[i] = this.odeEngine.step(timeStep, currentTime,
                        super.g_Outlet.mixture.getConcentration(tmpSpecies[i]), this.generateDifferentialEquation(tmpSpecies[i]));
            }
            for(int i = 0; i < tmpSpecies.length; i++) {
                this.g_Outlet.mixture.setConcentration(tmpSpecies[i], tmpConcentrations[i]);
            }
            currentTime += timeStep;
        }
    }
    @Override
    public String toString() {
        return "Inlet: " + this.inlet.toString() + "\n" + "Outlet: " + this.g_Outlet.toString() + "\n" + "V: " + this.volume;
    }
    public Function generateDifferentialEquation(ChemicalSpecies species) {
        return ((x, y) ->
                super.inlet.getVolumetricFlowrate() / this.volume *
                        (super.inlet.mixture.getConcentration(species) - super.g_Outlet.mixture.getConcentration(species))
        + super.getReaction().getStoichiometry(species)*super.getReaction().calculateReactionRate(super.g_Outlet.mixture));
    }
}
