import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.AbstractODEStepper;

public class IsothermalSpecieConcentrationControlCSTR extends IsothermalUncontrolledTransientCSTR implements Controllable {
    private SensorActuator actuator;
    private ChemicalSpecies controlledSpecies;
    public IsothermalSpecieConcentrationControlCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, AbstractODEStepper odeEngine,
                                                    SensorActuator actuator, ChemicalSpecies controlledSpecies){
        super(inlet, outlet, reaction, volume, odeEngine);
        this.controlledSpecies = controlledSpecies.clone();
        this.actuator = actuator.clone();

    }
    public IsothermalSpecieConcentrationControlCSTR(IsothermalSpecieConcentrationControlCSTR source) throws NumericalException, ArrayException {
        super(source);
        this.controlledSpecies = source.controlledSpecies;
        this.actuator = source.actuator.clone();
    }
    public void setControls(SensorActuator actuator) throws IllegalArgumentException
    {
        if(actuator==null) throw new IllegalArgumentException("Attempted to pass a null actuator to reactor object");
        this.actuator = actuator.clone();
    }

    public SensorActuator getControls()
    {
        return this.actuator;
    }

    @Override
    public void run(double dt, double runTime) {
        //this.g_odeEngine.reset();
        while(this.g_currentTime < runTime) {
            this.g_runData.addDataRow(formatDataRow(this.g_currentTime, this.outlet.mixture.getConcentrations()));
            this.outlet.mixture.setConcentrations(
                    this.g_odeEngine.integrate(this.g_currentTime, this.g_currentTime+dt, this.outlet.mixture.getConcentrations(),
                            this.generateDifferentialEquations()));
            this.actuator.trigger(this.g_currentTime, this);
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

    @Override
    public void adjustControllableParameter(double value, int id) {
        switch(id) {
            case 0 :
                if(value < 0) value = 0;
                super.inlet.setVolumetricFlowrate(value);
                super.outlet.setVolumetricFlowrate(value);
        }
    }

    @Override
    public double getControllableParameter(int id) {
        switch(id) {
            case 0:
                return super.outlet.mixture.getConcentration(this.controlledSpecies);
        }
        return 0;
    }
    @Override
    public void performStepChange(double value, int id) {
        switch(id) {
            case 0:
                super.inlet.mixture.setConcentration(this.controlledSpecies, value);
        }
    }
}
