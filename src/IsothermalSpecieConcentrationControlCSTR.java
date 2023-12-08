import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_ControlSystem.ControlElement;
import CHG4343_Design_Project_ODESolver.AbstractNumericalODESolver;

/**
 * This class extends functionality of uncontrolled CSTR to enable a controlled run.
 * This particular implementation allows to control single specie concentration, however
 * The id system will allow to have multiple control parameters if implemented.
 * Note: Only runForNTime() is capable of engaging control system, running till steady state
 * won't trigger the control system.
 */
public class IsothermalSpecieConcentrationControlCSTR extends IsothermalUncontrolledTransientCSTR implements Controllable {
    private ControlElement actuator;
    private ChemicalSpecies controlledSpecies;

    /**
     *
     * @param actuator SensorActuator object.
     * @param controlledSpecies species that will be controlled.
     * @see IsothermalUncontrolledTransientCSTR for super() constructor.
     */
    public IsothermalSpecieConcentrationControlCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, AbstractNumericalODESolver odeEngine,
                                                    ControlElement actuator, ChemicalSpecies controlledSpecies){
        super(inlet, outlet, reaction, volume, odeEngine);
        this.controlledSpecies = controlledSpecies.clone();
        this.actuator = actuator.clone();

    }

    /**
     * Copy constructor for isothermal CSTR with a single specie concentration control.
     * @param source
     */
    public IsothermalSpecieConcentrationControlCSTR(IsothermalSpecieConcentrationControlCSTR source) {
        super(source);
        this.controlledSpecies = source.controlledSpecies;
        this.actuator = source.actuator.clone();
    }

    /**
     * Override of clone method in parent class.
     * @return deep copy of the object.
     */
    @Override
    public IsothermalSpecieConcentrationControlCSTR clone() {
        return new IsothermalSpecieConcentrationControlCSTR(this);
    }

    /* Accessors and Mutators */

    /**
     * Actuator accessor.
     * @return deep copy of actuator object.
     */
    public ControlElement getActuator()
    {
        return this.actuator.clone();
    }

    /**
     * Actuator mutator.
     * @param actuator SensorActuator object.
     */
    public void setActuator(ControlElement actuator)
    {
        if(actuator==null) throw new IllegalArgumentException("Attempted to pass a null actuator to a reactor object");
        this.actuator = actuator.clone();
    }

    /**
     * Controlled species accessor.
     * @return deep copy of controlled ChemicalSpecies object.
     */
    public ChemicalSpecies getControlledSpecies()
    {
        return this.controlledSpecies.clone();
    }

    /**
     * Controlled species mutator.
     * @param species SensorActuator object.
     */
    public void setControlledSpecies(ChemicalSpecies species)
    {
        if(species == null) throw new IllegalArgumentException("Attempted to pass a null control ChemicalSpecies to a reactor object");
        this.controlledSpecies = this.controlledSpecies.clone();
    }

    /**
     * Run CSTR for runTime amount of time, while control system is engaged.
     * @param dt time step for data recording purposes.
     * @param runTime total run time of a reactor.
     * @param reset flag whether state should be reset before running reactor, this will not reset outlet concentrations
     */
    @Override
    public void runForNTime(double dt, double runTime, boolean reset) {
        if(this.actuator.getDeadTime() < dt) throw new IllegalArgumentException("Dead time is larger then time" +
                "interval dt, wt which control system will be triggered. Lower dt");
        if(dt <= 0) throw new IllegalArgumentException("dt can not be negative or equal to 0");
        if(runTime < 0) throw new IllegalArgumentException("Run time can not be negative");
        if(reset) this.g_odeEngine.reset();
        double localTimeCounter = 0;
        while(localTimeCounter < runTime) {
            this.g_runData.addDataRow(formatDataRow(this.g_currentTime, this.outlet.mixture.getConcentrations()));
            this.outlet.mixture.setConcentrations(
                    this.g_odeEngine.integrate(this.g_currentTime, this.g_currentTime+dt, this.outlet.mixture.getConcentrations(),
                            this.generateDifferentialEquations()));
            this.actuator.trigger(this.g_currentTime, this);
            localTimeCounter += dt;
            this.g_currentTime += dt;
        }
    }
    public boolean equals(Object comparator)
    {
        if(!super.equals(comparator)) return false;
        IsothermalSpecieConcentrationControlCSTR obj = ((IsothermalSpecieConcentrationControlCSTR)comparator);
        if(!obj.actuator.equals(this.actuator)) return false;
        return true;
    }

    /**
     * Controllable interface implementation. Adjust controlled parameter.
     * @param value new parameter value.
     * @param id controller id. Not applicable for single specie concentration control system
     */
    @Override
    public void adjustControllableParameter(double value, int id) {
        if(value < 0) value = 0;
        super.inlet.setVolumetricFlowrate(value);
        super.outlet.setVolumetricFlowrate(value);
    }

    /**
     * Controllable interface implementation. Return value of controlled parameter.
     * @param id controller id. Not applicable for single specie concentration control system
     * @return value of controlled parameter
     */
    @Override
    public double getControllableParameter(int id) {
        return super.outlet.mixture.getConcentration(this.controlledSpecies);
    }

    /**
     * Perform step change in the control parameter.
     * @param value new value of control parameter.
     * @param id controller id.
     */
    @Override
    public void performStepChange(double value, int id) {
        super.inlet.mixture.setConcentration(this.controlledSpecies, value);
    }
}
