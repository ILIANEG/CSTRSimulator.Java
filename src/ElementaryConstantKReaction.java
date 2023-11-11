import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.LengthMismatch;
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
        ChemicalSpecies[] reactants = this.getReactants();
        double[] reactantsStoichiometry = this.getReactantsStoichiometry();
        double reactionRate = this.rateConstant;
        for(int i = 0; i < reactants.length; i++) {
            reactionRate *= Math.pow(mixture.getConcentration(reactants[i]), Math.abs(reactantsStoichiometry[i]));
        }
        return reactionRate;
    }
    public Function generateRateExpression(ChemicalMixture mixture) {
        return (concentrations -> {
            ChemicalSpecies[] s = mixture.getSpecies();
            double r = this.rateConstant;
            for(int i = 0; i < s.length; i++) {
                if(this.getStoichiometry(s[i]) == 0 || !this.isReactant(s[i])) r *= 1;
                else r *= Math.pow(concentrations[i], Math.abs(this.getStoichiometry(s[i])));

            }
            return r;
        });
    }
}
