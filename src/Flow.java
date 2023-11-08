import CHG4343_Design_Project_ControlSystem.Controllable;
import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class Flow implements Controllable {
    /* Announced final to have access to the reference of the mixture object in a safe and controlled way.
    * This approach allows to modify mixture object through ChemicalMixture methods, using the reference rather than deep copy.
    * It will be immutable and other classes won't be able to reassign this field to null (null safety) or another Mixture object (invalid state safety)
    * This approach avoids encapsulation of shared data structure, yet remains safe
    * Mixture object is fully encapsulated, so can be modified only through established mutators. */
    public final ChemicalMixture mixture;
    private double volumetricFlowrate;

    /**
     * Constructor for a Flow object.
     * @param mixture ChemicalMixture object.
     * @param volumetricFlowrate Volumetric flow-rate, units: m^3/s.
     * @throws NullPointerException If ChemicalMixture object is null.
     * @throws NumericalException If negative volumetric flow rate is passed.
     */
    public Flow(ChemicalMixture mixture, double volumetricFlowrate) throws NullPointerException, NumericalException {
        if(mixture == null) throw new NullPointerException("Chemical mixture is null while initializing Flow object");
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        // mixture has to be deep copied initially, so instance variable will be reassign safe.
        this.mixture=mixture.clone();
        this.volumetricFlowrate=volumetricFlowrate;
    }
    public Flow(Flow source) {
        if(source == null) throw new NullPointerException("Source object in the Flow copy constructor is null");
        this.mixture = source.mixture.clone();
        this.volumetricFlowrate = source.volumetricFlowrate;
    }
    public Flow clone() {
        return new Flow(this);
    }
    /**
     * Flow constructor that initializes empty chemical mixture.
     * @param volumetricFlowrate Volumetric flow rate, unit: m^3/s.
     * @throws NumericalException If negative volumetric flow rate is passed.
     */
    public Flow(double volumetricFlowrate) throws NumericalException {
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        this.mixture = new ChemicalMixture();
        this.volumetricFlowrate = volumetricFlowrate;
    }

    /* Accessors and Mutators */

    /**
     * Returns deep copy of mixture object
     * @return ChemicalMixture object
     */
    public ChemicalMixture getMixture() {
        return this.mixture.clone();
    }
    /*public boolean setMixture(ChemicalMixture mixture) throws NullPointerException {
        if(mixture == null) throw new NullPointerException("Object Mixture is null.");
        this.mixture = mixture.clone();
        return true;
    }*/
    public double getVolumetricFlowrate() {
        return this.volumetricFlowrate;
    }
    public void setVolumetricFlowrate(double volumetricFlowrate) throws NumericalException {
        if(volumetricFlowrate < 0) throw new NumericalException("Attempting to assign negative volumetric flow rate to Flow object.");
        this.volumetricFlowrate = volumetricFlowrate;
    }

    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        return this.mixture.equals(((Flow) comparator).mixture) && this.volumetricFlowrate == ((Flow) comparator).volumetricFlowrate;
    }
    /**
     * Calculates molar flow rate of specie.
     * @param species Chemical specie object.
     * @return Molar flow-rate of chemical species.
     */
    public double calculateMolarFlowrate(ChemicalSpecies species) {
        return mixture.calculateMolesSolute(species, this.volumetricFlowrate);
    }
    /**
     * adjustControllableParameter implementation in Flow object. Allows to control flow rate.
     * @param value Value to be assigned to the flow rate.
     * @throws NumericalException If value is negative number.
     */
    @Override
    public void adjustControllableParameter(double value) throws NumericalException {
        this.setVolumetricFlowrate(value);
    }
    /**
     * getControllableParameter implementation in Flow object.
     * @return Volumetric flow rate, units: m^3/s.
     */
    @Override
    public double getControllableParameter() {
        return this.volumetricFlowrate;
    }
    @Override
    public String toString() {
        return "{ " + this.mixture.toString() + " } v = " + this.volumetricFlowrate;
    }
}
