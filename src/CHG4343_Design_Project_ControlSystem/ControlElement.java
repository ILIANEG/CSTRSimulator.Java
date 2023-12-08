package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Class simulating both sensor and actuator simultaneously.
 */
public class ControlElement {
    private AbstractController controller;
    private SignalQueue processAdjustments; // queue of control signals
    private Signal g_lastProcessedSignal;
    private double g_lastTimePolled;
    private SignalQueue setPointChanges;
    private double deadTime;
    private double pollingTime;
    private int id;
    public ControlElement(AbstractController controller, double deadTime, double pollingTime, int id) throws NumericalException {
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        if(deadTime < 0) throw new NumericalException("Dead Time is less than zero");
        if(id < 0) throw new IllegalArgumentException("Actuator ID must be an integer between 0 and inf, while " + id + " was passed");
        this.controller = controller.clone();
        this.processAdjustments = new SignalQueue();
        this.setPointChanges = new SignalQueue();
        this.deadTime = deadTime;
        this.pollingTime = pollingTime;
        this.id = id;
        this.reset();
    }
    public ControlElement(AbstractController controller, double deadTime, double pollingTime, int id, SignalQueue setPointChanges) throws NumericalException {
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        if(deadTime < 0) throw new NumericalException("Dead Time is less than zero");
        if(id < 0) throw new IllegalArgumentException("Actuator ID must be an integer between 0 and inf, while " + id + " was passed");
        this.controller = controller.clone();
        this.processAdjustments = new SignalQueue();
        if(setPointChanges == null) this.setPointChanges = new SignalQueue();
        else setPointChanges.clone();
        this.deadTime = deadTime;
        this.pollingTime = pollingTime;
        this.id = id;
        this.reset();
    }
    public ControlElement(ControlElement source) throws IllegalArgumentException
    {
        if(source == null) throw new IllegalArgumentException("Source object in reactor copy constructor is null");
        this.controller = source.controller.clone();
        this.processAdjustments = source.processAdjustments.clone();
        this.setPointChanges = source.setPointChanges.clone();
        this.deadTime = source.deadTime;
        this.pollingTime = source.pollingTime;
        this.id = source.id;
        this.reset();
    }

    public ControlElement clone()
    {
        return new ControlElement(this);
    }
    public void reset() {
        this.g_lastProcessedSignal = null;
        this.g_lastTimePolled = -1;
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
        // Check for signals that are waiting in queue. If a signal passed the dead time, actuate it immediately.
        if(!this.processAdjustments.isEmpty() && this.deadTime <= time - this.processAdjustments.peek().time) {
            Signal qSignal = this.processAdjustments.pop();
            controlledObject.adjustControllableParameter(qSignal.value, this.id);
            this.g_lastProcessedSignal = qSignal;
        }
        // Check if sensor is available to read (polling time)
        if(this.g_lastTimePolled == -1 || pollingTime <= time - g_lastTimePolled) {
            // Check if signal can be actuated immediately
            if(this.g_lastProcessedSignal == null || this.deadTime <= time - g_lastProcessedSignal.time) {
                Signal signal = new Signal(time,
                        this.controller.calculateControlSignal(time, controlledObject.getControllableParameter(this.id)));
                controlledObject.adjustControllableParameter(signal.value, this.id);
                this.g_lastProcessedSignal = signal;
            } else {
                // Add signal to a queue if signal can not be actuated immediately
                Signal signal = new Signal(time,
                        this.controller.calculateControlSignal(time, controlledObject.getControllableParameter(this.id)));
                this.processAdjustments.add(signal);
            }
            this.g_lastTimePolled = time;
        }
    }
    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        ControlElement obj = ((ControlElement)comparator);
        return obj.id == this.id && this.deadTime == obj.deadTime && this.controller.equals(obj.controller) &&
                this.processAdjustments.equals(obj.processAdjustments) && this.setPointChanges.equals(obj.setPointChanges);
    }
}
