package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public interface Controllable {
    void adjustControllableParameter(double value, int id) throws NumericalException;
    double getControllableParameter(int id);
}
