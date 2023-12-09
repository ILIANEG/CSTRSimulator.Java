import CHG4343_Design_Project_ODESolver.RK45;
import CHG4343_Design_Project_Mathematical.XYFunction;

public class Test_RK45 {
    public static void main(String[] args)
    {
        RK45 tester =new RK45(0.001,1E-5,1E-5,100000);
        XYFunction[] ODES = new XYFunction[1];
        ODES[0] = (double x, double[] y)-> 5;

        double[] y =new double[1];
        y[0]=50;
        double[] holder;
        holder= tester.integrate(0, 4, y, ODES); // integrated, divergent integral wont converge hence converge() not used (will throw max iterations error)
        System.out.println(holder[0]); // expected 70, since 50 = 5 * (4-0)
    }
}
