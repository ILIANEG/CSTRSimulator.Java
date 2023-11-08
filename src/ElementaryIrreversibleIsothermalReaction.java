import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class ElementaryIrreversibleIsothermalReaction extends AbstractReaction {
    // TODO: Add descriptions, check methods
    private double rateConstant;
    public ElementaryIrreversibleIsothermalReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                                                    double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry);
        this.rateConstant = rateConstant;
    }
    public ElementaryIrreversibleIsothermalReaction(ElementaryIrreversibleIsothermalReaction source) {
        super(source);
        this.rateConstant = source.rateConstant;
    }
    public AbstractReaction clone() {
        return new ElementaryIrreversibleIsothermalReaction(this);
    }
    @Override
    public double getRateConstant() {
        return this.rateConstant;
    }
    public void setRateConstant(double rateConstant) throws NumericalException {
        if(0 < rateConstant) throw new NumericalException("Rate constant can not be negative for an irreversible reaction");
        this.rateConstant = rateConstant;
    }
    @Override
    public double calculateRateConstant(double T) {
        return this.rateConstant;
    }
    @Override
    public double calculateReactionRate(ChemicalMixture chemicalComposition) {
        if(chemicalComposition == null) return 0;
        ChemicalSpecies[] reactants = super.getReactants();
        double[] reactantsStoichiometry = super.getReactantsStoichiometry();
        double reactionRate = this.getRateConstant();
        for(int i = 0; i < reactants.length; i++) {
            reactionRate *= Math.pow(chemicalComposition.getConcentration(reactants[i]), reactantsStoichiometry[i]);
        }
        return reactionRate;
    }
}
