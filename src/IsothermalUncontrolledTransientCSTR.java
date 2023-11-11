import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.Function;
import CHG4343_Design_Project_ODESolver.ODEStepper;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    private ODEStepper odeEngine;
    public IsothermalUncontrolledTransientCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, ODEStepper odeEngine) throws NullPointerException, NumericalException {
        super(inlet, outlet, reaction);
        if(volume < 0) throw new NumericalException("Nagative volume is not allowed");
        this.volume = volume;
        this.odeEngine = odeEngine;
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
    public void run() throws NumericalException {
        /*double currentTime = 0;
        double e = 0;
        do {
            ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[outlet.mixture.getNumberOfSpecies()];
            double[] tmpConcentrations = new double[outlet.mixture.getNumberOfSpecies()];
            for (int i = 0; i < super.outlet.mixture.getNumberOfSpecies(); i++) {
                tmpSpecies[i] = super.outlet.mixture.getSpecies()[i];
                tmpConcentrations[i] = this.odeEngine.step(0.0001, currentTime,
                        super.outlet.mixture.getConcentration(tmpSpecies[i]), this.generateDifferentialEquation(tmpSpecies[i]));
            }

            for (int i = 0; i < tmpSpecies.length; i++) {
                if(this.outlet)
                this.outlet.mixture.setConcentration(tmpSpecies[i], tmpConcentrations[i]);
            }
        } while (e < 0.000001);*/
    }
    public void run(double timeStep, double runTime) throws NumericalException {
        double currentTime = 0;
        while(currentTime < runTime) {
            ChemicalSpecies[] tmpSpecies = new ChemicalSpecies[outlet.mixture.getNumberOfSpecies()];
            double[] tmpConcentrations = new double[outlet.mixture.getNumberOfSpecies()];
            for(int i = 0; i < super.outlet.mixture.getNumberOfSpecies(); i++) {
                tmpSpecies[i] = super.outlet.mixture.getSpecies()[i];
                tmpConcentrations[i] = this.odeEngine.step(timeStep, currentTime,
                        super.outlet.mixture.getConcentration(tmpSpecies[i]), this.generateDifferentialEquation(tmpSpecies[i]));
            }
            for(int i = 0; i < tmpSpecies.length; i++) {
                this.outlet.mixture.setConcentration(tmpSpecies[i], tmpConcentrations[i]);
            }
            currentTime += timeStep;
        }
    }
    @Override
    public String toString() {
        return "Inlet: " + this.inlet.toString() + "\n" + "Outlet: " + this.outlet.toString() + "\n" + "V: " + this.volume;
    }
    public Function generateDifferentialEquation(ChemicalSpecies species) {
        return ((x, y) ->
                super.inlet.getVolumetricFlowrate() / this.volume *
                        (super.inlet.mixture.getConcentration(species) - super.outlet.mixture.getConcentration(species))
        + super.getReaction().getStoichiometry(species)*super.getReaction().calculateReactionRate(super.outlet.mixture));
    }
}
