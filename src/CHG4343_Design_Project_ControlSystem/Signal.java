package CHG4343_Design_Project_ControlSystem;

public class Signal {
    public final double time;
    public final double value;

    public Signal(double time, double value)
    {
        this.time = time;
        this.value = value;
    }
    public Signal(Signal source) throws NullPointerException
    {
        if(source == null) throw new NullPointerException("Source object in Signal copy constructor is null.");
        this.time = source.time;
        this.value = source.value;
    }

    public Signal clone()
    {
        return new Signal(this);
    }

    public double getTime()
    {
        return this.time;
    }

    public double getValue()
    {
        return this.value;
    }

    public boolean equals(Object comparator)
    {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        if(((Signal)comparator).time!= this.time) return false;
        if(((Signal)comparator).value != this.time) return false;
        return true;
    }
}
