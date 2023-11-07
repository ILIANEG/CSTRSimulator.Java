package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class Actuator {
    private AbstractController controller;
    private Controllable controlParameter;
    public Actuator(Controllable controlParameter, AbstractController controller) throws NullPointerException, NumericalException {
        if(controller == null) throw new NullPointerException("Controller is null while initializing actuator");
        this.controlParameter = controlParameter;
        this.controller = controller.clone();
    }
}
