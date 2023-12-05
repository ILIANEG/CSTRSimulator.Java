import CHG4343_Design_Project_ControlSystem.PIController;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.RK45;

import java.io.IOException;

public class Test_Case_1_Driver {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException, IOException {
        ChemicalSpecies a = new ChemicalSpecies("A", 0);
        ChemicalSpecies b = new ChemicalSpecies("B", 0);

        ElementaryConstantKReaction reaction = new ElementaryConstantKReaction(new ChemicalSpecies[]{a}, new ChemicalSpecies[]{b},
                new double[]{-1}, new double[]{1}, 0.2);
        Flow inlet = new Flow(new ChemicalMixture(new ChemicalSpecies[]{a}, new double[]{0.2}), 0.05);
        Flow outlet = new Flow(0.05);
        IsothermalUncontrolledTransientCSTR cstr = new IsothermalUncontrolledTransientCSTR(inlet, outlet, reaction, 1);
        cstr.run(0.01, 500, new RK45());
        System.out.println(cstr);
        cstr.getRuntimeData().writeToFile("/home/nilliax/Documents/testCase1.csv");
    }
}
