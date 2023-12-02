package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

import java.util.Queue;

public class SensorActuator {
    private AbstractController controller;
    private Controllable controlledObject;
    private int controlID;
    public SensorActuator(Controllable controlledObject, AbstractController controller, int controlID) throws IllegalArgumentException {
        if(controlledObject == null) throw new IllegalArgumentException("Controlled object is null while initializing sensor/actuator");
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        this.controlledObject = controlledObject;
        this.controller = controller.clone();
        this.controlID = controlID;
    }

    public SensorActuator(SensorActuator source) throws IllegalArgumentException
    {
        if(source == null) throw new IllegalArgumentException("Source object in reactor copy constructor is null");
        this.controller = source.controller.clone();
        this.controlledObject = source.controlledObject;
        this.controlID = source.controlID;
    }

    public SensorActuator clone()
    {
        return new SensorActuator(this);
    }

    public void setController(AbstractController controller) throws IllegalArgumentException
    {
        if (controller==null) throw new IllegalArgumentException("Attempted to pass a null value to controller");
        this.controller=controller;
    }

    public AbstractController getController()
    {
        return this.controller;
    }
    public void setControlledObject(Controllable controlledObject) throws IllegalArgumentException
    {
        if(controlledObject==null) throw new IllegalArgumentException("Attempted to pass a null value as a controllable");
        this.controlledObject=controlledObject;
    }

    public Controllable getControlledObject()
    {
        return this.controlledObject;
    }

    public void setControlID(int controlID)
    {
        this.controlID = controlID;
    }

    public int getControlID()
    {
        return this.controlID;
    }

    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        SensorActuator obj = ((SensorActuator)comparator);
        if(obj.controlID != this.controlID) return false;
        if(obj.controlledObject.equals(this.controlledObject)) return false;
        if(obj.controller.equals(this.controller)) return false;
        return true;
    }
}
