package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class Actuator {
    private AbstractController controller;
    private double deadTime;
    private Controllable controlParameter;
    public Actuator(Controllable controlParameter, AbstractController controller, double deadTime) throws NullPointerException, NumericalException {
        if(controller == null) throw new NullPointerException("Controller is null while initializing actuator");
        if(deadTime < 0) throw new NumericalException("Dead time can not be negative");
        this.controlParameter = controlParameter;
        this.controller = controller.clone();
        this.deadTime = deadTime;
    }
}
