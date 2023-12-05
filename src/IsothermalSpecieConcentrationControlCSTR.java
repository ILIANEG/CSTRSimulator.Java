import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.AbstractODEStepper;

public class IsothermalSpecieConcentrationControlCSTR extends IsothermalUncontrolledTransientCSTR implements Controllable {
    private SensorActuator actuator;
    private ChemicalSpecies controlledSpecies;
    public IsothermalSpecieConcentrationControlCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, SensorActuator actuator, ChemicalSpecies controlledSpecies) throws NumericalException, ArrayException {
        super(inlet, outlet, reaction, volume);
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
    public void run(double h, double finalTime, AbstractODEStepper odeEngine) throws NumericalException, ArrayException {
        double t = 0;
        odeEngine.reset();
        odeEngine.setStepSize(0.01);
        super.g_runData.addDataRow(formatDataRow(t, this.outlet.mixture.getConcentrations()));
        while (t < finalTime) {
            super.integrate(t, t+h, odeEngine);
            t += h;
            System.out.println(this.outlet);
            this.actuator.trigger(t, this);
            super.g_runData.addDataRow(formatDataRow(t, this.outlet.mixture.getConcentrations()));
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
    public void adjustControllableParameter(double value, int id) throws NumericalException {
        switch(id) {
            case 0 :
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
}
