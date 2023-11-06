import CHG4343_Design_Project_CustomExcpetions.NumericalException;

public class Flow {
    private ChemicalMixture mixture;
    private double volumetricFlowrate; //m^3/s
    public Flow(ChemicalMixture mixture, double volumetricFlowrate) throws NullPointerException, NumericalException {
        if(mixture == null) throw new NullPointerException("Chemical mixture is null while initializing Flow object");
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        this.mixture=mixture;
        this.volumetricFlowrate=volumetricFlowrate;
    }

    //Copy constructor
    public Flow(Flow source) {
        if(source == null) throw new NullPointerException("Source object in the Flow copy constructor is null");
        this.mixture = new ChemicalMixture(source.mixture);
        this.volumetricFlowrate = source.volumetricFlowrate;

    }//End of copy constructor
    public Flow clone() {
        return new Flow(this);
    }
    public Flow(double volumetricFlowrate) throws NumericalException {
        if(volumetricFlowrate < 0) throw new NumericalException("Negative volumetric flow rate is not allowed");
        this.mixture = new ChemicalMixture();
        this.volumetricFlowrate = volumetricFlowrate;
    }
    public ChemicalMixture getMixture() {
        return this.mixture;
    }
    public boolean setMixture(ChemicalMixture mixture) {
        if(mixture == null) return false;
        this.mixture = mixture.clone();
        return true;
    }
    public double getVolumetricFlowrate() {
        return this.volumetricFlowrate;
    }
    public boolean setVolumetricFlowrate(double volumetricFlowrate) {
        if(volumetricFlowrate < 0) return false;
        this.volumetricFlowrate = volumetricFlowrate;
        return true;
    }

    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        return this.mixture.equals(((Flow) comparator).mixture) && this.volumetricFlowrate == ((Flow) comparator).volumetricFlowrate;
    }

    public double calculateMolarFlowrate(ChemicalSpecies species) throws NumericalException {
        return mixture.calculateMolesSolute(species, this.volumetricFlowrate);
    }

}
