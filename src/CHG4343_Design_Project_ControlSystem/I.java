package CHG4343_Design_Project_ControlSystem;

public interface I
{
    public double calculateI(double kc, double tauI, double e, double t, double tPrev, double iPrev);
}


