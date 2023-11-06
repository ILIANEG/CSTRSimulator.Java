public abstract class AbstractReactor {
    private Flow inlet;
    private Flow g_Outlet;
    private ChemicalMixture g_ReactorConditions;
    private AbstractReaction forwardReaction;
    private AbstractReaction reverseReaction;
    private double volume;

    abstract public void run(int runTime);
}
