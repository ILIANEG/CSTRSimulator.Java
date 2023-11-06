import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ElementaryReaction extends AbstractReaction {
    public ElementaryReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                              double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry, rateConstant);
    }
    public ElementaryReaction(ElementaryReaction source) {
        super(source);
    }
    public AbstractReaction clone() {
        return new ElementaryReaction(this);
    }
    @Override
    public double calculateReactionRate(ChemicalMixture chemicalComposition) {
        if(chemicalComposition == null) return 0;
        ChemicalSpecies[] reactants = super.getReactants();
        double[] reactantsStoichiometry = getReactantsStoichiometry();
        double reactionRate = super.getRateConstant();
        for(int i = 0; i < reactants.length; i++) {
            reactionRate *= Math.pow(chemicalComposition.getConcentration(reactants[i]), reactantsStoichiometry[i]);
        }
        return reactionRate;
    }
}
