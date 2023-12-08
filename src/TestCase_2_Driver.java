import CHG4343_Design_Project_ControlSystem.PIController;
import CHG4343_Design_Project_ControlSystem.ControlElement;
import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.RK45;

import java.io.IOException;
import java.util.Scanner;

public class TestCase_2_Driver {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException, IOException {
        ChemicalSpecies a = new ChemicalSpecies("A", 0);
        ChemicalSpecies b = new ChemicalSpecies("B", 0);

        ElementaryConstantKReaction reaction = new ElementaryConstantKReaction(new ChemicalSpecies[]{a}, new ChemicalSpecies[]{b},
                new double[]{-1}, new double[]{1}, 0.2);
        Flow inlet = new Flow(new ChemicalMixture(new ChemicalSpecies[]{a}, new double[]{0.2}), 0.05);
        Flow outlet = new Flow(0.05);
        ControlElement sensor = new ControlElement(new PIController(0.04, 0.64, 4, 0.2), 0.2, 0, 0);
        RK45 rk45 = new RK45(0.001, 1E-10, 1E-5, 100000);
        IsothermalSpecieConcentrationControlCSTR cstr = new IsothermalSpecieConcentrationControlCSTR(inlet, outlet, reaction, 1, rk45, sensor, a);
        cstr.runForNTime(0.1, 300, true);
        System.out.println(cstr);
        cstr.performStepChange(1.2, 0);
        cstr.runForNTime(0.1, 300, false);
        System.out.println(cstr);
        Scanner s = new Scanner(System.in);
        boolean exported = false;
        System.out.println("EXPORT DIALOG");
        while(!exported) {
            exported = true;
            System.out.print("Specify absolute path to the folder where file will be placed (for example C:/Users/usr/Documents/CHG4343_G9): ");
            String absolutePath = s.nextLine();
            try {
                absolutePath += "/test_case_2.csv";
                cstr.getRuntimeData().writeToFile(absolutePath);
            } catch (IOException e) {
                System.out.println("Path was not found, try again");
                exported = false;
            }
        }
    }
}
