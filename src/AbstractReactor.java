import CHG4343_Design_Project_ControlSystem.AbstractController;

public abstract class AbstractReactor {
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
    abstract public AbstractReactor clone();
    abstract public void run(int runTime);
    abstract protected void initializeOutletFlow();
}
