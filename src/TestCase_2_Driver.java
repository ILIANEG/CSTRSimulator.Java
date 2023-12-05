import CHG4343_Design_Project_ControlSystem.PIController;
import CHG4343_Design_Project_ControlSystem.SensorActuator;
import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.RK45;

import java.io.IOException;

public class TestCase_2_Driver {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException, IOException {
        ChemicalSpecies a = new ChemicalSpecies("A", 0);
        ChemicalSpecies b = new ChemicalSpecies("B", 0);

        ElementaryConstantKReaction reaction = new ElementaryConstantKReaction(new ChemicalSpecies[]{a}, new ChemicalSpecies[]{b},
                new double[]{-1}, new double[]{1}, 0.2);
        Flow inlet = new Flow(new ChemicalMixture(new ChemicalSpecies[]{a}, new double[]{0.2}), 0.05);
        Flow outlet = new Flow(0.05);
        SensorActuator sensor = new SensorActuator(new PIController(0.04, 0.64, 4, 0.2), 0.2, 0, 0);
        IsothermalSpecieConcentrationControlCSTR cstr = new IsothermalSpecieConcentrationControlCSTR(inlet, outlet, reaction, 1, sensor, a);
        cstr.run(0.01, 100, new RK45());
        System.out.println(cstr);
        cstr.getRuntimeData().writeToFile("/home/nilliax/Documents/testCase2.csv");
    }
}
