import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_ODESolver.RK45;

public class TestDriver {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException {
        ChemicalSpecies a = new ChemicalSpecies("A", 0);
        ChemicalSpecies b = new ChemicalSpecies("B", 0);
        //ChemicalSpecies c = new ChemicalSpecies("C", 0);
        //ChemicalSpecies d = new ChemicalSpecies("D", 0);

        ElementaryConstantKReaction reaction = new ElementaryConstantKReaction(new ChemicalSpecies[]{a}, new ChemicalSpecies[]{b},
                new double[]{-1}, new double[]{1}, 0.2);
        Flow inlet = new Flow(new ChemicalMixture(new ChemicalSpecies[]{a}, new double[]{0.2}), 0.05);
        Flow outlet = new Flow(0.05);
        IsothermalUncontrolledTransientCSTR cstr = new IsothermalUncontrolledTransientCSTR(inlet, outlet, reaction, 1);
        cstr.run(0.001, 0, 1000, new RK45());
        System.out.println(cstr);
    }
}
