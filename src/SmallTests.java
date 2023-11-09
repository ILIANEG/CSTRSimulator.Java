import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.Function;

public class SmallTests {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException {
        ChemicalSpecies a = new ChemicalSpecies("A", 0);
        ChemicalSpecies b = new ChemicalSpecies("B", 0);
        Flow inlet = new Flow(new ChemicalMixture(new ChemicalSpecies[]{a, b}, new double[]{0.2, 0}), 0.05);
        ElementaryConstantKReaction reaction = new ElementaryConstantKReaction(new ChemicalSpecies[]{a}, new ChemicalSpecies[]{b},
                new double[]{-1}, new double[]{1}, 0.2);
        IsothermalUncontrolledTransientCSTR cstr = new IsothermalUncontrolledTransientCSTR(inlet, reaction, 1);
        Function f = cstr.generateDifferentialEquation(a);
        System.out.println(cstr.g_Outlet);
        cstr.run(1, 100);
        System.out.println(cstr);
    }
}
