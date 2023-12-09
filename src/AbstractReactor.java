import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Class represents and abstract reactor
 */
public abstract class AbstractReactor {
    /* Flows announced as protected since Flow objects might require
    reference access in child class implementation to change the operating conditions. */
    protected Flow inlet;
    protected Flow outlet;
    private AbstractReaction reaction;

    /**
     * Abstract reactor constructor.
     * @param inlet Flow object representing inlet flow.
     * @param reaction Reaction object representing the reaction occurring in the reactor.
     */
    public AbstractReactor(Flow inlet, Flow outlet, AbstractReaction reaction) {
        // Null checks for inlet and reaction
        if(inlet == null) throw new IllegalArgumentException("Inlet was found to be null while initializing Reactor");
        if(reaction == null) throw new IllegalArgumentException("Reaction was found to be null while initializing Reactor");
        // deep copying inlet and reaction
        this.inlet = inlet.clone();
        this.reaction = reaction.clone();
        this.outlet = this.formatOutletFlow(outlet.clone());
    }

    /**
     * Abstract reactor copy constructor.
     * @param source
     */
    public AbstractReactor(AbstractReactor source) throws NullPointerException{
        // Null check
        if(source == null) throw new NullPointerException("Source object in reactor copy constructor is null");
        this.inlet = source.inlet.clone();
        this.reaction = source.reaction.clone();
        this.outlet = source.outlet;
    }

    /**
     * Abstract clone method.
     * @return deep copy of reactor object.
     */
    abstract public AbstractReactor clone();

    /* Accessors and Mutators */

    /**
     * Inlet getter.
     * @return deep copy of inlet object.
     */
    public Flow getInlet() {
        return this.inlet.clone();
    }

    /**
     * Inlet flow object setter.
     * @param inlet
     */
    public void setInlet(Flow inlet) {
        if(inlet == null) throw new IllegalArgumentException("Attempting to set null inlet flow in Reactor object");
        this.inlet = inlet.clone();
    }

    /**
     * Outlet getter
     * @return deep copy of the outlet flow object.
     */
    public Flow getOutlet() {
        return this.outlet.clone();
    }

    /**
     * Outlet setter
     * @param outlet outlet flow object.
     */
    public void setOutlet(Flow outlet) {
        if(outlet == null) throw new IllegalArgumentException("Attempting to set null inlet flow in Reactor object");
        this.outlet = this.formatOutletFlow(outlet.clone());
    }

    /**
     * Reaction getter
     * @return deep copy of chemical reaction object
     */
    public AbstractReaction getReaction() {
        return this.reaction.clone();
    }

    /**
     * Reaction setter.
     * @param reaction reaction object
     */
    public void setReaction(AbstractReaction reaction) throws NullPointerException {
        if(reaction == null) throw new NullPointerException("Attempting to set null reaction in Reactor object");
        this.reaction = reaction.clone();
    }

    /**
     * Equals method.
     * @param comparator
     * @return true is objects are equal.
     */
    @Override
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        // Outlet is not checked since is a global variable of state, rather than a property or condition of the reactor.
        return this.inlet.equals(((AbstractReactor) comparator).inlet) && this.reaction.equals(((AbstractReactor) comparator).reaction)
                && this.outlet.equals(((AbstractReactor) comparator).outlet);
    }

    /**
     * Abstract method that would calculate outlet conditions at steady state based on current conditions.
     */
    public abstract void calculateOutlet();

    /**
     * Helper function that adds all "possible" species to the outlet flow mixture
     * This is made to have a uniform representation of all possible species and avoid adding species
     * that are formed at run time. If any species can be in the system, it will be assigned to the outlet flow.
     * Therefore, it is possible to have an outlet species with concentration = 0 (for example when reaction does not
     * proceed.
     * @param outlet Flow object
     * @return return formatted flow object
     */
    private Flow formatOutletFlow(Flow outlet) {
        ChemicalSpecies[] inlet = this.inlet.getMixture().getSpecies();
        ChemicalSpecies[] reactants = this.getReaction().getReactants();
        ChemicalSpecies[] products = this.getReaction().getProducts();
        // Add all species from inlet, reactants and products, with concentration 0 if they are not already in the g_Outlet;
        // Recall that addSpecies() method adds species only if species is not already in the mixture, otherwise assignment will be ignored
        for (int i = 0; i < inlet.length; i++) {
            outlet.mixture.addSpecies(inlet[i], 0);
        }
        for (int i = 0; i < reactants.length; i++) {
            outlet.mixture.addSpecies(reactants[i], 0);
        }
        for (int i = 0; i < products.length; i++) {
            outlet.mixture.addSpecies(products[i], 0);
        }
        return outlet;
    }
}
