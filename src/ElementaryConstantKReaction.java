import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;
import CHG4343_Design_Project_Mathematical.Function;

/**
 * Implements Abstract Reaction to represent elementary, irreversible reaction with constant k (k is not calculated based on
 * temperature, but instead is passed as a constant into the constructor). Applicable for isothermal processes ONLY.
 */
public class ElementaryConstantKReaction extends AbstractReaction {
    private double rateConstant;

    /**
     * Elementary constant k reaction constructor.
     * @see AbstractReaction
     */
    public ElementaryConstantKReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStochiometry,
                                       double[] productStochiometry, double rateConstant) throws ArrayException, NumericalException {
        super(reactants, products, reactantsStochiometry, productStochiometry);
        if(rateConstant < 0) throw new NumericalException("Rate constant can not be negative");
        // assigning rate constant
        this.rateConstant = rateConstant;
    }

    /**
     * Elementary constant k copy constructor.
     * @see AbstractReaction
     */
    public ElementaryConstantKReaction(ElementaryConstantKReaction source) {
        super(source);
        this.rateConstant = source.rateConstant;
    }

    /**
     * Implementation of clone method.
     * @return deep copy of chemical reaction.
     */
    public AbstractReaction clone() {
        return new ElementaryConstantKReaction(this);
    }

    /* Getters and Setters */

    /**
     * Implementation of calculate rate constant method. Constant k implementation simply returns rate constant.
     * @param mixture ChemicalMixture object.
     * @return rate constant
     */
    @Override
    public double calculateRateConstant(ChemicalMixture mixture) {
        return this.rateConstant;
    }

    /**
     * Rate constant setter.
     * @param rateConstant Rate constant with applicable units for a given elementary reaction.
     */
    public void setRateConstant(double rateConstant) {
        if(0 < rateConstant) throw new NumericalException("Rate constant can not be negative for an irreversible reaction");
        this.rateConstant = rateConstant;
    }

    /**
     * Implementation of reaction rate calculation.
     * @see AbstractReaction
     */
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

    /**
     * Implementation of reaction rate expression generation.
     * @see AbstractReaction
     */
    @Override
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
