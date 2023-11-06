import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.InvalidArrayDataException;
import CHG4343_Design_Project_CustomExcpetions.LengthMismatch;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public abstract class AbstractReaction {
    private ChemicalSpecies[] reactants;
    private ChemicalSpecies[] products;
    private double[] reactantsStoichiometry; //Stored as positive values
    private double[] productsStoichiometry; //Stored as positive values
    private double rateConstant; //Positive value always (even if reaction can be seen as a reverse reaction of some arbitrary reaction)
    //Constructor
    public AbstractReaction(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStoichiometry,
                            double[] productsStoichiometry, double rateConstant) throws ArrayException, NumericalException {
        AbstractReaction.validateData(reactants, products, reactantsStoichiometry, productsStoichiometry, rateConstant);
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
        this.rateConstant = rateConstant;

    }
    //Copy constructor
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
        this.rateConstant = source.rateConstant;
    }
    abstract public AbstractReaction clone();
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
    public double getRateConstant() {
        return rateConstant;
    }
    abstract public double calculateReactionRate(ChemicalMixture chemicalComposition);

    public double calculateNetReactionRate(ChemicalMixture chemicalComposition, AbstractReaction reverseReaction) {
        if(chemicalComposition == null) return 0;
        else if(reverseReaction == null) return this.calculateReactionRate(chemicalComposition);
        else return this.calculateReactionRate(chemicalComposition) - reverseReaction.calculateReactionRate(chemicalComposition);
    }
    //Custom validator
    private static void validateData(ChemicalSpecies[] reactants, ChemicalSpecies[] products, double[] reactantsStoichiometry,
                                     double[] productsStoichiometry, double rateConstant) throws ArrayException, NumericalException {
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
            if (reactantsStoichiometry[i] < 0) throw new ArrayException("Stoichiometry at index " + i + " is invalid.",
                    new InvalidArrayDataException(new NumericalException("Negative stoichiometry."), i));
        }
        for (int i = 0; i < products.length; i++) {
            if (products[i] == null)
                throw new ArrayException("Products at index " + i + " is null.", new InvalidArrayDataException(i));
            //add here one for the others since they are arrays of objects
            if (productsStoichiometry[i] < 0) throw new ArrayException("Stoichiometry at index " + i + " is invalid.",
                    new InvalidArrayDataException(new NumericalException("Negative stoichiometry."), i));
        }
        if(0 < rateConstant) throw new NumericalException("Negative rate constant");
    }
}
