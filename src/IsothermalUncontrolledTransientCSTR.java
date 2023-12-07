import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_DataStorage.NumericalDataStorage;
import CHG4343_Design_Project_ODESolver.*;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    protected double g_currentTime;
    protected NumericalDataStorage g_runData;
    protected AbstractODEStepper g_odeEngine;

    public IsothermalUncontrolledTransientCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, AbstractODEStepper odeEngine) {
        super(inlet, outlet, reaction);
        if(volume < 0) throw new NumericalException("Negative volume is not allowed");
        this.volume = volume;
        this.g_odeEngine = odeEngine.clone();
        this.reset();
    }
    public IsothermalUncontrolledTransientCSTR(IsothermalUncontrolledTransientCSTR source) {
        super(source.inlet, source.outlet, source.getReaction());
        this.volume = source.volume;
        this.g_odeEngine = source.g_odeEngine.clone();
        this.reset();
    }
    @Override
    public AbstractReactor clone() {
        try {
            return new IsothermalUncontrolledTransientCSTR(this);
        } catch (NumericalException | ArrayException e) {
            throw new RuntimeException(e);
        }
    }
    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume){
        if(0 <= volume) throw new NumericalException("Attempting to assign negative or zero volume to the reactor");
        this.volume = volume;
    }
    public void reset() {
        this.g_odeEngine.reset();
        this.g_runData = new NumericalDataStorage(this.generateHeaders());
        this.g_currentTime = 0;
    }
    public NumericalDataStorage getRuntimeData() {
        return this.g_runData;
    }

    @Override
    public void run(double dt, double runTime) {
        this.reset();
        while(this.g_currentTime < runTime) {
            g_runData.addDataRow(formatDataRow(this.g_currentTime, this.outlet.mixture.getConcentrations()));
            this.outlet.mixture
                    .setConcentrations(this.g_odeEngine.integrate(this.g_currentTime, this.g_currentTime+dt, this.outlet.mixture.getConcentrations(), this.generateDifferentialEquations()));
            this.g_currentTime += dt;
        }
    }
    public void runTillSteadyState() {
        reset();
        this.outlet.mixture
                .setConcentrations(this.g_odeEngine.converge(this.g_currentTime, this.outlet.mixture.getConcentrations(), this.generateDifferentialEquations()));
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
    protected String[] generateHeaders() {
        ChemicalSpecies[] species = this.outlet.mixture.getSpecies();
        String[] headers = new String[species.length + 1];
        headers[0] = "t";
        for(int i = 0; i < species.length; i++) {
            headers[i+1] = species[i].getName();
        }
        return headers;
    }
    protected double[] formatDataRow(double time, double[] concentrations) {
        double[] formattedData = new double[concentrations.length + 1];
        formattedData[0] = time;
        for(int i = 0; i < concentrations.length; i++) {
            formattedData[i+1] = concentrations[i];
        }
        return formattedData;
    }
    protected XYFunction[] generateDifferentialEquations() {
        ChemicalSpecies[] tmpSpecies = this.outlet.mixture.getSpecies();
        XYFunction[] speciesFunctions = new XYFunction[this.outlet.mixture.getNumberOfSpecies()];
        for(int i = 0; i < this.outlet.mixture.getNumberOfSpecies(); i++) {
            speciesFunctions[i] = this.generateDifferentialEquation(tmpSpecies[i], this.outlet.mixture);
        }
        return speciesFunctions;
    }
}
