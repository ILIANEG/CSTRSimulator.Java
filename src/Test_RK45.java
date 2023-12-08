import CHG4343_Design_Project_ODESolver.RK45;
import CHG4343_Design_Project_Mathematical.XYFunction;

public class Test_RK45 {
    public static void main(String[] args)
    {
        RK45 tester =new RK45(0.01,1E-8,1E-8,100000);
        XYFunction[] ODES = new XYFunction[1];
        ODES[0] = (double x, double[] y)-> 9.8-0.196*y[0];

        double[] y =new double[1];
        y[0]=1;
        double[] holder;
        holder=tester.converge(1,y,ODES);
        System.out.println(holder[0]);
    }
}
