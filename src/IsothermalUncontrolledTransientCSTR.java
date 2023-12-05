import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_DataStorage.NumericalDataStorage;
import CHG4343_Design_Project_ODESolver.*;

public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume;
    protected NumericalDataStorage g_runData;

    public IsothermalUncontrolledTransientCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume) throws NullPointerException, NumericalException, ArrayException {
        super(inlet, outlet, reaction);
        if(volume < 0) throw new NumericalException("Negative volume is not allowed");
        this.volume = volume;
        this.reset();
    }
    public IsothermalUncontrolledTransientCSTR(IsothermalUncontrolledTransientCSTR source) throws NumericalException, ArrayException {
        super(source.inlet, source.outlet, source.getReaction());
        this.volume = source.volume;
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
    public void reset() throws ArrayException {
        this.g_runData = new NumericalDataStorage(this.generateHeaders());
    }
    public NumericalDataStorage getRuntimeData() {
        return this.g_runData;
    }
    /**
     * Runs CSTR till steady state, witch step of 0.0001 time unit and e = 0.000001
     */
    @Override
    protected void integrate(double xStart, double xFinish, AbstractODEStepper odeEngine) throws NumericalException, ArrayException {
        double currentTime = xStart;
        while(currentTime < xFinish) {
            Flow tmpFlow = this.outlet.clone();
            ChemicalSpecies[] tmpSpecies = tmpFlow.mixture.getSpecies();
            XYFunction[] speciesFunction = new XYFunction[tmpFlow.mixture.getNumberOfSpecies()];
            double[] speciesConcentrations = new double[tmpFlow.mixture.getNumberOfSpecies()];
            for(int i = 0; i < tmpFlow.mixture.getNumberOfSpecies(); i++) {
                speciesFunction[i] = this.generateDifferentialEquation(tmpSpecies[i], tmpFlow.mixture);
                speciesConcentrations[i] = tmpFlow.mixture.getConcentration(tmpSpecies[i]);
            }
            speciesConcentrations = odeEngine.step(currentTime, speciesConcentrations, speciesFunction);
            for(int i = 0; i < tmpSpecies.length; i++) {
                this.outlet.mixture.setConcentration(tmpSpecies[i], speciesConcentrations[i]);
            }
            currentTime += odeEngine.getStepSize();
        }
    }
    @Override
    public void run(double h, double finalTime, AbstractODEStepper odeEngine) throws NumericalException, ArrayException {
        odeEngine.reset();
        odeEngine.setStepSize(h);
        double currentTime = 0;
        g_runData.addDataRow(formatDataRow(currentTime, this.outlet.mixture.getConcentrations()));
        while(currentTime < finalTime) {
            this.integrate(currentTime, currentTime+h, odeEngine);
            currentTime += h;
            g_runData.addDataRow(formatDataRow(currentTime, this.outlet.mixture.getConcentrations()));
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
}
