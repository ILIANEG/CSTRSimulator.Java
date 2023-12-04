import CHG4343_Design_Project_ControlSystem.AbstractController;
import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.AbstractODEStepper;

public class IsothermalVolumetricFlowControlCSTR extends IsothermalUncontrolledTransientCSTR implements Controllable {
    private SensorActuator actuator;
    public IsothermalVolumetricFlowControlCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, SensorActuator actuator) throws NumericalException {
        super(inlet, outlet, reaction, volume);
        this.actuator = actuator.clone();

    }

    public IsothermalVolumetricFlowControlCSTR(IsothermalVolumetricFlowControlCSTR source) throws NumericalException
    {
        super(source);
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
    public void run(double h, double initialTime, double runTime, AbstractODEStepper odeEngine) throws NumericalException {

    }

    public boolean equals(Object comparator)
    {
        if(!super.equals(comparator)) return false;
        IsothermalVolumetricFlowControlCSTR obj = ((IsothermalVolumetricFlowControlCSTR)comparator);
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
                super.inlet.getVolumetricFlowrate();
        }
        return super.inlet.getVolumetricFlowrate();
    }
}
