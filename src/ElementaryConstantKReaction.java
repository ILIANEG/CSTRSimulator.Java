import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Implements Abstract Reaction to represent elementary, irreversible reaction with constant k (k is not calculated based on
 * temperature, but instead is passed as a constant into the constructor). Applicable for isothermal processes ONLY.
 */
public class ElementaryConstantKReaction extends AbstractReaction {
    // TODO: Add descriptions, check methods
    private double rateConstant;
    public ElementaryConstantKReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                                       double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry);
        this.rateConstant = rateConstant;
    }
    public ElementaryConstantKReaction(ElementaryConstantKReaction source) {
        super(source);
        this.rateConstant = source.rateConstant;
    }
    public AbstractReaction clone() {
        return new ElementaryConstantKReaction(this);
    }
    @Override
    public double calculateRateConstant(ChemicalMixture mixture) {
        return this.rateConstant;
    }

    /**
     *
     * @param rateConstant Rate constant with applicable units for a given elementary reaction.
     * @throws NumericalException If negative rate constant is passed.
     */
    public void setRateConstant(double rateConstant) throws NumericalException {
        if(0 < rateConstant) throw new NumericalException("Rate constant can not be negative for an irreversible reaction");
        this.rateConstant = rateConstant;
    }
    @Override
    public double calculateReactionRate(ChemicalMixture mixture) {
        if(mixture == null) return 0;
        ChemicalSpecies[] reactants = super.getReactants();
        double[] reactantsStoichiometry = super.getReactantsStoichiometry();
        double reactionRate = this.calculateRateConstant(mixture);
        for(int i = 0; i < reactants.length; i++) {
            reactionRate *= Math.pow(mixture.getConcentration(reactants[i]), Math.abs(reactantsStoichiometry[i]));
        }
        return reactionRate;
    }
}
