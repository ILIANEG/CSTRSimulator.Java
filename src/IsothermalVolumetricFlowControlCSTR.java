import CHG4343_Design_Project_ControlSystem.AbstractController;
import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class IsothermalVolumetricFlowControlCSTR extends IsothermalUncontrolledTransientCSTR implements Controllable {
    private SensorActuator controls;
    public IsothermalVolumetricFlowControlCSTR(Flow inlet, Flow outlet, AbstractReaction reaction, double volume, AbstractController controller) throws NumericalException {
        super(inlet, outlet, reaction, volume);
        this.controls = new SensorActuator(this, controller, 0);
    }

    public IsothermalVolumetricFlowControlCSTR(IsothermalVolumetricFlowControlCSTR source) throws NumericalException
    {
        super(source);
        this.controls = source.controls.clone();
    }
    public void setControls(AbstractController controller) throws IllegalArgumentException
    {
        if(controller==null) throw new IllegalArgumentException("Attempted to pass a null value to controller");
        this.controls = new SensorActuator(this, controller, 0);
    }

    public SensorActuator getControls()
    {
        return this.controls;
    }

    public boolean equals(Object comparator)
    {
        if(!super.equals(comparator)) return false;
        IsothermalVolumetricFlowControlCSTR obj = ((IsothermalVolumetricFlowControlCSTR)comparator);
        if(!obj.controls.equals(this.controls)) return false;
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
