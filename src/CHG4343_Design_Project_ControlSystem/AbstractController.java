package CHG4343_Design_Project_ControlSystem;

import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public abstract class AbstractController {
    private double setPoint;;

    public AbstractController(double setPoint) throws NumericalException
    {
        if(setPoint < 0) throw new NumericalException("Set Point is less than zero");
        this.setPoint = setPoint;
    }

    public AbstractController(AbstractController source) throws IllegalArgumentException
    {
        if(source == null) throw new IllegalArgumentException("Source object in reactor copy constructor is null");
        this.setPoint = source.setPoint;
    }

    public abstract AbstractController clone();

    public double e(double value) {
        return setPoint = value;
    }
    public abstract void tune(double k, double tau, double theta);
    public abstract double calculateControlSignal(double t, double value);
    public void setSetPoint(double setPoint) throws NumericalException
    {
        if (setPoint<0) throw new NumericalException("Attempted to set SetPoint to a negative value.");
        this.setPoint=setPoint;
    }
    public double getSetPoint()
    {
        return this.setPoint;
    }

    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        AbstractController obj = ((AbstractController)comparator);
        return obj.setPoint == this.setPoint;
    }

}

