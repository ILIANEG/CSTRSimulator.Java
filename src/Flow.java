import CHG4343_Design_Project_CustomExcpetions.NumericalException;

/**
 * FLow class represents a flow of chemical mixture. It aggregates ChemicalMixture and assigns volumetric flowRate.
 */
public class Flow {
    /* Announced final to have access to the reference of the mixture object in a safe and controlled way.
    * This approach allows to modify mixture object through ChemicalMixture methods, using the reference rather than deep copy of ChemicalMixture object itself.
    * Deep copy approach would require large number of array copying and reassignment, which would significantly degrade the performance.
    * Mixture field will be immutable and other classes won't be able to reassign this field to null (null safety) or another Mixture object (invalid state safety)
    * This approach avoids encapsulation of shared data structure, yet remains safe
    * Elements of mixture object (species and concentrations) are fully encapsulated, so can be modified only through established mutators. */
    public final ChemicalMixture mixture;
    private double volumetricFlowrate;

    /**
     * Constructor for a Flow object.
     * @param mixture ChemicalMixture object.
     * @param volumetricFlowrate Volumetric flow-rate, units: m^3/s.
     */
    public Flow(ChemicalMixture mixture, double volumetricFlowrate) {
        if(mixture == null) throw new NullPointerException("Chemical mixture is null while initializing Flow object");
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        // mixture has to be deep copied initially, so instance variable will be reassign safe.
        this.mixture=mixture.clone();
        this.volumetricFlowrate=volumetricFlowrate;
    }

    /**
     * Copy constructor.
     * @param source source FLow object
     */
    public Flow(Flow source) {
        if(source == null) throw new NullPointerException("Source object in the Flow copy constructor is null");
        this.mixture = source.mixture.clone();
        this.volumetricFlowrate = source.volumetricFlowrate;
    }

    /**
     * Clone method.
     * @return deep copy of FLow object.
     */
    public Flow clone() {
        return new Flow(this);
    }

    /**
     * Flow constructor that initializes empty chemical mixture (pure solvent flow).
     * @param volumetricFlowrate Volumetric flow rate with appropriate units.
     */
    public Flow(double volumetricFlowrate) {
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        this.mixture = new ChemicalMixture();
        this.volumetricFlowrate = volumetricFlowrate;
    }

    /* Accessors and Mutators. Mixture is immutable, hence does not require a setter. */

    /**
     * Returns deep copy of mixture object
     * @return ChemicalMixture object
     */
    public ChemicalMixture getMixture() {
        return this.mixture.clone();
    }

    /**
     * Volumetric flow rate getter.
     * @return volumetric flow rate.
     */
    public double getVolumetricFlowrate() {
        return this.volumetricFlowrate;
    }

    /**
     * Volumetric flow rate setter
     * @param volumetricFlowrate
     */
    public void setVolumetricFlowrate(double volumetricFlowrate) {
        if(volumetricFlowrate < 0) throw new NumericalException("Attempting to assign negative volumetric flow rate to Flow object.");
        this.volumetricFlowrate = volumetricFlowrate;
    }

    /**
     * equals method.
     * @param comparator
     * @return true of objects are equal.
     */
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        return this.mixture.equals(((Flow) comparator).mixture) && this.volumetricFlowrate == ((Flow) comparator).volumetricFlowrate;
    }

    /**
     * toString method.
     * @return string representation of the Flow object.
     */
    @Override
    public String toString() {
        return "{ " + this.mixture.toString() + " } v = " + this.volumetricFlowrate;
    }
}
