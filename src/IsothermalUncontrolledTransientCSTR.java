import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_DataStorage.NumericalDataStorage;
import CHG4343_Design_Project_Mathematical.XYFunction;
import CHG4343_Design_Project_ODESolver.*;

/**
 * Isothermal uncontrolled transient CSTR implementation (Case 1)
 */
public class IsothermalUncontrolledTransientCSTR extends AbstractReactor {
    private double volume; // volume in appropriate units
    protected double g_currentTime; // global variable that tracks total reactor runtime
    protected NumericalDataStorage g_runData; // runtime data
    protected AbstractNumericalODESolver g_odeEngine; // ode engine used to solve the system of differential equations

    /**
     * Constructor for isothermal uncontrolled transient CSTR reactor.
     * @param volume volume of the reactor, volume is constant for given implementation.
     * @param odeEngine odeEngine used to solve differential equations numerically.
     * @see AbstractReactor for super constructor.
     */
    public IsothermalUncontrolledTransientCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, AbstractNumericalODESolver odeEngine) {
        super(inlet, outlet, reaction);
        if(volume < 0) throw new NumericalException("Negative volume is not allowed");
        if(odeEngine == null) throw new IllegalArgumentException("Invalid ODE Engine provided to CSTR reactor.");
        this.volume = volume;
        this.g_odeEngine = odeEngine.clone();
        this.reset();
    }

    /**
     * Copy constructor for isothermal uncontrolled transient CSTR reactor.
     * @param source
     * @see AbstractReactor for super constructor.
     */
    public IsothermalUncontrolledTransientCSTR(IsothermalUncontrolledTransientCSTR source) {
        super(source.inlet, source.outlet, source.getReaction());
        this.volume = source.volume;
        this.g_odeEngine = source.g_odeEngine.clone();
        this.reset();
    }

    /**
     * clone method implementation for uncontrolled transient state CSTR.
     * @return deep copy of the object.
     */
    @Override
    public AbstractReactor clone() {
        return new IsothermalUncontrolledTransientCSTR(this);
    }

    /**
     * Reset global parameters to their initial state.
     */
    public void reset() {
        // reset odeEgnine
        this.g_odeEngine.reset();
        // clear run data
        this.g_runData = new NumericalDataStorage(this.generateHeaders());
        // reset current runtime to 0;
        this.g_currentTime = 0;
    }

    /* Getters and Setters */

    /**
     * Volume getter
     * @return volume of the reactor.
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Volume setter.
     * @param volume volume of the reactor to be assigned.
     */
    public void setVolume(double volume){
        if(0 <= volume) throw new NumericalException("Attempting to assign negative or zero volume to the reactor");
        this.volume = volume;
    }
    /**
     * odeEngine getter
     * @return odeEngine.
     */
    public AbstractNumericalODESolver getOdeEngine() {
        return this.g_odeEngine.clone();
    }

    /**
     * odeEngine setter.
     * @param odeEngine odeEngine that implements AbstractODESolver
     */
    public void setOdeEngine(AbstractNumericalODESolver odeEngine){
        if(odeEngine == null) throw new IllegalArgumentException("Attempting to assign invalid ODE Solver");
        this.g_odeEngine = odeEngine.clone();
    }

    /**
     * Return reference to runtime data. Since it is used purely for tracking purposes, data is not encapsulated.
     * @return reference to runtime data.
     */
    public NumericalDataStorage getRuntimeData() {
        return this.g_runData;
    }

    /**
     * Runs reactor for a given amount of time. Records run result every dt period of time.
     * @param dt time step for data recording purposes.
     * @param runTime total run time of a reactor.
     * @reset flag to reset global parameters. DOES NOT reset the outlet conditions.
     */
    public void runForNTime(double dt, double runTime, boolean reset) {
        if(dt <= 0) throw new IllegalArgumentException("dt can not be negative or equal to 0");
        if(runTime < 0) throw new IllegalArgumentException("Run time can not be negative");
        if(reset) this.reset();
        double localTimeCounter = 0;
        while(localTimeCounter < runTime) {
            g_runData.addDataRow(formatDataRow(this.g_currentTime, this.outlet.mixture.getConcentrations()));
            this.outlet.mixture
                    .setConcentrations(this.g_odeEngine.integrate(this.g_currentTime, this.g_currentTime+dt, this.outlet.mixture.getConcentrations(), this.generateDifferentialEquations()));
            localTimeCounter += dt;
            this.g_currentTime += dt;
        }
    }

    /**
     * Runs reactor till steady state is achieved. Records run result every dt period of time.
     * @param dt time step for data recording purposes.
     */
    public void runTillSteadyState(double dt, boolean reset) {
        if(dt <= 0) throw new IllegalArgumentException("dt can not be negative or equal to 0");
        if(reset) this.reset();
        while(!this.g_odeEngine.isConverged()) {
            g_runData.addDataRow(formatDataRow(this.g_currentTime, this.outlet.mixture.getConcentrations()));
            this.outlet.mixture
                    .setConcentrations(this.g_odeEngine.integrate(this.g_currentTime, this.g_currentTime+dt, this.outlet.mixture.getConcentrations(), this.generateDifferentialEquations()));
            this.g_currentTime += dt;
        }
    }

    /**
     * Method calculates outlet concentrations based on current conditions. Run time data will not be recorded.
     */
    @Override
    public void calculateOutlet() {
        reset();
        this.outlet.mixture
                .setConcentrations(this.g_odeEngine.converge(this.g_currentTime, this.outlet.mixture.getConcentrations(), this.generateDifferentialEquations()));
    }

    /**
     * Programmatically generates differential equation for a given specie in form of a lambda function.
     * @param species Species for which differential equation is being generated
     * @param outlet Outlet condition
     * @return lambda function (XYFunction interface) representing a differential equation for a given species.
     */
    public XYFunction generateDifferentialEquation(ChemicalSpecies species, ChemicalMixture outlet) {
        int specieIndex = outlet.getSpeciesIndex(species);
        AbstractReaction r = this.getReaction().clone();
        return ((time, concentrations) -> this.inlet.getVolumetricFlowrate() / this.volume * (this.inlet.mixture.getConcentration(species) - concentrations[specieIndex])
                + r.getStoichiometry(species) * r.generateRateExpression(outlet).evaluate(concentrations));
    }

    /**
     * Generates headers for data storage.
     * @return String of data headers in form of [t, v, A, B, ..., N]
     */
    protected String[] generateHeaders() {
        ChemicalSpecies[] species = this.outlet.mixture.getSpecies();
        String[] headers = new String[species.length + 2];
        headers[0] = "t";
        headers[1] = "v";
        for(int i = 0; i < species.length; i++) {
            headers[i+2] = species[i].getName();
        }
        return headers;
    }

    /**
     * Helper function that formats data row before being added to the data storage.
     * In essence, it simply puts time as first element of array and then copies the array of concentrations.
     * @param time
     * @param concentrations array of concentrations.
     * @return array of data prepared for data storage.
     */
    protected double[] formatDataRow(double time, double[] concentrations) {
        double[] formattedData = new double[concentrations.length + 2];
        formattedData[0] = time;
        formattedData[1] = this.outlet.getVolumetricFlowrate();
        for(int i = 0; i < concentrations.length; i++) {
            formattedData[i+2] = concentrations[i];
        }
        return formattedData;
    }

    /**
     * Generates an array of differential equations for each species.
     * @return array of lambda functions (XYFunction objects).
     */
    public XYFunction[] generateDifferentialEquations() {
        ChemicalSpecies[] tmpSpecies = this.outlet.mixture.getSpecies();
        XYFunction[] speciesFunctions = new XYFunction[this.outlet.mixture.getNumberOfSpecies()];
        for(int i = 0; i < this.outlet.mixture.getNumberOfSpecies(); i++) {
            speciesFunctions[i] = this.generateDifferentialEquation(tmpSpecies[i], this.outlet.mixture);
        }
        return speciesFunctions;
    }

    /**
     * @return string representation of chemical reactor.
     */
    @Override
    public String toString() {
        return "Inlet: " + this.inlet.toString() + "\n" + "Outlet: " + this.outlet.toString() + "\n" + "V: " + this.volume;
    }
}
