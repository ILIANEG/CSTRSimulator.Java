package CHG4343_Design_Project_ODESolver;
public class Euler implements ODEStepper
{
	public double step(double hStart, double xStart, double yStart, Function dydx)
	{
		return dydx.evaluate(xStart,yStart)*hStart+yStart;
	}
}
		
	