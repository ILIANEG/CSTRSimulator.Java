import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ElementaryIrreversibleReaction extends AbstractReaction {
    public ElementaryIrreversibleReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                                          double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry, rateConstant);
    }
    public ElementaryIrreversibleReaction(ElementaryIrreversibleReaction source) {
        super(source);
    }
    public AbstractReaction clone() {
        return new ElementaryIrreversibleReaction(this);
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
