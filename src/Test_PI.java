import CHG4343_Design_Project_ControlSystem.PIController;

public class Test_PI {
    public static void main(String[] args)
    {
        double value=0.08424;
        double SetPoint=0.04;
        double K=0.64;
        double Tau=4.0;
        double theta=0.2;
        double t=0.1;
        double tprev=0;
        double Iprev=0;
        PIController test = new PIController(SetPoint,K,Tau,theta);
        System.out.println("P:\t"+test.calculateP(1/K * (0.9*Tau / theta + 1./12.),SetPoint-value));
        System.out.println("I:\t"+test.calculateI(1/K * (0.9*Tau / theta + 1./12.),theta * ((30 + 3* theta/Tau)/(9 + 20 * theta/Tau)),SetPoint-value,t,tprev,Iprev));
        System.out.println("PI:\t"+test.calculateControlSignal(t,value));
    }
}
