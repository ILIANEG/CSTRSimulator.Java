package CHG4343_Design_Project_ControlSystem;

public interface Controllable {
    void adjustControllableParameter(double signal);
    double getControllableParameter();
}
