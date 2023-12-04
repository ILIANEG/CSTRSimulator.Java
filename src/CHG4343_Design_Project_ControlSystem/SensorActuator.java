package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class SensorActuator {
    private AbstractController controller;
    private SignalQueue processAdjustments;
    private Signal g_lastProcessedSignal;
    private SignalQueue setPointChanges;
    private double deadTime;
    private int id;
    public SensorActuator(AbstractController controller, double deadTime, int id) throws IllegalArgumentException, NumericalException {
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        if(deadTime < 0) throw new NumericalException("Dead Time is less than zero");
        if(id < 0) throw new IllegalArgumentException("Actuator ID must be an integer between 0 and inf, while " + id + " was passed");
        this.controller = controller.clone();
        this.processAdjustments = new SignalQueue();
        this.setPointChanges = new SignalQueue();
        this.deadTime = deadTime;
        this.id = id;
        this.reset();
    }
    public SensorActuator(AbstractController controller, double deadTime, int id, SignalQueue disturbances, SignalQueue setPointChanges) throws NumericalException {
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        if(deadTime < 0) throw new NumericalException("Dead Time is less than zero");
        if(id < 0) throw new IllegalArgumentException("Actuator ID must be an integer between 0 and inf, while " + id + " was passed");
        this.controller = controller.clone();
        this.processAdjustments = new SignalQueue();
        if(setPointChanges == null) this.setPointChanges = new SignalQueue();
        else setPointChanges.clone();
        this.deadTime = deadTime;
        this.id = id;
        this.reset();
    }
    public SensorActuator(SensorActuator source) throws IllegalArgumentException
    {
        if(source == null) throw new IllegalArgumentException("Source object in reactor copy constructor is null");
        this.controller = source.controller.clone();
        this.setPointChanges = source.setPointChanges.clone();
        this.deadTime = source.deadTime;
        this.id = source.id;
        this.reset();
    }

    public SensorActuator clone()
    {
        return new SensorActuator(this);
    }
    public void reset() {
        this.g_lastProcessedSignal = null;
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
    public SignalQueue getSetPointChanges() {
        return this.setPointChanges.clone();
    }
    public void setSignalQueue(SignalQueue signalQueue) {
        if(signalQueue == null) throw new IllegalArgumentException("Attempting to assign null as set point change signal queue");
        this.setPointChanges = signalQueue.clone();
    }
    public void setId(int id)
    {
        if(id < 0) throw new IllegalArgumentException("Actuator id can not be negative");
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }
    public void setDeadTime(double deadTime) {
        if(deadTime < 0) throw new IllegalArgumentException("Dead time can not be negative");
        this.deadTime = deadTime;
    }
    public double getDeadTime(double deadTime) {
        return this.deadTime;
    }
    public void trigger(double time, Controllable controlledObject) throws NumericalException {
        if(!this.setPointChanges.isEmpty() && time <= this.setPointChanges.peek().time) {
            this.controller.setSetPoint(this.setPointChanges.pop().value);
        }
        double value = this.controller.calculateControlSignal(time, controlledObject.getControllableParameter(this.id));
        Signal currSignal = new Signal(time, value);
        if(g_lastProcessedSignal == null) {
            controlledObject.adjustControllableParameter(value, this.id);
            g_lastProcessedSignal = currSignal;
        } else if (this.deadTime <= time - g_lastProcessedSignal.time) {
            Signal qSignal = this.processAdjustments.pop();
            controlledObject.adjustControllableParameter(qSignal.value, this.id);
            this.processAdjustments.add(currSignal);
            g_lastProcessedSignal = qSignal;
        } else this.processAdjustments.add(currSignal);
    }
    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        SensorActuator obj = ((SensorActuator)comparator);
        return obj.id == this.id && this.deadTime == obj.deadTime && this.controller.equals(obj.controller) &&
                this.processAdjustments.equals(obj.processAdjustments) && this.setPointChanges.equals(obj.setPointChanges);
    }
}
