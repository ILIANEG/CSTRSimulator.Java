import CHG4343_Design_Project_ControlSystem.AbstractController;

public abstract class AbstractReactor {
    /* Flows announced as protected since Flow objects might be accessed by reference to change the operating conditions.
    * Inlet flow might be accessed by reference in control loop for example.
    * g_Outlet is a global variable which certainly will be manipulated during runtime. */
    protected Flow inlet;
    protected Flow g_Outlet;
    private AbstractReaction reaction;
    //Constructor for irreversible reaction
    public AbstractReactor(Flow inlet, AbstractReaction reaction) throws NullPointerException {
        if(inlet == null) throw new NullPointerException("Inlet was found to be null while initializing Reactor");
        if(reaction == null) throw new NullPointerException("Reaction was found to be null while initializing Reactor");
        this.inlet = inlet.clone();
        this.reaction = reaction.clone();
        this.initializeOutletFlow();
    }
    public AbstractReactor(AbstractReactor source) throws NullPointerException {
        if(source == null) throw new NullPointerException("Source object in reactor copy constructor is null");
        this.inlet = source.inlet.clone();
        this.reaction = source.reaction.clone();
        if(this.g_Outlet == null) this.g_Outlet = null;
        else this.g_Outlet = source.g_Outlet.clone();
    }
    public Flow getInlet() {
        return this.inlet.clone();
    }
    public void setInlet(Flow inlet) throws NullPointerException {
        if(inlet == null) throw new NullPointerException("Attempting to set null inlet flow in Reactor object");
        this.inlet = inlet.clone();
    }
    public Flow getG_Outlet() {
        if(this.g_Outlet == null) return null;
        return this.g_Outlet.clone();
    }
    public AbstractReaction getReaction() {
        return this.reaction.clone();
    }
    public void setReaction(AbstractReaction reaction) throws NullPointerException {
        if(reaction == null) throw new NullPointerException("Attempting to set null reaction in Reactor object");
        this.reaction = reaction.clone();
    }
    @Override
    // Note that global variable outlet flow should not be checked, since it might be dependent on time
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        if(comparator.getClass() != this.getClass()) return false;
        return this.inlet.equals(((AbstractReactor) comparator).inlet) && this.reaction.equals(((AbstractReactor) comparator).reaction);
    }
    abstract public AbstractReactor clone();
    // Running reactor for given amount of time (will end if steady state is achieved)
    abstract public void run(int runTime, int timeStep);
    // Running reactor till steady state is achieved
    abstract public void run(int timeStep);
    abstract protected void initializeOutletFlow();
}
