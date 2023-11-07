package CHG4343_Design_Project_ControlSystem;

public abstract class AbstractController {
    private double setPoint;
    private double deadTime;
    public abstract AbstractController clone();
}
