import CustomExcpetions.ArrayException;
import CustomExcpetions.NumericalException;

public class ElementaryReaction extends AbstractReaction {
    public ElementaryReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                              double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry, rateConstant);
    }

    @Override
    public double calculateReactionRate(ChemicalMixture chemicalComposition) {
        return 0;
    }
}
