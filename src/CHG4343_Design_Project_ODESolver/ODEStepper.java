package CHG4343_Design_Project_ODESolver;

abstract class ODEStepper
{  
	public abstract double step(double h, double xStart, double yStart, Function dydx);
}
		
	