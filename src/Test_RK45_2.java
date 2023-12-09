import CHG4343_Design_Project_Mathematical.XYFunction;
import CHG4343_Design_Project_ODESolver.RK45;

public class Test_RK45_2 {
    public static void main(String[] args)
    {
        RK45 tester =new RK45(0.001,1E-8,1E-7,100000);
        XYFunction[] ODES = new XYFunction[1];
        ODES[0] = (double x, double[] y)-> 9.8 - 0.196*y[0];

        double[] y =new double[1];
        y[0]=48;
        double[] holder = tester.converge(0, y, ODES); // integral is convergent
        System.out.println(holder[0]); // analytical solution: 50-2*e^(-0.196*t) expected: 50
        holder = tester.integrate(0, 4, y, ODES); //
        System.out.println(holder[0]); // analytical solution: 50-2*e^(-0.196*t) expected: expected: 49.086
    }
}
