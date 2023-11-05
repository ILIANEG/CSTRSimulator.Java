public abstract class AbstractReactor {
    private Flow inlet;
    private Flow outlet;
    private AbstractReaction forwardReaction;
    private AbstractReaction reverseReaction;

    abstract public void run(int runTime);
}
