package CHG4343_Design_Project_ControlSystem;

public abstract class AbstractController {
    private double setPoint;
    private double deadTime;
    public abstract AbstractController clone();

    public double e(double value) {
        return setPoint = value;
    }
    public abstract void tune(double k, double tau, double theta);
    public abstract double controlSignal(double value);
    public void setSetPoint(double setPoint) throws NumericalException
    {
        if (setPoint<0) throw new NumericalException("Attempted to set SetPoint to a negative value.");
        this.setPoint=setPoint;
    }
    public double getSetPoint()
    {
        return this.setPoint;
    }

    public void setDeadTime(double deadTime) throws NumericalException
    {
        if(deadTime < 0) throw new NumericalException("Attempted to set dead time to a negative value.");
        this.deadTime = deadTime;
    }

    public double getDeadTime()
    {
        return this.deadTime;
    }

    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        AbstractController obj = ((AbstractController)comparator);
        if(obj.setPoint != this.setPoint) return false;
        if(obj.deadTime != this.deadTime) return false;
        return true;
    }

}

