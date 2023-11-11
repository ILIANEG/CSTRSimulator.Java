import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.*;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    private AbstractODEStepper odeEngine;

    public IsothermalUncontrolledTransientCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume) throws NullPointerException, NumericalException {
        super(inlet, outlet, reaction);
        if(volume < 0) throw new NumericalException("Negative volume is not allowed");
        this.volume = volume;
        this.odeEngine = new RK45();
        this.odeEngine.setDefaultStepSize(1);
        this.odeEngine.reset();
    }
    public IsothermalUncontrolledTransientCSTR(IsothermalUncontrolledTransientCSTR source) throws NumericalException {
        super(source.inlet, source.outlet, source.getReaction());
        this.odeEngine = source.odeEngine;
        this.volume = source.volume;
    }
    @Override
    public AbstractReactor clone() {
        try {
            return new IsothermalUncontrolledTransientCSTR(this);
        } catch (NumericalException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Runs CSTR till steady state, witch step of 0.0001 time unit and e = 0.000001
     */
    @Override
    public void run() throws NumericalException {}
    public void run(double timeStep, double runTime) throws NumericalException, ArrayException {
        this.odeEngine.setDefaultStepSize(timeStep);
        this.odeEngine.reset();
        double currentTime = 0;
        while(currentTime < runTime) {
            Flow tmpFlow = this.outlet.clone();
            ChemicalSpecies[] tmpSpecies = tmpFlow.mixture.getSpecies();
            XYFunction[] speciesFunction = new XYFunction[tmpFlow.mixture.getNumberOfSpecies()];
            double[] speciesConcentrations = new double[tmpFlow.mixture.getNumberOfSpecies()];
            for(int i = 0; i < tmpFlow.mixture.getNumberOfSpecies(); i++) {
                speciesFunction[i] = this.generateDifferentialEquation(tmpSpecies[i], tmpFlow.mixture);
                speciesConcentrations[i] = tmpFlow.mixture.getConcentration(tmpSpecies[i]);
            }
            speciesConcentrations = this.odeEngine.step(currentTime, speciesConcentrations, speciesFunction);
            for(int i = 0; i < tmpSpecies.length; i++) {
                this.outlet.mixture.setConcentration(tmpSpecies[i], speciesConcentrations[i]);
            }
            currentTime += timeStep;
        }
    }
    @Override
    public String toString() {
        return "Inlet: " + this.inlet.toString() + "\n" + "Outlet: " + this.outlet.toString() + "\n" + "V: " + this.volume;
    }
    public XYFunction generateDifferentialEquation(ChemicalSpecies species, ChemicalMixture outlet) {
        int specieIndex = outlet.getSpeciesIndex(species);
        AbstractReaction r = this.getReaction().clone();
        return ((time, concentrations) -> this.inlet.getVolumetricFlowrate() / this.volume * (this.inlet.mixture.getConcentration(species) - concentrations[specieIndex])
                + r.getStoichiometry(species) * r.generateRateExpression(outlet).evaluate(concentrations));
    }
}
