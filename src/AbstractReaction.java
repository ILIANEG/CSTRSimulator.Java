import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidArrayDataException;
import CHG4343_Design_Project_CustomExcpetions.LengthMismatch;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public abstract class AbstractReaction {
    /* TODO prevent adding same species in reactants and products
    *  TODO add more detailed comments */
    /* All variables announced as final since they should not change over the lifetime of reaction object
    * In other words, variables do not represent state, but rather "immutable characteristics" of the reaction. */
    private final ChemicalSpecies[] reactants;
    private final ChemicalSpecies[] products;
    private final double[] reactantsStoichiometry; // Stored as negative values
    private final double[] productsStoichiometry; // Stored as positive values

    /**
     *
     * @param reactants Array of ChemicalSpecies representing reactants.
     * @param products Array of ChemicalSpecies representing products.
     * @param reactantsStoichiometry Array of doubles representing reactants stoichiometric ratios.
     * @param productsStoichiometry Array of doubles representing product stoichiometric ratios.
     * @throws ArrayException If an array or an element of the array is invalid.
     */
    public AbstractReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStoichiometry,
                            double[] productsStoichiometry) throws ArrayException {
        // Data validation.
        AbstractReaction.validateData(reactants, products, reactantsStoichiometry, productsStoichiometry);
        this.reactants = new ChemicalSpecies[reactants.length];
        this.products = new ChemicalSpecies[products.length];
        this.reactantsStoichiometry = new double[reactantsStoichiometry.length];
        this.productsStoichiometry = new double[productsStoichiometry.length];
        for(int i = 0; i < reactants.length; i++) {
            this.reactants[i] = reactants[i].clone();
            this.reactantsStoichiometry[i] = reactantsStoichiometry[i];
        }
        for(int i = 0; i < products.length; i++) {
            this.products[i] = products[i].clone();
            this.productsStoichiometry[i] = productsStoichiometry[i];
        }


    }
    public AbstractReaction(AbstractReaction source) throws NullPointerException {
        if(source == null) throw new NullPointerException("Source object in copy constructor of Reaction is null");
        this.reactants = new ChemicalSpecies[source.reactants.length];
        this.products = new ChemicalSpecies[source.products.length];
        this.reactantsStoichiometry = new double[source.reactantsStoichiometry.length];
        this.productsStoichiometry = new double[source.productsStoichiometry.length];
        for(int i = 0; i < this.reactants.length; i++) {
            this.reactants[i] = source.reactants[i].clone();
            this.reactantsStoichiometry[i] = source.reactantsStoichiometry[i];
        }
        for(int i = 0; i < this.products.length; i++) {
            this.products[i] = source.products[i].clone();
            this.productsStoichiometry[i] = source.productsStoichiometry[i];
        }
    }
    abstract public AbstractReaction clone();

    /* Accessor and Mutators */

    public ChemicalSpecies[] getReactants() {
        ChemicalSpecies[] tmpReactants = new ChemicalSpecies[this.reactants.length];
        for(int i = 0; i < this.reactants.length; i++) {
            tmpReactants[i] = this.reactants[i].clone();
        }
        return tmpReactants;
    }
    public ChemicalSpecies[] getProducts() {
        ChemicalSpecies[] tmpProducts = new ChemicalSpecies[this.products.length];
        for(int i = 0; i < this.products.length; i++) {
            tmpProducts[i] = this.products[i].clone();
        }
        return tmpProducts;
    }
    public double[] getReactantsStoichiometry() {
        double[] tmpReactantsStoichiometry = new double[this.reactantsStoichiometry.length];
        for(int i = 0; i < this.reactantsStoichiometry.length; i++) {
            tmpReactantsStoichiometry[i] = this.reactantsStoichiometry[i];
        }
        return tmpReactantsStoichiometry;
    }
    public double[] getProductsStoichiometry() {
        double[] tmpProductsStoichiometry = new double[this.reactantsStoichiometry.length];
        for(int i = 0; i < this.productsStoichiometry.length; i++) {
            tmpProductsStoichiometry[i] = this.productsStoichiometry[i];
        }
        return tmpProductsStoichiometry;
    }
    public double getStoichiometry(ChemicalSpecies species) {
        for(int i = 0; i < this.products.length; i++) {
            if(this.products[i].equals(species)) return this.productsStoichiometry[i];
        }
        for(int i = 0; i < this.reactants.length; i++) {
            if(this.reactants[i].equals(species)) return this.reactantsStoichiometry[i];
        }
        return 0;
    }
    public boolean isReactant(ChemicalSpecies species) {
        for(int i = 0; i < this.reactants.length; i++) {
            if(this.reactants[i].equals(species)) return true;
        }
        return false;
    }
    public abstract double calculateRateConstant(ChemicalMixture mixture);
    public abstract double calculateReactionRate(ChemicalMixture mixture);
    public abstract Function generateRateExpression(ChemicalMixture mixture);
    /**
     * Custom validator
     */
    private static void validateData(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStoichiometry,
                                     double[] productsStoichiometry) throws ArrayException {
        if (reactants == null) throw new ArrayException("List of reactants was found to be null in " +
                    "reaction object.", new NullPointerException("Array reactants is null"));
        if (products == null) throw new ArrayException("List of products was found to be null in " +
                    "reaction object.", new NullPointerException("Array products is null"));
        if (reactantsStoichiometry == null) throw new ArrayException("List of reactant stoichiometries was found to be null in " +
                    "in reaction object.", new NullPointerException("Array reactantsStoichiometry is null"));
        if (productsStoichiometry == null) throw new ArrayException("List of product stoichiometries was found to be null in " +
                    "in reaction object.", new NullPointerException("Array productsStoichiometry is null"));
        if (reactants.length != reactantsStoichiometry.length) {
            throw new ArrayException("Length of array reactants does not match array reactant stoichiometry.",
                    new LengthMismatch("Related arrays' lengths mismatch.", reactants.length, reactantsStoichiometry.length));
        }
        if (products.length != productsStoichiometry.length) {
            throw new ArrayException("Length of array products does not match array reactant stoichiometry.",
                    new LengthMismatch("Related arrays' lengths mismatch.", products.length, productsStoichiometry.length));
        }
        for (int i = 0; i < reactants.length; i++) {
            if (reactants[i] == null)
                throw new ArrayException("Reactant at index " + i + " is null.", new InvalidArrayDataException(i));
            //add here one for the others since they are arrays of objects
            if (0 < reactantsStoichiometry[i]) throw new ArrayException("Stoichiometry at index " + i + " is invalid.",
                    new InvalidArrayDataException(new NumericalException("Positive stoichiometry value for reactants are not allowed."), i));
        }
        for (int i = 0; i < products.length; i++) {
            if (products[i] == null)
                throw new ArrayException("Products at index " + i + " is null.", new InvalidArrayDataException(i));
            //add here one for the others since they are arrays of objects
            if (productsStoichiometry[i] < 0) throw new ArrayException("Stoichiometry at index " + i + " is invalid.",
                    new InvalidArrayDataException(new NumericalException("Negative stoichiometry value for products are not allowed."), i));
        }
    }
}
