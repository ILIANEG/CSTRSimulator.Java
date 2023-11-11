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
        ChemicalSpecies[] reactants = super.getReactants();
        double[] reactantsStoichiometry = super.getReactantsStoichiometry();
        double reactionRate = this.rateConstant;
        for(int i = 0; i < reactants.length; i++) {
            reactionRate *= Math.pow(mixture.getConcentration(reactants[i]), Math.abs(reactantsStoichiometry[i]));
        }
        return reactionRate;
    }
    /*public Function generateSingleSpecieRateExpression(ChemicalSpecies species, ChemicalMixture mixture) {
        if(this.getStoichiometry(species) != 0) {
            if(this.isReactant(species)) {
                if(Math.pow(mixture.getConcentration(species), Math.abs(this.getStoichiometry(species))) == 0) {
                    return x -> this.calculateReactionRate(mixture) * Math.pow(x, Math.abs(this.getStoichiometry(species)));
                } else return x -> this.calculateReactionRate(mixture) /
                        Math.pow(mixture.getConcentration(species), Math.abs(this.getStoichiometry(species)))
                        * Math.pow(x, Math.abs(this.getStoichiometry(species)));

            }
            else return x -> this.calculateReactionRate(mixture);
        } else return (x -> 0);
    }*/
    public Function generateRateExpression(ChemicalMixture mixture) {
        ChemicalSpecies[] tmpSpecies = mixture.getSpecies();
        final double rateConstant = this.rateConstant;
        return (concentrations -> {
            double r = rateConstant;
            String tmpString = "";
            for(int i = 0; i < tmpSpecies.length; i++) {
                if(this.getStoichiometry(tmpSpecies[i]) == 0 || !this.isReactant(tmpSpecies[i])) {
                    tmpString += r + " * " + "1";
                    r *= 1;
                }
                else {
                    tmpString += r + " * " + concentrations[i] + "^" + Math.abs(this.getStoichiometry(tmpSpecies[i]));
                    r *= Math.pow(concentrations[i], Math.abs(this.getStoichiometry(tmpSpecies[i])));
                }
            }
            return r;
        });
    }
}
