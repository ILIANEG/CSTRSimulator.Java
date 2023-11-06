package CHG4343_Design_Project_ODESolver;
public class Euler extends ODEStepper
{
	public double step(double hStart, double xStart, double yStart, Function dydx)
	{
		return dydx.evaluate(xStart,yStart)*hStart+yStart;
	}
}
		
	