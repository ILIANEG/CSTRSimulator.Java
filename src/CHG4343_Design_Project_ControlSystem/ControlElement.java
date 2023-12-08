package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Class simulating control element.
 */
public class ControlElement {
    private AbstractController controller;
    private SignalQueue g_processAdjustments; // queue of control signals
    private Signal g_lastProcessedSignal;
    private double g_lastTimePolled;
    private double deadTime;
    private double pollingTime;
    private int id;

    /**
     * Control element constructor
     * @param controller
     * @param deadTime
     * @param pollingTime
     * @param id
     */
    public ControlElement(AbstractController controller, double deadTime, double pollingTime, int id) {
        if(controller == null) throw new IllegalArgumentException("Controller is null while initializing sensor/actuator");
        if(deadTime < 0) throw new NumericalException("Dead Time is less than zero");
        if(id < 0) throw new IllegalArgumentException("Actuator ID must be an integer between 0 and inf, while " + id + " was passed");
        this.controller = controller.clone();
        this.deadTime = deadTime;
        this.pollingTime = pollingTime;
        this.id = id;
        this.reset();
    }

    /**
     * Control element copy constructor
     * @param source
     * @throws IllegalArgumentException
     */
    public ControlElement(ControlElement source) {
        if(source == null) throw new IllegalArgumentException("Source object in reactor copy constructor is null");
        this.controller = source.controller.clone();
        this.g_processAdjustments = source.g_processAdjustments.clone();
        this.deadTime = source.deadTime;
        this.pollingTime = source.pollingTime;
        this.id = source.id;
        this.reset();
    }

    public ControlElement clone()
    {
        return new ControlElement(this);
    }

    /**
     * Reset global variables.
     */
    public void reset() {
        this.g_processAdjustments = new SignalQueue();
        this.g_lastProcessedSignal = null;
        this.g_lastTimePolled = -1;
    }

    /**
     * Controller mutator
     * @param controller
     */
    public void setController(AbstractController controller) {
        if (controller==null) throw new IllegalArgumentException("Attempted to pass a null value to controller");
        this.controller=controller.clone();
    }

    /**
     * Controller accessor.
     * @return deep copy of controller.
     */
    public AbstractController getController()
    {
        return this.controller.clone();
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
    public double getDeadTime() {
        return this.deadTime;
    }

    public void setPollingTime(double pollingTime) {
        if(0 < pollingTime) throw new IllegalArgumentException("polling time can not be 0");
        this.pollingTime = pollingTime;
    }

    public double getPollingTime() {
        return pollingTime;
    }

    /**
     * Trigger the control system.
     * @param time
     * @param controlledObject
     */
    public void trigger(double time, Controllable controlledObject) {
        // Check for signals that are waiting in queue. If a signal passed the dead time, actuate it immediately.
        if(!this.g_processAdjustments.isEmpty() && this.deadTime <= time - this.g_processAdjustments.peek().time) {
            Signal qSignal = this.g_processAdjustments.pop();
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
                this.g_processAdjustments.add(signal);
            }
            this.g_lastTimePolled = time;
        }
    }
    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        ControlElement obj = ((ControlElement)comparator);
        return obj.id == this.id && this.deadTime == obj.deadTime && this.controller.equals(obj.controller) &&
                this.g_processAdjustments.equals(obj.g_processAdjustments);
    }
}
