import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidNotationException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class SmallTests {
    public static void main(String[] argv) throws NumericalException, InvalidNotationException, ArrayException {
        ChemicalMixture mix = new ChemicalMixture(
                new ChemicalSpecies[] {new ChemicalSpecies("A", 0), new ChemicalSpecies("B", 0)},
                new double[] {1, 0});
        mix.addSpecies(new ChemicalSpecies("C", 0), 2);
        //mix.addMultipleSpecies(null, null);
        mix.addMultipleSpecies(new ChemicalSpecies[]{new ChemicalSpecies("D", 0), new ChemicalSpecies("E", 0)}, new double[] {0.1, 0.5});
        Flow flow = new Flow(mix, 0.1);
        flow.mixture.removeSpecies(new ChemicalSpecies("C", 0));
        ElementaryIrreversibleIsothermalReaction r = new ElementaryIrreversibleIsothermalReaction(
                new ChemicalSpecies[] {new ChemicalSpecies("A", 0), new ChemicalSpecies("B", 0)},
                new ChemicalSpecies[] {new ChemicalSpecies("B", 0), new ChemicalSpecies("C", 0)},
                new double[]{-1, -1}, new double[]{1,1}, 0.1);
        IsothermalUncontrolledTransientCSTR reactor = new IsothermalUncontrolledTransientCSTR(flow, r, 1);
        System.out.println(reactor);
    }
}
