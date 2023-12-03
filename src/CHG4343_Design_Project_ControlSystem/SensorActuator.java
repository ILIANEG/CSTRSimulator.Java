package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

import java.util.Queue;

public class SensorActuator {
    private AbstractController controller;
    private Controllable controlledObject;
    private SignalQueue processAdjustments;
    private Signal g_lastProcessedSignal;
    private SignalQueue setPointChanges;
    private int controlID;
    public SensorActuator(Controllable controlledObject, AbstractController controller, int controlID) throws IllegalArgumentException {
        if(controlledObject == null) throw new IllegalArgumentException("Controlled object is null while initializing sensor/actuator");
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        this.controlledObject = controlledObject;
        this.controller = controller.clone();
        this.controlID = controlID;
        this.processAdjustments = new SignalQueue();
        this.setPointChanges = new SignalQueue();
    }
    public SensorActuator(Controllable controlledObject, AbstractController controller, int controlID, SignalQueue disturbances, SignalQueue setPointChanges) {
        if(controlledObject == null) throw new IllegalArgumentException("Controlled object is null while initializing sensor/actuator");
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        this.controlledObject = controlledObject;
        this.controller = controller.clone();
        this.controlID = controlID;
        this.processAdjustments = new SignalQueue();
        this.setPointChanges = setPointChanges.clone();
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
    public void reset() {
        g_lastProcessedSignal = null;
    }
    public void setController(AbstractController controller) throws IllegalArgumentException
    {
        if (controller==null) throw new IllegalArgumentException("Attempted to pass a null value to controller");
        this.controller=controller.clone();
    }

    public AbstractController getController()
    {
        return this.controller.clone();
    }
    public void setControlledObject(Controllable controlledObject) throws IllegalArgumentException
    {
        if(controlledObject==null) throw new IllegalArgumentException("Attempted to pass a null value as a controllable");
        this.controlledObject=controlledObject;
    }
    public SignalQueue getSetPointChanges() {
        return this.setPointChanges.clone();
    }
    public void setSignalQueue(SignalQueue signalQueue) {
        if(signalQueue == null) throw new IllegalArgumentException("Attempting to assign null as set point change signal queue");
        this.setPointChanges = signalQueue.clone();
    }
    public void setControlID(int controlID)
    {
        this.controlID = controlID;
    }
    public int getControlID()
    {
        return this.controlID;
    }
    /*public void trigger(double t, Controllable controlledObject) throws NumericalException {
        double controlSignal = this.controller.calculateControlSignal(t, controlledObject.getControllableParameter(this.controlID));
        Signal signal = new Signal(t, controlSignal);
        if(g_lastProcessedSignal == null) {
            controlledObject.adjustControllableParameter(controlSignal, this.controlID);
        } else if (< processAdjustments.checkLastTime())
    }*/
    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        SensorActuator obj = ((SensorActuator)comparator);
        return obj.controlID == this.controlID && this.controlledObject.equals(obj.controlledObject)
                && this.controller.equals(obj.controller) && this.processAdjustments.equals(obj.processAdjustments)
                && this.setPointChanges.equals(obj.setPointChanges);
    }
}
