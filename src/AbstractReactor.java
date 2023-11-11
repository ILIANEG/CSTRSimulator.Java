import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * Class represents and abstract reactor
 */
public abstract class AbstractReactor {
    /* Flows announced as protected since Flow objects might be accessed by reference to change the operating conditions.
    * Inlet flow might be accessed by reference in control loop for example.
    * g_Outlet is a global variable which certainly will be manipulated during runtime. */
    protected Flow inlet;
    protected Flow outlet;
    private AbstractReaction reaction;

    /**
     *
     * @param inlet Flow object representing inlet flow.
     * @param reaction Reaction object representing the reaction occurring in the reactor.
     * @throws NullPointerException If inlet flow or reaction objects are null.
     */
    public AbstractReactor(Flow inlet, Flow outlet, AbstractReaction reaction) throws NullPointerException {
        // Null checks for inlet and reaction
        if(inlet == null) throw new NullPointerException("Inlet was found to be null while initializing Reactor");
        if(reaction == null) throw new NullPointerException("Reaction was found to be null while initializing Reactor");
        // deep copying inlet and reaction
        this.inlet = inlet.clone();
        this.reaction = reaction.clone();
        this.outlet = this.formatOutletFlow(outlet.clone());
    }
    public AbstractReactor(AbstractReactor source) throws NullPointerException{
        // Null check
        if(source == null) throw new NullPointerException("Source object in reactor copy constructor is null");
        this.inlet = source.inlet.clone();
        this.reaction = source.reaction.clone();
        this.outlet = source.outlet;
    }

    /**
     * Abstract clone method.
     * @return Instance of Reactor implementation.
     */
    abstract public AbstractReactor clone();

    /* Accessors and Mutators */

    public Flow getInlet() {
        return this.inlet.clone();
    }
    public void setInlet(Flow inlet) throws NullPointerException {
        if(inlet == null) throw new NullPointerException("Attempting to set null inlet flow in Reactor object");
        this.inlet = inlet.clone();
    }
    public Flow getOutlet() {
        return this.outlet.clone();
    }
    public void setOutlet(Flow outlet) throws NullPointerException {
        if(outlet == null) throw new NullPointerException("Attempting to set null inlet flow in Reactor object");
        this.outlet = this.formatOutletFlow(outlet.clone());
    }
    public AbstractReaction getReaction() {
        return this.reaction.clone();
    }
    public void setReaction(AbstractReaction reaction) throws NullPointerException {
        if(reaction == null) throw new NullPointerException("Attempting to set null reaction in Reactor object");
        this.reaction = reaction.clone();
    }
    @Override
    public boolean equals(Object comparator) {
        // Note that global variable outlet flow should not be checked, since it might be dependent on time in equivalent reactors
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        // Outlet is not checked since is a global variable of state, rather than a property or condition of the reactor.
        return this.inlet.equals(((AbstractReactor) comparator).inlet) && this.reaction.equals(((AbstractReactor) comparator).reaction)
                && this.outlet.equals(((AbstractReactor) comparator).outlet);
    }
    // Running reactor till steady state is achieved
    public abstract void run() throws NumericalException;
    protected Flow formatOutletFlow(Flow outlet) {
        ChemicalSpecies[] inlet = this.inlet.getMixture().getSpecies();
        ChemicalSpecies[] reactants = this.getReaction().getReactants();
        ChemicalSpecies[] products = this.getReaction().getProducts();
        // Add all species from inlet, reactants and products, with concentration 0 if they are not already in the g_Outlet;
        try {
            for (int i = 0; i < inlet.length; i++) {
                outlet.mixture.addSpecies(inlet[i], 0);
            }
            for (int i = 0; i < reactants.length; i++) {
                outlet.mixture.addSpecies(reactants[i], 0);
            }
            for (int i = 0; i < products.length; i++) {
                outlet.mixture.addSpecies(products[i], 0);
            }
        } catch (NumericalException e) {
            /* Exception won't be thrown since 0 is a valid value for concentration */
        }
        return outlet;
    }
}
