package ODESolver;

abstract class ODEStepper
{  
	public abstract double step(double h, double xStart, double yStart, Function dydx);
}
		
	