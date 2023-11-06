public abstract class AbstractReactor {
    private Flow inlet;
    private Flow g_Outlet;
    private ChemicalMixture g_ReactorConditions;
    private AbstractReaction forwardReaction;
    private AbstractReaction reverseReaction;
    private double volume;
    //Constructor for irreversible reaction
    public AbstractReactor(Flow inlet, AbstractReaction forwardReaction) throws NullPointerException {
        if(inlet == null) throw new NullPointerException("Inlet was found to be null while initializing Reactor");

    }
    abstract public void run(int runTime);
    public double calculateTau() {
        return this.volume / this.inlet.getVolumetricFlowrate();
    }
}
